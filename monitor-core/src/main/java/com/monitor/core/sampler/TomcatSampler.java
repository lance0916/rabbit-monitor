package com.monitor.core.sampler;

import static com.monitor.core.MonitorConstant.TOMCAT_NAME_PREFIX;

import com.monitor.core.Metric;
import com.monitor.core.MetricsSampler;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Tomcat 信息采集器
 *
 * @author WuQinglong
 * @since 2023/3/1 17:48
 */
public class TomcatSampler implements MetricsSampler {

    private final MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();

    private final boolean embedded;

    public TomcatSampler(boolean embedded) {
        this.embedded = embedded;
    }

    @Override
    public List<Metric> sample() {
        return sampleThreadUsage(embedded ? TomcatRunningMode.EMBEDDED : TomcatRunningMode.STAND_ALONE);
    }

    private List<Metric> sampleThreadUsage(TomcatRunningMode mode) {
        ObjectName objectNamesPattern;
        try {
            objectNamesPattern = new ObjectName(mode.getObjectNamesPattern());
        } catch (MalformedObjectNameException e) {
            log.warn("MalformedObjectName for tomcat executor thread usage: {}", mode.getObjectNamesPattern());
            return Collections.emptyList();
        }
        Set<ObjectName> objectNames = platformMBeanServer.queryNames(objectNamesPattern, null);
        if (objectNames == null || objectNames.isEmpty()) {
            return Collections.emptyList();
        }

        long timestamp = System.currentTimeMillis();
        List<Metric> metrics = new LinkedList<>();

        // 通常情况下只有一个
        for (ObjectName objectName : objectNames) {
            try {
                // 执行器的名字
                String executorName = (String) platformMBeanServer.getAttribute(objectName, "name");
                // 遍历各个属性
                AttributeList attributeList = platformMBeanServer.getAttributes(objectName, mode.getAttributes());
                List<Attribute> attributes = attributeList.asList();
                for (Attribute attribute : attributes) {
                    Object attributeVal = platformMBeanServer.getAttribute(objectName, attribute.getName());
                    metrics.add(new Metric(TOMCAT_NAME_PREFIX + "." + executorName + "." + attribute.getName(),
                        toNumber(attributeVal), timestamp));
                }
            } catch (Exception e) {
                log.error("get mbean attribute failed", e);
            }
        }
        return metrics;
    }

    public enum TomcatRunningMode {
        EMBEDDED("Tomcat:type=ThreadPool,name=*", new String[]{
            // 总线程数
            "currentThreadCount",
            // 工作线程数
            "currentThreadsBusy",
            // 连接数
            "connectionCount"
        }),
        STAND_ALONE("", new String[]{}),
        ;

        private final String objectNamesPattern;
        private final String[] attributes;

        TomcatRunningMode(String objectNamesPattern, String[] attributes) {
            this.objectNamesPattern = objectNamesPattern;
            this.attributes = attributes;
        }

        public String getObjectNamesPattern() {
            return objectNamesPattern;
        }

        public String[] getAttributes() {
            return attributes;
        }
    }

    private Number toNumber(Object val) {
        if (val instanceof Number) {
            return (Number) val;
        }
        throw new RuntimeException("非数值类的值");
    }

}

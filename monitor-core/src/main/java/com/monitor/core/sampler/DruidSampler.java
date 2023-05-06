package com.monitor.core.sampler;

import static com.monitor.core.MonitorConstant.DRUID_NAME_PREFIX;

import com.monitor.core.Metric;
import com.monitor.core.MetricsSampler;
import com.monitor.core.MetricsSamplerException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * Druid 信息采集器
 *
 * @author WuQinglong
 * @since 2023/3/3 09:00
 */
public class DruidSampler implements MetricsSampler {

    private final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    private final Map<String, String> attributeMap = new HashMap<>();
    private final String[] druidAttributes;
    private static final String OBJECT_NAME = "com.alibaba.druid:type=DruidDataSource,id=*";

    {
//        // 空闲连接数
//        attributeMap.put("PoolingCount", "idle");
        // 活跃连接数
        attributeMap.put("ActiveCount", "active");
        // 等待连接的线程数
        attributeMap.put("NotEmptyWaitThreadCount", "awaiting");

        druidAttributes = attributeMap.keySet().toArray(new String[0]);
    }

    @Override
    public List<Metric> sample() {
        Set<ObjectName> objectNames;
        try {
            objectNames = mBeanServer.queryNames(new ObjectName(OBJECT_NAME), null);
        } catch (MalformedObjectNameException e) {
            log.error("the objectNamePattern:{} is not valid!", OBJECT_NAME);
            return Collections.emptyList();
        }
        if (objectNames == null || objectNames.isEmpty()) {
            log.warn("the result of objectNames is empty! objectName:{}", OBJECT_NAME);
            return Collections.emptyList();
        }

        long timestamp = System.currentTimeMillis();
        List<Metric> metrics = new LinkedList<>();

        for (ObjectName objectName : objectNames) {
            AttributeList attributeList;
            try {
                attributeList = mBeanServer.getAttributes(objectName, druidAttributes);
            } catch (InstanceNotFoundException e) {
                log.error("MBean {} has not registered.", objectName.toString());
                continue;
            } catch (ReflectionException e) {
                throw new MetricsSamplerException(e);
            }

            // 为空时，打印 warn 日志
            if (attributeList == null || attributeList.isEmpty()) {
                continue;
            }

            List<Attribute> attributes = attributeList.asList();
            for (Attribute attribute : attributes) {
                String name = attribute.getName();
                Number value = (Number) attribute.getValue();
                metrics.add(new Metric(DRUID_NAME_PREFIX + "." + attributeMap.get(name),
                    value, timestamp));
            }
        }
        return metrics;
    }
}

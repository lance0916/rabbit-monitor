package com.monitor.core.sampler;

import static com.monitor.core.MonitorConstant.HIKARI_NAME_PREFIX;

import com.monitor.core.Metric;
import com.monitor.core.MetricsSampler;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * Hikari 信息采集器
 *
 * @author WuQinglong
 * @since 2023/3/1 11:28
 */
public class HikariSampler implements MetricsSampler {

    private final MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
    private final Map<String, String> attributeMap = new HashMap<>();
    private final String[] hikariAttributes;
    private ObjectName mbeanObjectName = null;

    {
//        // 空闲连接数
//        attributeMap.put("IdleConnections", "idle");
        // 活跃连接数
        attributeMap.put("ActiveConnections", "active");
        // 等待连接的线程数
        attributeMap.put("ThreadsAwaitingConnection", "awaiting");

        hikariAttributes = attributeMap.keySet().toArray(new String[0]);
    }

    public HikariSampler(String poolName) {
        if (poolName == null || poolName.isEmpty()) {
            return;
        }
        try {
            if ("true".equals(System.getProperty("hikaricp.jmx.register2.0"))) {
                mbeanObjectName = new ObjectName("com.zaxxer.hikari:type=Pool,name=" + poolName);
            } else {
                mbeanObjectName = new ObjectName("com.zaxxer.hikari:type=Pool (" + poolName + ")");
            }
        } catch (MalformedObjectNameException e) {
            log.error("fetch Hikari MBean object name failed", e);
        }
    }

    @Override
    public List<Metric> sample() {
        if (mbeanObjectName == null) {
            log.warn("skip sampling hikari metrics with an empty mbean object name.");
            return Collections.emptyList();
        }

        long timestamp = System.currentTimeMillis();
        List<Metric> metrics = new LinkedList<>();

        AttributeList attributeList;
        try {
            attributeList = platformMBeanServer.getAttributes(mbeanObjectName, hikariAttributes);
        } catch (InstanceNotFoundException e) {
            log.warn("mbean object name has not registered.");
            return Collections.emptyList();
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }

        List<Attribute> attributes = attributeList.asList();
        for (Attribute attribute : attributes) {
            String attrName = attribute.getName();
            Number attrValue = (Number) attribute.getValue();
            metrics.add(new Metric(HIKARI_NAME_PREFIX + "." + attributeMap.get(attrName),
                attrValue, timestamp));
        }
        return metrics;
    }

}

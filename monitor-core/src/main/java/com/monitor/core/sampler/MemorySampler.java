package com.monitor.core.sampler;

import static com.monitor.core.MonitorConstant.HEAP_MEMORY_NAME_PREFIX;
import static com.monitor.core.MonitorConstant.NO_HEAP_MEMORY_NAME_PREFIX;

import com.monitor.core.Metric;
import com.monitor.core.MetricsSampler;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedList;
import java.util.List;

/**
 * 内存信息采集
 * <p>
 * 重点关注 已用的 和 已提交的 内存使用情况，对于初始化内存大小和最大内存大小，一般都是通过 JVM 的启动参数来控制的。
 *
 * @author WuQinglong
 * @since 2023/2/24 16:12
 */
public class MemorySampler implements MetricsSampler {

    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Override
    public List<Metric> sample() {
        long timestamp = System.currentTimeMillis();
        List<Metric> metrics = new LinkedList<>();

        // 堆内存
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        metrics.add(new Metric(HEAP_MEMORY_NAME_PREFIX + ".used",
            heapMemoryUsage.getUsed(), timestamp));
        metrics.add(new Metric(HEAP_MEMORY_NAME_PREFIX + ".committed",
            heapMemoryUsage.getCommitted(), timestamp));

        // 堆外内存
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        metrics.add(new Metric(NO_HEAP_MEMORY_NAME_PREFIX + ".used",
            nonHeapMemoryUsage.getUsed(), timestamp));
        metrics.add(new Metric(NO_HEAP_MEMORY_NAME_PREFIX + ".committed",
            nonHeapMemoryUsage.getCommitted(), timestamp));

        return metrics;
    }

}

package com.monitor.core.sampler;

import com.monitor.core.Metric;
import com.monitor.core.MetricsSampler;
import com.monitor.core.MonitorConstant;
import com.monitor.core.StrUtil;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 垃圾处理器信息采集
 *
 * @author WuQinglong
 * @since 2023/2/24 16:12
 */
public class GarbageCollectorSampler implements MetricsSampler {

    private final List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
    private final Map<String, Long> lastGcCounts = new HashMap<>();
    private final Map<String, Long> lastGcTimes = new HashMap<>();

    @Override
    public List<Metric> sample() {
        long timestamp = System.currentTimeMillis();

        List<Metric> metrics = new LinkedList<>();
        for (GarbageCollectorMXBean mxBean : garbageCollectorMXBeans) {
            String memoryManagerName = mxBean.getName();
            memoryManagerName = StrUtil.trimAllWhitespace(memoryManagerName);

            // 执行次数
            long totalCount = mxBean.getCollectionCount();
            long lastCount = lastGcCounts.getOrDefault(memoryManagerName, 0L);
            // 采集时间差内 GC 执行的次数
            long count = totalCount - lastCount;
            metrics.add(new Metric(MonitorConstant.GC_NAME_PREFIX + "." + memoryManagerName + ".count",
                count, timestamp));
            metrics.add(new Metric(MonitorConstant.GC_NAME_PREFIX + "." + memoryManagerName + ".totalCount",
                totalCount, timestamp));
            lastGcCounts.put(memoryManagerName, totalCount);

            // 总的执行时间
            long totalTime = mxBean.getCollectionTime();
            long lastTime = lastGcTimes.getOrDefault(memoryManagerName, 0L);
            // 采集时间差内 GC 执行的总耗时
            long time = totalTime - lastTime;
            metrics.add(new Metric(MonitorConstant.GC_NAME_PREFIX + "." + memoryManagerName + ".time",
                time, timestamp));
            metrics.add(new Metric(MonitorConstant.GC_NAME_PREFIX + "." + memoryManagerName + ".totalTime",
                totalTime, timestamp));
            lastGcTimes.put(memoryManagerName, totalTime);
        }
        return metrics;
    }

}

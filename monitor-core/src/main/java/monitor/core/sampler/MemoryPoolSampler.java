package monitor.core.sampler;

import monitor.core.Metric;
import monitor.core.StrUtil;
import monitor.core.MetricsSampler;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static monitor.core.MonitorConstant.MEMORY_NAME_PREFIX;

/**
 * 内存池信息采集
 *
 * @author WuQinglong
 * @since 2023/2/24 16:12
 */
public class MemoryPoolSampler implements MetricsSampler {

    private final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

    /**
     * 只会监控这些内存池
     * TODO 可配置化
     */
    private final Set<String> monitorSpaceNames = new HashSet<>();

    {
        // Metaspace
//        monitorSpaceNames.add("Metaspace");

        // CMS
        monitorSpaceNames.add("PS Eden Space");
        monitorSpaceNames.add("PS Survivor Space");
        monitorSpaceNames.add("PS Old Gen");

        // G1
        monitorSpaceNames.add("G1 Eden Space");
        monitorSpaceNames.add("G1 Survivor Space");
        monitorSpaceNames.add("G1 Old Gen");
    }

    @Override
    public List<Metric> sample() {
        long timestamp = System.currentTimeMillis();
        List<Metric> metrics = new LinkedList<>();

        for (MemoryPoolMXBean mxBean : memoryPoolMXBeans) {
            MemoryUsage usage = mxBean.getUsage();
            String poolName = mxBean.getName();
            if (!monitorSpaceNames.contains(mxBean.getName())) {
                continue;
            }
            poolName = StrUtil.trimAllWhitespace(poolName);
            metrics.add(new Metric(MEMORY_NAME_PREFIX + "." + poolName + ".used",
                usage.getUsed(), timestamp));
            metrics.add(new Metric(MEMORY_NAME_PREFIX + "." + poolName + ".committed",
                usage.getCommitted(), timestamp));
            metrics.add(new Metric(MEMORY_NAME_PREFIX + "." + poolName + ".max",
                usage.getMax(), timestamp));
        }

        return metrics;
    }

}

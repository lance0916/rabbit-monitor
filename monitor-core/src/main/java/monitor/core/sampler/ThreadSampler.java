package monitor.core.sampler;

import monitor.core.Metric;
import monitor.core.MetricsSampler;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.LinkedList;
import java.util.List;
import monitor.core.MonitorConstant;

/**
 * 线程信息采集
 *
 * @author WuQinglong
 * @since 2023/2/24 16:12
 */
public class ThreadSampler implements MetricsSampler {

    private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    @Override
    public List<Metric> sample() {
        long timestamp = System.currentTimeMillis();
        List<Metric> metrics = new LinkedList<>();

        // 当前活动的线程数量，包括守护程序和非守护程序线程。
        int threadCount = threadMXBean.getThreadCount();
        metrics.add(new Metric(MonitorConstant.THREAD_NAME_PREFIX + ".active", threadCount, timestamp));

        // 守护线程的数量
        int daemonThreadCount = threadMXBean.getDaemonThreadCount();
        metrics.add(new Metric(MonitorConstant.THREAD_NAME_PREFIX + ".daemon", daemonThreadCount, timestamp));

        // 同时活动的线程峰值数量
        int peakThreadCount = threadMXBean.getPeakThreadCount();
        metrics.add(new Metric(MonitorConstant.THREAD_NAME_PREFIX + ".peakActive", peakThreadCount, timestamp));

        // 自JVM启动以来启动过的线程数量
        long totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount();
        metrics.add(new Metric(MonitorConstant.THREAD_NAME_PREFIX + ".total", totalStartedThreadCount, timestamp));

        return metrics;
    }

}

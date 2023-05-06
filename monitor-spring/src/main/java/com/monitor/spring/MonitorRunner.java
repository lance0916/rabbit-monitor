package com.monitor.spring;

import com.monitor.core.MetricsReporter;
import com.monitor.core.MetricsSampler;
import com.monitor.core.Metric;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WuQinglong
 * @since 2023/3/3 16:04
 */
public class MonitorRunner implements Runnable {

    private final Logger log = LoggerFactory.getLogger(MonitorRunner.class);

    private final List<MetricsSampler> samplers;
    private final List<MetricsReporter> reporters;
    private final ThreadPoolExecutor reportThreadPool;

    public MonitorRunner(List<MetricsSampler> samplers, List<MetricsReporter> reporters,
        ThreadPoolExecutor reportThreadPool) {
        this.samplers = samplers;
        this.reporters = reporters;
        this.reportThreadPool = reportThreadPool;
    }

    @Override
    public void run() {
        try {
            doMetrics();
        } catch (Exception e) {
            log.error("采集上报数据异常", e);
        }
    }

    /**
     * 采集并上报数据
     */
    public void doMetrics() {
        // 采集
        List<Metric> metrics = new LinkedList<>();
        for (MetricsSampler sampler : samplers) {
            metrics.addAll(sampler.sample());
        }
        List<Metric> unmodifiableMetrics = Collections.unmodifiableList(metrics);

        // 上报，一个上报器一个线程
        for (MetricsReporter reporter : reporters) {
            reportThreadPool.submit(() -> {
                try {
                    reporter.report(unmodifiableMetrics);
                } catch (Exception e) {
                    log.error("上报数据异常", e);
                }
            });
        }
    }
}

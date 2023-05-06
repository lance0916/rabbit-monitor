package com.monitor.core.reporter;

import com.monitor.core.MetricsReporter;
import com.monitor.core.Metric;
import java.util.List;

/**
 * 日志输出
 *
 * @author WuQinglong
 * @since 2023/3/3 14:38
 */
public class LogReporter implements MetricsReporter {

    @Override
    public void report(List<Metric> metrics) {
        for (Metric metric : metrics) {
            log.info(metric.toString());
        }
    }
}

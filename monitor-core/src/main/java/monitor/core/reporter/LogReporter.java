package monitor.core.reporter;

import monitor.core.MetricsReporter;
import monitor.core.Metric;
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

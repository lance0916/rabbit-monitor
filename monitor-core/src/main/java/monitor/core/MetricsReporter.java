package monitor.core;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 上报器
 *
 * @author WuQinglong
 * @since 2023/2/24 16:12
 */
public interface MetricsReporter {

    Logger log = LoggerFactory.getLogger(MetricsReporter.class);

    void report(List<Metric> metrics);

}

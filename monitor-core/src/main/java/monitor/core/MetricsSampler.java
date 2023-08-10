package monitor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 取样器
 *
 * @author WuQinglong
 * @since 2023/2/24 16:12
 */
public interface MetricsSampler {

    Logger log = LoggerFactory.getLogger(MetricsSampler.class);

    List<Metric> sample();

}

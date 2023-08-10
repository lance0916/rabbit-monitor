package monitor.spring;

import monitor.core.MetricsReporter;
import monitor.core.MetricsSampler;
import monitor.core.reporter.LogReporter;
import monitor.core.sampler.DruidSampler;
import monitor.core.sampler.GarbageCollectorSampler;
import monitor.core.sampler.HikariSampler;
import monitor.core.sampler.MemoryPoolSampler;
import monitor.core.sampler.MemorySampler;
import monitor.core.sampler.ThreadSampler;
import monitor.core.sampler.TomcatSampler;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author WuQinglong
 * @since 2023/3/4 10:56
 */
public class MonitorConfiguration implements InitializingBean, DisposableBean {

    private final Logger log = LoggerFactory.getLogger(MonitorConfiguration.class);

    /**
     * 采集时间间隔，单位：秒
     */
    private int interval = 10;

    /**
     * 采集器
     */
    private final List<MetricsSampler> samplers = new LinkedList<>();

    /**
     * 上报器
     */
    private final List<MetricsReporter> reporters = new LinkedList<>();

    /**
     * 上报数据的线程池
     */
    private ThreadPoolExecutor reportThreadPool;

    /**
     * 调度采集和上报的线程池
     */
    private final ScheduledExecutorService dispatchThreadPool = new ScheduledThreadPoolExecutor(
        1, new DefaultThreadFactory());

    private MonitorConfiguration() {
    }

    public static Builder newBuilder() {
        return new Builder(new MonitorConfiguration());
    }

    @Override
    public void afterPropertiesSet() {
        MonitorRunner runner = new MonitorRunner(samplers, reporters, reportThreadPool);
        dispatchThreadPool.scheduleAtFixedRate(runner, 0, interval, TimeUnit.SECONDS);
        log.info("monitor is started.");
    }

    @Override
    public void destroy() {
        dispatchThreadPool.shutdown();
        log.info("shutdown monitor dispatchThreadPool success.");
        reportThreadPool.shutdown();
        log.info("shutdown monitor reportThreadPool success.");
    }

    public static class Builder {

        private final MonitorConfiguration configuration;

        private Builder(MonitorConfiguration configuration) {
            this.configuration = configuration;
        }

        public Builder enabledMemorySampler() {
            configuration.samplers.add(new MemorySampler());
            return this;
        }

        public Builder enabledMemoryPoolSampler() {
            configuration.samplers.add(new MemoryPoolSampler());
            return this;
        }

        public Builder enabledThreadSampler() {
            configuration.samplers.add(new ThreadSampler());
            return this;
        }

        public Builder enabledGarbageCollectorSampler() {
            configuration.samplers.add(new GarbageCollectorSampler());
            return this;
        }

        public Builder enabledTomcatSampler(boolean embedded) {
            configuration.samplers.add(new TomcatSampler(embedded));
            return this;
        }

        public Builder enabledDruidSampler() {
            configuration.samplers.add(new DruidSampler());
            return this;
        }

        public Builder enabledHikariSampler(String poolName) {
            configuration.samplers.add(new HikariSampler(poolName));
            return this;
        }

        public Builder enabledLogReporter() {
            configuration.reporters.add(new LogReporter());
            return this;
        }

        public Builder addReporter(MetricsReporter reporter) {
            configuration.reporters.add(reporter);
            return this;
        }

        public Builder interval(int interval) {
            configuration.interval = interval;
            return this;
        }

        public Builder reportThreadPool(ThreadPoolExecutor executor) {
            configuration.reportThreadPool = executor;
            return this;
        }

        public Builder reportThreadPool(int minThread, int maxThread) {
            configuration.reportThreadPool = new ThreadPoolExecutor(minThread, maxThread,
                1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(minThread * 3),
                new DefaultThreadFactory());
            return this;
        }

        public MonitorConfiguration build() {
            if (configuration.reportThreadPool == null) {
                int minThread = configuration.reporters.size();
                int maxThread = (int) (configuration.reporters.size() * 1.5);
                configuration.reportThreadPool = new ThreadPoolExecutor(minThread, maxThread,
                    1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(minThread * 3),
                    new DefaultThreadFactory());
            }
            return configuration;
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            namePrefix = "monitorPool-" + poolNumber.getAndIncrement() + "-t-";
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + threadNumber.getAndIncrement());
        }
    }

}

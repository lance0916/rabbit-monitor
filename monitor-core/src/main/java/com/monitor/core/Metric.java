package com.monitor.core;

import java.util.List;

/**
 * @author WuQinglong
 * @since 2023/2/24 16:11
 */
public class Metric {

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private Number value;

    /**
     * 采集时间
     */
    private long timestamp;

    /**
     * Tags
     */
    private List<MetricTag> tags;

    public Metric() {
    }

    public Metric(String name, Number value, long timestamp) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Metric(String name, Number value, long timestamp, List<MetricTag> tags) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<MetricTag> getTags() {
        return tags;
    }

    public void setTags(List<MetricTag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Metric{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                ", tags=" + tags +
                '}';
    }
}

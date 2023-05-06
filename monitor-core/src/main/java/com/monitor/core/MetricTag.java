package com.monitor.core;

public class MetricTag {

    private String name;
    private String value;

    public MetricTag() {
    }

    public MetricTag(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MetricTag{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

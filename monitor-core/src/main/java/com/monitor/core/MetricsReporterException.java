package com.monitor.core;

/**
 * @author WuQinglong
 * @since 2023/3/4 10:46
 */
public class MetricsReporterException extends RuntimeException {

    public MetricsReporterException() {
    }

    public MetricsReporterException(String message) {
        super(message);
    }

    public MetricsReporterException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricsReporterException(Throwable cause) {
        super(cause);
    }

    public MetricsReporterException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package monitor.core;

/**
 * @author WuQinglong
 * @since 2023/3/4 10:46
 */
public class MetricsSamplerException extends RuntimeException {

    public MetricsSamplerException() {
    }

    public MetricsSamplerException(String message) {
        super(message);
    }

    public MetricsSamplerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricsSamplerException(Throwable cause) {
        super(cause);
    }

    public MetricsSamplerException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

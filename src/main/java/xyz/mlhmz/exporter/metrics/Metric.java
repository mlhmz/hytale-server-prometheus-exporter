package xyz.mlhmz.exporter.metrics;

import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;

public interface Metric {
    MetricWithFixedMetadata register();

    String getConfigKey();
}

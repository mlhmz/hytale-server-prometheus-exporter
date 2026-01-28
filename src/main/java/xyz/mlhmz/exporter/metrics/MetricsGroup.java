package xyz.mlhmz.exporter.metrics;

import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;

import java.util.List;

public interface MetricsGroup {
    List<MetricWithFixedMetadata> register();

    String getConfigKey();
}

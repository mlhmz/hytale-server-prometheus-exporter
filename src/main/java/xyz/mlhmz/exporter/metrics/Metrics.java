package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.logger.HytaleLogger;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;

import java.util.List;
import java.util.logging.Level;

public record Metrics(HytaleLogger logger, List<Metric> metricInstances) {
    public static Metrics create(HytaleLogger logger, Metric... metricInstances) {
        return new Metrics(logger, List.of(metricInstances));
    }

    public void register() {
        metricInstances.forEach(instance -> {
            MetricWithFixedMetadata metric = instance.register();
            logger.at(Level.INFO).log("Registered metric '%s' of type '%s'.",
                    metric.getPrometheusName(),
                    metric.getClass().getSimpleName());
        });
    }
}

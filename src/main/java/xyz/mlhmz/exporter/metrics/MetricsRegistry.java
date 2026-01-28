package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.logger.HytaleLogger;

import java.util.List;
import java.util.logging.Level;

public class MetricsRegistry {
    private final HytaleLogger logger;
    private final List<MetricsGroup> metricsGroupList;

    public MetricsRegistry(HytaleLogger logger, List<MetricsGroup> metricsGroupList) {
        this.logger = logger;
        this.metricsGroupList = metricsGroupList;
    }

    public void register() {
        metricsGroupList.forEach(this::registerMetricGroup);
    }

    private void registerMetricGroup(MetricsGroup instance) {
        instance.register()
                .forEach(metric -> logger.at(Level.INFO).log("Registered metric '%s' of type '%s'.",
                        metric.getPrometheusName(),
                        metric.getClass().getSimpleName()));
    }
}

package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.server.core.universe.Universe;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;

public record TotalPlayerCountGaugeMetric(Universe universe) implements Metric {
    @Override
    public MetricWithFixedMetadata register() {
        return GaugeWithCallback.builder()
                .name("hytale_player_count")
                .help("Total player count")
                .callback(callback -> {
                    Universe universe = Universe.get();
                    int playerCount = universe.getPlayerCount();
                    callback.call(playerCount);
                })
                .register();
    }

    @Override
    public String getConfigKey() {
        return "playerCount";
    }
}

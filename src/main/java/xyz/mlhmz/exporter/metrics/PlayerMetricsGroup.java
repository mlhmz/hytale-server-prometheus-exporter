package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.server.core.universe.Universe;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PlayerMetricsGroup implements MetricsGroup {
    private final Universe universe;

    @Override
    public List<MetricWithFixedMetadata> register() {
        GaugeWithCallback playerCountGauge = GaugeWithCallback.builder()
                .name("hytale_player_count")
                .help("Total player count")
                .callback(callback -> {
                    int playerCount = universe.getPlayerCount();
                    callback.call(playerCount);
                })
                .register();

        return List.of(playerCountGauge);
    }

    @Override
    public String getConfigKey() {
        return "playerCount";
    }
}

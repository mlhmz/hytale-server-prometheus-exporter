package xyz.mlhmz.exporter.gauges;

import com.hypixel.hytale.server.core.universe.Universe;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.registry.PrometheusRegistry;

import java.util.Objects;

public class PlayerCountGauge implements HytaleGauge {
    public static final String GAUGE_NAME = "hytale_player_count";
    private Gauge gauge = null;

    @Override
    public Gauge register() {
        gauge = Gauge.builder()
                .name(GAUGE_NAME)
                .help("Total player count")
                .register();
        return gauge;
    }

    @Override
    public void run() {
        Universe universe = Universe.get();
        int playerCount = universe.getPlayerCount();
        Objects.requireNonNull(gauge, String.format("The gauge %s was not initialized.", GAUGE_NAME))
                .set(playerCount);
    }
}

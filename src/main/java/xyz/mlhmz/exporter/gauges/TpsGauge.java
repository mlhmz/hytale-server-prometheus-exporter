package xyz.mlhmz.exporter.gauges;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.metrics.metric.HistoricMetric;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.commands.world.perf.WorldPerfCommand;
import io.prometheus.metrics.core.metrics.Gauge;

import java.util.Objects;
import java.util.logging.Level;

public class TpsGauge implements HytaleGauge {
    public static final String GAUGE_NAME = "hytale_tps";
    private final HytaleLogger logger;
    private Gauge gauge = null;

    public TpsGauge(HytaleLogger logger) {
        this.logger = logger;
    }

    @Override
    public Gauge register() {
        gauge = Gauge.builder()
                .name(GAUGE_NAME)
                .help("TPS counter per world")
                .labelNames("world")
                .register();
        return gauge;
    }

    @Override
    public void run() {
        Universe universe = Universe.get();

        for (World world : universe.getWorlds().values()) {
            int tickStepNanos = world.getTickStepNanos();
            HistoricMetric metric = world.getBufferedTickLengthMetricSet();
            double tps = WorldPerfCommand.tpsFromDelta(metric.getLastValue(), tickStepNanos);
            String worldName = world.getName();
            Objects.requireNonNull(gauge, String.format("The gauge %s was not initialized.", GAUGE_NAME))
                    .labelValues(worldName)
                    .set(tps);
            logger.at(Level.INFO).log("Set gauge '%s' on world '%s' to '%f'", GAUGE_NAME, worldName, tps);
        }
    }
}

package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.metrics.metric.HistoricMetric;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.commands.world.perf.WorldPerfCommand;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;

public record TpsGaugeMetric(Universe universe) implements Metric {
    @Override
    public MetricWithFixedMetadata register() {
        return GaugeWithCallback.builder()
                .name("hytale_tps")
                .help("TPS counter per world")
                .labelNames("world")
                .callback(callback -> {
                    for (World world : universe.getWorlds().values()) {
                        int tickStepNanos = world.getTickStepNanos();
                        HistoricMetric metric = world.getBufferedTickLengthMetricSet();
                        double tps = WorldPerfCommand.tpsFromDelta(metric.getLastValue(), tickStepNanos);
                        String worldName = world.getName();
                        callback.call(tps, worldName);
                    }
                }).register();
    }

    @Override
    public String getConfigKey() {
        return "tps";
    }
}

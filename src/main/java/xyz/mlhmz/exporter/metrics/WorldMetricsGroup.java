package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.metrics.metric.HistoricMetric;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.commands.world.perf.WorldPerfCommand;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;

import java.util.List;
import java.util.Map;

public record WorldMetricsGroup(Universe universe) implements MetricsGroup {
    @Override
    public List<MetricWithFixedMetadata> register() {

        Map<String, World> worlds = universe.getWorlds();

        GaugeWithCallback tpsGauge = GaugeWithCallback.builder()
                .name("hytale_tps")
                .help("TPS counter per world")
                .labelNames("world")
                .callback(callback -> {
                    for (var worldSet : worlds.entrySet()) {
                        World world = worldSet.getValue();
                        int tickStepNanos = world.getTickStepNanos();
                        HistoricMetric metric = world.getBufferedTickLengthMetricSet();
                        double tps = WorldPerfCommand.tpsFromDelta(metric.getLastValue(), tickStepNanos);
                        callback.call(tps, worldSet.getKey());
                    }
                }).register();

        return List.of(tpsGauge);
    }

    @Override
    public String getConfigKey() {
        return "tps";
    }
}

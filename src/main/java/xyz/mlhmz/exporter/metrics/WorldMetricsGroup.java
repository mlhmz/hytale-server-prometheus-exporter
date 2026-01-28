package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.metrics.metric.HistoricMetric;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.commands.world.perf.WorldPerfCommand;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WorldMetricsGroup implements MetricsGroup {
    private final Universe universe;

    @Override
    public List<MetricWithFixedMetadata> register() {

        Map<String, World> worlds = universe.getWorlds();

        GaugeWithCallback tpsGauge = GaugeWithCallback.builder()
                .name("hytale_tps")
                .help("TPS counter per world")
                .labelNames("world")
                .callback(callback -> mapWorldTPSIntoMap(worlds)
                        .forEach((worldName, tps) -> callback.call(tps, worldName)))
                .register();

        GaugeWithCallback activeChunksGauge = GaugeWithCallback.builder()
                .name("hytale_world_active_chunks")
                .help("Active chunks per world")
                .labelNames("world")
                .callback(callback -> mapWorldActiveChunksIntoMap(worlds)
                        .forEach((worldName, activeChunks) -> callback.call(activeChunks, worldName)))
                .register();

        return List.of(tpsGauge, activeChunksGauge);
    }

    private Map<String, Integer> mapWorldActiveChunksIntoMap(Map<String, World> worlds) {
        return worlds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getChunkStore().getLoadedChunksCount()
                ));
    }

    private Map<String, Double> mapWorldTPSIntoMap(Map<String, World> worlds) {
        return worlds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        this::retrieveTPSFromWorldEntry
                ));
    }

    private double retrieveTPSFromWorldEntry(Map.Entry<String, World> entry) {
        int tickStepNanos = entry.getValue().getTickStepNanos();
        HistoricMetric metric = entry.getValue().getBufferedTickLengthMetricSet();
        return WorldPerfCommand.tpsFromDelta(metric.getLastValue(), tickStepNanos);
    }

    @Override
    public String getConfigKey() {
        return "worldMetrics";
    }
}

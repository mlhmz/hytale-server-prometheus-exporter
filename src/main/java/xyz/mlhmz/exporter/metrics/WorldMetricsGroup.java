package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.metrics.metric.HistoricMetric;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.commands.world.perf.WorldPerfCommand;
import io.prometheus.metrics.core.metrics.CounterWithCallback;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WorldMetricsGroup implements MetricsGroup {
    private final Universe universe;

    @Override
    public List<MetricWithFixedMetadata> register() {
        Objects.requireNonNull(universe, "Universe cannot be null");

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

        CounterWithCallback chunkLoadedTotal = CounterWithCallback.builder()
                .name("hytale_chunk_loaded_total")
                .help("Total chunk loaded rate per world")
                .labelNames("world")
                .callback(callback -> mapWorldTotalLoadedChunksIntoMap(worlds).forEach((worldName, generatedChunks) ->
                        callback.call(generatedChunks, worldName)))
                .register();

        CounterWithCallback chunkGeneratedTotal = CounterWithCallback.builder()
                .name("hytale_chunk_generated_total")
                .help("Total generated chunks per world")
                .labelNames("world")
                .callback(callback -> mapWorldTotalGeneratedChunksIntoMap(worlds).forEach((worldName, generatedChunks) ->
                        callback.call(generatedChunks, worldName)))
                .register();

        return List.of(
                tpsGauge,
                activeChunksGauge,
                chunkLoadedTotal,
                chunkGeneratedTotal
        );
    }


    private Map<String, Integer> mapWorldTotalLoadedChunksIntoMap(Map<String, World> worlds) {
        return worlds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .getChunkStore()
                                .getTotalLoadedChunksCount()
                ));
    }


    private Map<String, Integer> mapWorldTotalGeneratedChunksIntoMap(Map<String, World> worlds) {
        return worlds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .getChunkStore()
                                .getTotalGeneratedChunksCount()
                ));
    }

    private Map<String, Integer> mapWorldActiveChunksIntoMap(Map<String, World> worlds) {
        return worlds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .getChunkStore()
                                .getLoadedChunksCount()
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
}

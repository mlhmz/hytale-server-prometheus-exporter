package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EntitiesMetricsGroup implements MetricsGroup {
    private final Universe universe;


    @Override
    public List<MetricWithFixedMetadata> register() {
        Objects.requireNonNull(universe, "Universe cannot be null");

        Map<String, World> worlds = universe.getWorlds();

        GaugeWithCallback entityCountGauge = GaugeWithCallback.builder()
                .name("hytale_world_entity_count")
                .help("Entity count per world")
                .labelNames("world")
                .callback(callback -> mapWorldEntityCountsIntoMap(worlds)
                        .forEach((worldName, entityCount) -> callback.call(entityCount, worldName)))
                .register();

        return List.of(entityCountGauge);
    }

    private Map<String, Integer> mapWorldEntityCountsIntoMap(Map<String, World> worlds) {
        return worlds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            EntityStore entityStore = entry.getValue().getEntityStore();
                            return entityStore.getStore().getEntityCount();
                        }
                ));
    }
}

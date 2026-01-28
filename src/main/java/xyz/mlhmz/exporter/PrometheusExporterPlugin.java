package xyz.mlhmz.exporter;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.Config;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import xyz.mlhmz.exporter.metrics.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class PrometheusExporterPlugin extends JavaPlugin {
    private static HTTPServer server = null;
    private final Config<PluginConfig> configWrapper;

    public PrometheusExporterPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        configWrapper = withConfig(PluginConfig.newInstance());
    }

    @Override
    protected void start() {
        PluginConfig config = this.configWrapper.get();

        Objects.requireNonNull(config, "PluginConfig cannot be null.");

        getLogger().at(Level.INFO).log("Prometheus Exporter successfully started!");

        registerHytaleMetrics(config);

        JvmMetrics.builder().register();

        try {
            HTTPServer.Builder serverBuilder = HTTPServer.builder()
                    .port(config.getPort());
            if (isHostNameNotNullEmptyOrBlank(config)) {
                serverBuilder = serverBuilder.hostname(config.getHostName());
            }
            server = serverBuilder
                    .buildAndStart();
        } catch (IOException e) {
            getLogger().at(Level.SEVERE).log("An error occured while starting the prometheus server", e);
            throw new RuntimeException(e);
        }
    }

    private static boolean isHostNameNotNullEmptyOrBlank(PluginConfig config) {
        return config.getHostName() != null && !config.getHostName().isEmpty() && !config.getHostName().isBlank();
    }

    private void registerHytaleMetrics(PluginConfig config) {
        Universe universe = Universe.get();

        List<MetricsGroup> metricsGroupList = new ArrayList<>();

        if (config.isWorldMetricsEnabled()) {
            metricsGroupList.add(new WorldMetricsGroup(universe));
        }

        if (config.isPlayerMetricsEnabled()) {
            metricsGroupList.add(new PlayerMetricsGroup(universe));
        }

        if (config.isEntityMetricsEnabled()) {
            metricsGroupList.add(new EntitiesMetricsGroup(universe));
        }

        MetricsRegistry metricsRegistry = new MetricsRegistry(
                getLogger(),
                metricsGroupList
        );

        metricsRegistry.register();
    }


    @Override
    public void shutdown() {
        server.stop();
        getLogger().at(Level.INFO).log("Plugin stopping.");
    }
}

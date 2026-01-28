package xyz.mlhmz.exporter;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import xyz.mlhmz.exporter.metrics.Metrics;
import xyz.mlhmz.exporter.metrics.TotalPlayerCountGaugeMetric;
import xyz.mlhmz.exporter.metrics.TpsGaugeMetric;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.logging.Level;

public class PrometheusExporterPlugin extends JavaPlugin {
    private static HTTPServer server = null;

    public PrometheusExporterPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void start() {
        getLogger().at(Level.INFO).log("Prometheus Exporter successfully started!");


        JvmMetrics.builder().register();

        Universe universe = Universe.get();

        Metrics.create(
                getLogger(),
                new TpsGaugeMetric(universe),
                new TotalPlayerCountGaugeMetric(universe)
        ).register();

        try {
            server = HTTPServer.builder()
                    .port(9400)
                    .buildAndStart();
        } catch (IOException e) {
            getLogger().at(Level.SEVERE).log("An error occured while starting the prometheus server", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void shutdown() {
        server.stop();
        getLogger().at(Level.INFO).log("Plugin stopping.");
    }
}

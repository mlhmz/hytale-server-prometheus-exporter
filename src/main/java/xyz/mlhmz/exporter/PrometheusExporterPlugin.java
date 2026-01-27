package xyz.mlhmz.exporter;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import xyz.mlhmz.exporter.gauges.HytaleGauges;
import xyz.mlhmz.exporter.gauges.PlayerCountGauge;
import xyz.mlhmz.exporter.gauges.TpsGauge;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
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


        HytaleGauges hytaleGauges = new HytaleGauges(List.of(
                new TpsGauge(getLogger()),
                new PlayerCountGauge()
        ), getLogger());
        hytaleGauges.init();

        HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(hytaleGauges::run, 5, 5, TimeUnit.SECONDS);

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

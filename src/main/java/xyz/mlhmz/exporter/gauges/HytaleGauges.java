package xyz.mlhmz.exporter.gauges;

import com.hypixel.hytale.logger.HytaleLogger;
import io.prometheus.metrics.core.metrics.Gauge;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HytaleGauges {
    private final List<HytaleGauge> gauges;
    private final HytaleLogger logger;

    public HytaleGauges(List<HytaleGauge> gauges, HytaleLogger logger) {
        this.gauges = gauges;
        this.logger = logger;
    }


    public void init() {
        gauges.forEach(gauge -> {
            Gauge registeredGauge = gauge.register();
            logger.at(Level.INFO).log("Gauge '%s' initialized.", registeredGauge.getPrometheusName());
        });
    }

    public void run() {
        gauges.forEach(HytaleGauge::run);
    }
}

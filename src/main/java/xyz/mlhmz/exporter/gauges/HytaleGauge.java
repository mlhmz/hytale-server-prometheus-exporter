package xyz.mlhmz.exporter.gauges;

import io.prometheus.metrics.core.metrics.Gauge;

interface HytaleGauge {
    Gauge register();

    void run();
}

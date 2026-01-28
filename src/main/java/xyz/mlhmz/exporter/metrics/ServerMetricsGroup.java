package xyz.mlhmz.exporter.metrics;

import com.hypixel.hytale.server.core.modules.accesscontrol.provider.HytaleWhitelistProvider;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;
import io.prometheus.metrics.core.metrics.MetricWithFixedMetadata;

import java.util.List;

public class ServerMetricsGroup implements MetricsGroup {
    @Override
    public List<MetricWithFixedMetadata> register() {
        GaugeWithCallback whitelistedPlayerGauge = GaugeWithCallback.builder()
                .name("hytale_whitelisted_player_count")
                .help("Total whitelisted player count")
                .callback(callback -> callback.call(new HytaleWhitelistProvider()
                        .getList()
                        .size()))
                .register();

        return List.of(whitelistedPlayerGauge);
    }
}

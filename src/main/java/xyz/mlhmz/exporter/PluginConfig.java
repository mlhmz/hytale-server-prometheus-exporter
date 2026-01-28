package xyz.mlhmz.exporter;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class PluginConfig {
    private String hostName = null;
    private int port = 9400;
    private boolean worldMetricsEnabled = true;
    private boolean playerMetricsEnabled = true;
    private boolean entityMetricsEnabled = true;

    public static BuilderCodec<PluginConfig> newInstance() {
        return BuilderCodec.builder(PluginConfig.class, PluginConfig::new)
                .append(new KeyedCodec<>("HostName", BuilderCodec.STRING),
                        PluginConfig::setHostName,
                        PluginConfig::getHostName
                ).add()
                .append(
                        new KeyedCodec<>("Port", BuilderCodec.INTEGER),
                        PluginConfig::setPort,
                        PluginConfig::getPort
                ).add()
                .append(
                        new KeyedCodec<>("WorldMetricsEnabled", BuilderCodec.BOOLEAN),
                        PluginConfig::setWorldMetricsEnabled,
                        PluginConfig::isWorldMetricsEnabled
                ).add()
                .append(
                        new KeyedCodec<>("PlayerMetricsEnabled", BuilderCodec.BOOLEAN),
                        PluginConfig::setPlayerMetricsEnabled,
                        PluginConfig::isPlayerMetricsEnabled
                ).add()
                .append(
                        new KeyedCodec<>("EntityMetricsEnabled", BuilderCodec.BOOLEAN),
                        PluginConfig::setEntityMetricsEnabled,
                        PluginConfig::isEntityMetricsEnabled
                ).add()
                .build();

    }
}

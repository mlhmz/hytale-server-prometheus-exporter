# Hytale Server Prometheus Exporter 🔥

This plugin exposes a set of Hytale server metrics over an embedded Prometheus HTTP server so a Prometheus standalone server (or any Prometheus-compatible scraper) can scrape them.

### Main features

- Exposes Hytale metrics per-world and server-wide (see list below).
- Exposes JVM-level metrics (GC, memory, threads, classloader, etc.) using the Prometheus `JvmMetrics` instrumentation.
- Runs an embedded Prometheus HTTP server (via the Prometheus Java exporter) to serve metrics.

### Configuration

The plugin writes and reads a simple config file (defaults shown). The available configuration options are:

- `HostName` (string, default: `null`) — optional hostname to bind the metrics HTTP server to. If omitted the server will bind to all interfaces.
- `Port` (integer, default: `9400`) — port for the embedded metrics HTTP server.
- `WorldMetricsEnabled` (boolean, default: `true`) — enable per-world metrics (TPS, active chunks, generation counts).
- `PlayerMetricsEnabled` (boolean, default: `true`) — enable player metrics (player count).
- `EntityMetricsEnabled` (boolean, default: `true`) — enable entity metrics (entity count per world).
- `ServerMetricsEnabled` (boolean, default: `true`) — (reserved) server-level metrics toggle (currently unused in code but reserved in config).

You can edit the generated config file in the server mods/plugins folder after the plugin has run once, or change values before first start by creating the plugin's config file using the same keys.

### Exposed Hytale metrics

The plugin currently registers the following metrics (Prometheus names):

- `hytale_tps{world="<world>"}` — current ticks-per-second per world.
- `hytale_world_active_chunks{world="<world>"}` — number of active (loaded) chunks per world.
- `hytale_chunk_generation_total{world="<world>"}` — total chunks loaded per world.
- `hytale_chunk_generation_rate_total{world="<world>"}` — total chunk generation rate per world.
- `hytale_player_count` — total players across the server.
- `hytale_world_entity_count{world="<world>"}` — number of entities in each world.

Additionally the Prometheus Java JvmMetrics instrumentation is registered which exposes standard JVM metrics (memory pools, GC, threads, classloader, buffer pools, etc.).

### How it serves metrics

The plugin starts an embedded Prometheus HTTP server (from the Prometheus Java exporter library) on the configured `HostName`/`Port`. By default the server listens on port `9400` on all interfaces.

Prometheus (the standalone server) should be configured to scrape the Hytale server at:

`<HOST>:9400/metrics`

(Replace `<HOST>` with the Hytale server's address or hostname.)

If you run Hytale in Docker, make sure Prometheus can reach the plugin's HTTP server (same Docker network or port mapping).

### Notes and next steps / improvements

- The plugin currently focuses on high-level counters and gauges. Future additions could include: chunk tick latency histograms, dropped-item counts, and more detailed entity breakdowns.

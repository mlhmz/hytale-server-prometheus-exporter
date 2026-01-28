# Hytale Server Prometheus Exporter

Work in Progress Prometheus exporter for Hytale Servers. 
Utilizes the Prometheus Java Lib, already exports the usual JvmMetrics, the Player Count, 
and the World TPS.

## Server Setup
1. Put plugin into the servers mods folder
2. Add the server into the prometheus configuration with the metricsRegistry endpoint `<HOST>:9400/metricsRegistry` (And put the Prometheus and Hytale Server into a docker network if the hytale server is run via docker like I do it)

## TODOs
- [ ] Gather more metricsRegistry
- [ ] Make web server port in config configurable
- [ ] Setup version structure
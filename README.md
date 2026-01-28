# Hytale Server Prometheus Exporter

Work in Progress Prometheus exporter for Hytale Servers. 
Utilizes the Prometheus Java Lib, already exports the usual JvmMetrics, the Player Count, 
and the World TPS (that is apparently fixed, so useless?)

## Dev Setup
Right now the process, to use the API is a bit hacky, which is kinda my fault, because its easier to import local files in gradle.

Hopefully we can expect in the future to get  an actual maven repository that holds the server api?

1. Get hytale server jar with the [Hytale Downloader](https://support.hytale.com/hc/en-us/articles/45326769420827-Hytale-Server-Manual#server-setup)
2. Add jar to project local-maven-repo with `mvn deploy:deploy-file -Dfile=HytaleServer.jar -DgroupId=com.hypixel.hytale -DartifactId=server -Dversion=0.0.0 -DgeneratePom=true -Durl=file:<PROJECT_LOCATION>/local-maven-repo -DrepositoryId=local-maven-repo -DupdateReleaseInfo=true`
3. Compile plugin with `mvn clean install`

## Server Setup
1. Put plugin into the servers mods folder
2. Add the server into the prometheus configuration with the metricsRegistry endpoint `<HOST>:9400/metricsRegistry` (And put the Prometheus and Hytale Server into a docker network if the hytale server is run via docker like I do it)

## TODOs
- [ ] Gather more metricsRegistry
  - [ ] And gather an actual better metric that shows if the server lags (World TPS apparently stays at 30.0 - skill issue?)
- [ ] Make web server port in config configurable
- [ ] Setup version structure
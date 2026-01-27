
Amazon Corretto 21 has a 'headless' variant available. This variant omits runtime dependencies
that are typically associated with GUI applications such as X11 and ALSA and is worth considering
for server-oriented workloads. The 'headful' variant adds support for X11 and ALSA
```sh
docker pull amazoncorretto:21.0.9-al2023-headless # 136.6MB
docker pull amazoncorretto:21.0.9-al2023-headful # 137.28MB
docker pull amazoncorretto:21.0.9-al2023 # 213.79MB
```
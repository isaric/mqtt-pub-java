# mqtt-pub-java
This demo application is written in Java using the Gradle build system. It publishes mock sensor readings to an MQTT broker.

## New: YAML-driven generic sensors
- Sensors are now configured via YAML. A single generic sensor class generates values between min/max with a given step.
- RoomReport now contains a dynamic map of sensor label -> value.
- MQTT clientId, broker address, topic, and publish interval are configurable.

## Configuration
Provide configuration via one of these environment variables:
- APP_CONFIG: the YAML contents directly.
- APP_CONFIG_PATH: a file path to a YAML file.

Example YAML (see `app/bin/sample-config.yaml`):

```
mqtt:
  addr: "tcp://broker:1883"
  clientId: "room-sensor-1"
  topic: "sensors/room"
  intervalMs: 1000
sensors:
  - label: "temperature"
    min: 15.0
    max: 35.0
    step: 0.5
    initial: 25.0
    integer: false
  - label: "co2Level"
    min: 450
    max: 1200
    step: 50
    initial: 600
    integer: true
```

## Build and Run
- Build: `./gradlew :app:build`
- Run (using a file):
  - `export APP_CONFIG_PATH=$(pwd)/app/bin/sample-config.yaml`
  - `./gradlew :app:run`
- Or run (inline):
  - `export APP_CONFIG="$(cat app/bin/sample-config.yaml)"`
  - `./gradlew :app:run`

The app will connect to `mqtt.addr`, publish JSON to `mqtt.topic` every `mqtt.intervalMs` milliseconds, and identify as `mqtt.clientId`.

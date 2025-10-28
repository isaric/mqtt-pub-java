package com.path.variable.mqtt.pub.demo.config;

import java.util.List;

public record AppConfig(MqttConfig mqtt, List<SensorConfig> sensors) {
}

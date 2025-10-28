package com.path.variable.mqtt.pub.demo.config;

public record MqttConfig(String addr, String clientId, String topic, Integer intervalMs) {
}

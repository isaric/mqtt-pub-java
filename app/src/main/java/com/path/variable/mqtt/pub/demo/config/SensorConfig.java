package com.path.variable.mqtt.pub.demo.config;

public record SensorConfig(String label, double min, double max, double step, Double initial, Boolean integer) {
    public double initialOrDefault() {
        if (initial != null) return initial;
        return (min + max) / 2.0;
    }
    public boolean integerOrDefault() {
        return integer != null && integer;
    }
}

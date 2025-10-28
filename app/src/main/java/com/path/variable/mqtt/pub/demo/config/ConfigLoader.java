package com.path.variable.mqtt.pub.demo.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigLoader {
    public static final String ENV_CONFIG_PATH = "APP_CONFIG_PATH";
    public static final String ENV_CONFIG_YAML = "APP_CONFIG";

    public static AppConfig load() {
        String yamlContent = System.getenv(ENV_CONFIG_YAML);
        if (yamlContent == null || yamlContent.isBlank()) {
            String path = System.getenv(ENV_CONFIG_PATH);
            if (path == null || path.isBlank()) {
                throw new IllegalStateException("No configuration provided. Set APP_CONFIG with YAML contents or APP_CONFIG_PATH with a file path.");
            }
            try {
                yamlContent = Files.readString(Path.of(path));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read YAML from APP_CONFIG_PATH: " + path, e);
            }
        }
        var yaml = new Yaml();
        Map<String, Object> root = yaml.load(yamlContent);
        return toConfig(root);
    }

    @SuppressWarnings("unchecked")
    private static AppConfig toConfig(Map<String, Object> root) {
        if (root == null) throw new IllegalArgumentException("YAML content is empty");
        var mqttMap = (Map<String, Object>) root.get("mqtt");
        if (mqttMap == null) throw new IllegalArgumentException("Missing 'mqtt' section in YAML");
        String addr = str(mqttMap.get("addr"));
        String clientId = str(mqttMap.getOrDefault("clientId", "room-sensor"));
        String topic = str(mqttMap.get("topic"));
        Integer intervalMs = asInt(mqttMap.getOrDefault("intervalMs", 1000));
        var mqtt = new MqttConfig(addr, clientId, topic, intervalMs);

        var sensorsList = (Iterable<Map<String, Object>>) root.get("sensors");
        if (sensorsList == null) throw new IllegalArgumentException("Missing 'sensors' array in YAML");
        var list = new java.util.ArrayList<SensorConfig>();
        for (Map<String, Object> s : sensorsList) {
            String label = str(s.get("label"));
            double min = asDouble(s.get("min"));
            double max = asDouble(s.get("max"));
            double step = asDouble(s.get("step"));
            Double initial = s.get("initial") == null ? null : asDouble(s.get("initial"));
            Boolean integer = s.get("integer") == null ? null : asBoolean(s.get("integer"));
            if (label == null || label.isBlank()) throw new IllegalArgumentException("Each sensor requires a non-empty 'label'");
            if (max < min) throw new IllegalArgumentException("Sensor '" + label + "': max < min");
            if (step <= 0) throw new IllegalArgumentException("Sensor '" + label + "': step must be > 0");
            if (initial != null && (initial < min || initial > max)) throw new IllegalArgumentException("Sensor '" + label + "': initial out of range");
            list.add(new SensorConfig(label, min, max, step, initial, integer));
        }
        return new AppConfig(mqtt, list);
    }

    private static String str(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static double asDouble(Object o) {
        if (o instanceof Number n) return n.doubleValue();
        return Double.parseDouble(String.valueOf(o));
    }

    private static int asInt(Object o) {
        if (o instanceof Number n) return n.intValue();
        return Integer.parseInt(String.valueOf(o));
    }

    private static boolean asBoolean(Object o) {
        if (o instanceof Boolean b) return b;
        return Boolean.parseBoolean(String.valueOf(o));
    }
}

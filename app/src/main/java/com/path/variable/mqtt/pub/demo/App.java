package com.path.variable.mqtt.pub.demo;

import com.path.variable.mqtt.pub.demo.config.AppConfig;
import com.path.variable.mqtt.pub.demo.config.ConfigLoader;
import com.path.variable.mqtt.pub.demo.publisher.MqttPublisher;
import com.path.variable.mqtt.pub.demo.sensor.GenericSensor;
import com.path.variable.mqtt.pub.demo.sensor.report.RoomReport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static java.lang.Thread.sleep;

public class App {

    public void run() {
        final AppConfig config = ConfigLoader.load();
        final var mqttCfg = config.mqtt();
        final List<GenericSensor> sensors = new ArrayList<>();
        for (var s : config.sensors()) {
            sensors.add(new GenericSensor(
                    s.label(), s.min(), s.max(), s.step(), s.initialOrDefault(), s.integerOrDefault()
            ));
        }
        final var publisher = new MqttPublisher(mqttCfg.addr(), mqttCfg.clientId());
        while (true) {
            var map = new LinkedHashMap<String, Number>();
            for (var sensor : sensors) {
                map.put(sensor.label(), sensor.nextValue());
            }
            var report = new RoomReport(map);
            publisher.publish(report, mqttCfg.topic());
            try {
                sleep(mqttCfg.intervalMs() != null ? mqttCfg.intervalMs() : 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        new App().run();
    }
}

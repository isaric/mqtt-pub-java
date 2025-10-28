package com.path.variable.mqtt.pub.demo.sensor.report;

import java.util.Map;

public record RoomReport(Map<String, Number> readings) {
}

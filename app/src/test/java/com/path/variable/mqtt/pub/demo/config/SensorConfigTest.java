package com.path.variable.mqtt.pub.demo.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SensorConfigTest {

    @Test
    void defaultInitialIsMidpoint() {
        SensorConfig sc = new SensorConfig("temp", 10.0, 30.0, 1.0, null, null);
        assertEquals(20.0, sc.initialOrDefault());
    }

    @Test
    void defaultIntegerIsFalse() {
        SensorConfig sc = new SensorConfig("temp", 0.0, 1.0, 0.1, null, null);
        assertFalse(sc.integerOrDefault());
    }

    @Test
    void respectsProvidedDefaults() {
        SensorConfig sc = new SensorConfig("co2", 400, 1500, 50, 800.0, true);
        assertEquals(800.0, sc.initialOrDefault());
        assertTrue(sc.integerOrDefault());
    }
}

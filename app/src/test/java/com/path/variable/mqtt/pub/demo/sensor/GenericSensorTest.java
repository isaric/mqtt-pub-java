package com.path.variable.mqtt.pub.demo.sensor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GenericSensorTest {

    @Test
    void staysWithinBoundsAndSteps() {
        GenericSensor s = new GenericSensor("temp", 10.0, 12.0, 0.5, 11.0, false);
        for (int i = 0; i < 100; i++) {
            Number v = s.nextValue();
            double d = v.doubleValue();
            assertTrue(d >= 10.0 && d <= 12.0, "value out of bounds: " + d);
        }
    }

    @Test
    void returnsIntegerWhenConfigured() {
        GenericSensor s = new GenericSensor("co2", 400, 410, 1, 405, true);
        for (int i = 0; i < 20; i++) {
            Number n = s.nextValue();
            double d = n.doubleValue();
            assertEquals(Math.rint(d), d, 0.0, "Expected an integer-valued number");
            assertTrue(d >= 400 && d <= 410);
        }
    }
}

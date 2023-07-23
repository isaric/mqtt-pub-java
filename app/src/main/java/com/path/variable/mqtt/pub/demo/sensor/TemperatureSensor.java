package com.path.variable.mqtt.pub.demo.sensor;

import java.util.Random;

public class TemperatureSensor {

    private static final Double MIN_TEMPERATURE = 15.00;

    private static final Double MAX_TEMPERATURE = 35.00;

    private static final Double STEP = 0.5;

    private Double current = 25.0;

    private Random random = new Random();

    public Double getTemperature() {
        var toss = random.nextInt(2);
        if (toss == 0) {
            current = Math.max(current - STEP, MIN_TEMPERATURE);
        } else {
            current = Math.min(current + STEP, MAX_TEMPERATURE);
        }
        return current;
    }
}

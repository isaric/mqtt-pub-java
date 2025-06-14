package com.path.variable.mqtt.pub.demo.sensor;

import java.util.Random;

public class Co2Sensor {

    private static final Integer MIN = 450;

    private static final Integer MAX = 1200;

    private static final Integer STEP = 50;

    private Integer current = 600;

    private final Random random = new Random();

    public Integer getCo2Level() {
        var toss = random.nextInt(2);
        if (toss == 0) {
            current = Math.max(current - STEP, MIN);
        } else {
            current = Math.min(current + STEP, MAX);
        }
        return current;
    }
}

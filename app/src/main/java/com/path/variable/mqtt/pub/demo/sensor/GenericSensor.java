package com.path.variable.mqtt.pub.demo.sensor;

import java.util.Random;

public class GenericSensor {
    private final String label;
    private final double min;
    private final double max;
    private final double step;
    private double current;
    private final boolean integer;
    private final Random random = new Random();

    public GenericSensor(String label, double min, double max, double step, double initial, boolean integer) {
        this.label = label;
        this.min = min;
        this.max = max;
        this.step = step;
        this.current = clamp(initial);
        this.integer = integer;
    }

    public String label() { return label; }

    public Number nextValue() {
        int toss = random.nextInt(2);
        if (toss == 0) {
            current = Math.max(current - step, min);
        } else {
            current = Math.min(current + step, max);
        }
        return integer ? (int) Math.round(current) : current;
    }

    private double clamp(double v) {
        return Math.max(min, Math.min(max, v));
    }
}

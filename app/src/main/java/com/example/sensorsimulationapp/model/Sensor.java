package com.example.sensorsimulationapp.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Sensor {
    //thread safety generation of integer id
    private static AtomicInteger nextId = new AtomicInteger();

    private int id;
    private int pulse;
    private int bloodSaturation;
    private int breathPerMinute;

    public Sensor() {
        this.id = nextId.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getBloodSaturation() {
        return bloodSaturation;
    }

    public void setBloodSaturation(int bloodSaturation) {
        this.bloodSaturation = bloodSaturation;
    }

    public int getBreathPerMinute() {
        return breathPerMinute;
    }

    public void setBreathPerMinute(int breathPerMinute) {
        this.breathPerMinute = breathPerMinute;
    }
}

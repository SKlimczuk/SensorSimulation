package com.example.sensorsimulationapp.model;

public class Sensor {
    private int id;
    private int pulse;
    private int bloodSaturation;

    public Sensor() {
    }

    public Sensor(int id,
                  int pulse,
                  int bloodSaturation) {
        this.id = id;
        this.pulse = pulse;
        this.bloodSaturation = bloodSaturation;
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
}

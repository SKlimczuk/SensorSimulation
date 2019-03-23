package com.example.sensorsimulationapp.logic.impl;

import com.example.sensorsimulationapp.logic.SensorActivity;

public class DefaultSensorActivity implements SensorActivity {

    @Override
    public int generatePulse() {
        return 0;
    }

    @Override
    public int generateBloodSaturation() {
        return 0;
    }
}

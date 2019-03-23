package com.example.sensorsimulationapp.logic.impl;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.model.PatientStatus;

import java.util.Random;

public class DefaultSensorActivity implements SensorActivity {

    @Override
    public int generatePulse(String patientStatus) {
        Random random = new Random();
        int pulse;

        //TODO: investigate what is the correct pulse value for specific patients status
        if (patientStatus.equalsIgnoreCase(PatientStatus.BLACK.toString())) {
            pulse = 0;
        } else if (patientStatus.equalsIgnoreCase(PatientStatus.RED.toString())) {
            pulse = 0;
        } else if (patientStatus.equalsIgnoreCase(PatientStatus.YELLOW.toString())) {
            pulse = 0;
        } else if (patientStatus.equalsIgnoreCase(PatientStatus.GREEN.toString())) {
            pulse = 0;
        } else {
            pulse = 66;
        }

        return pulse;
    }

    @Override
    public int generateBloodSaturation(String patientStatus) {
        //TODO: investigate what is the correct saturation level value for specific patients status
        Random random = new Random();
        int saturationLevel = random.nextInt(100) + 1;
        return saturationLevel;
    }
}

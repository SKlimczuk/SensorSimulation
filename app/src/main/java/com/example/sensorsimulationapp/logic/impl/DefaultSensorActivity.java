package com.example.sensorsimulationapp.logic.impl;

import android.annotation.TargetApi;
import android.os.Build;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.model.PatientStatus;
import com.example.sensorsimulationapp.model.Sensor;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class DefaultSensorActivity implements SensorActivity {

    @Override
    public int generatePulse(PatientStatus patientStatus) {
        Random random = new Random();
        int pulse;

        //TODO: investigate what is the correct pulse value for specific patients status
        if (patientStatus.equals(PatientStatus.BLACK)) {
            pulse = 1;
        } else if (patientStatus.equals(PatientStatus.RED)) {
            pulse = 2;
        } else if (patientStatus.equals(PatientStatus.YELLOW)) {
            pulse = 3;
        } else if (patientStatus.equals(PatientStatus.GREEN)) {
            pulse = 4;
        } else {
            pulse = random.nextInt(100) + 4;
        }

        return pulse;
    }

    @Override
    public int generateBloodSaturation(PatientStatus patientStatus) {
        //TODO: investigate what is the correct saturation level value for specific patients status
        Random random = new Random();
        int saturationLevel = random.nextInt(100) + 1;
        return saturationLevel;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public PatientStatus stringToEnumConverter(String toConvert) {
        Optional<PatientStatus> patientStatus = Stream.of(PatientStatus.values())
                .filter(p -> p.toString().equalsIgnoreCase(toConvert))
                .findFirst();

        return patientStatus.get();
    }


    //TODO: not sure if this method should be here or in activity class ???
    @Override
    public void lifeLineSimulation(Sensor sensor, PatientStatus patientStatus, int seconds) throws InterruptedException {
        for (int i = 0; i < seconds; i++) {
            sensor.setPulse(generatePulse(patientStatus));
            sensor.setBloodSaturation(generateBloodSaturation(patientStatus));
        }

    }
}

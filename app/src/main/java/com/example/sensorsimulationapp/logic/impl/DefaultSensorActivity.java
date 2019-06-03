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
        int pulse = 0;

        if (patientStatus.equals(PatientStatus.BLACK))
            pulse = 0;
        else if (patientStatus.equals(PatientStatus.RED))
            pulse = random.nextInt(40) + 5;
        else if (patientStatus.equals(PatientStatus.YELLOW) || patientStatus.equals(PatientStatus.GREEN))
            pulse = random.nextInt(31) + 60;

        return pulse;
    }

    @Override
    public int generateBloodSaturation(PatientStatus patientStatus) {
        Random random = new Random();
        int saturation = 0;

        if (patientStatus.equals(PatientStatus.BLACK))
            saturation = 0;
        else if (patientStatus.equals(PatientStatus.RED))
            saturation = random.nextInt(30) + 1;
        else if (patientStatus.equals(PatientStatus.YELLOW))
            saturation = random.nextInt(60) + 30;
        else if (patientStatus.equals(PatientStatus.GREEN))
            saturation = random.nextInt(5) + 90;

        return saturation;
    }

    @Override
    public int generateBreathe(PatientStatus patientStatus) {
        Random random = new Random();
        int breathe = 0;

        if (patientStatus.equals(PatientStatus.BLACK))
            breathe = 0;
        else if (patientStatus.equals(PatientStatus.RED))
            breathe = random.nextInt(20) + 1;
        else if (patientStatus.equals(PatientStatus.YELLOW) || patientStatus.equals(PatientStatus.GREEN))
            breathe = random.nextInt(31) + 30;

        return breathe;
    }


    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public PatientStatus stringToEnumConverter(String toConvert) {
        Optional<PatientStatus> patientStatus = Stream.of(PatientStatus.values())
                .filter(p -> p.toString().equalsIgnoreCase(toConvert))
                .findFirst();

        return patientStatus.get();
    }

    @Override
    public void lifeLineSimulation(Sensor sensor, PatientStatus patientStatus) {
        sensor.setPulse(generatePulse(patientStatus));
        sensor.setBloodSaturation(generateBloodSaturation(patientStatus));
        sensor.setBreathPerMinute(generateBreathe(patientStatus));
    }

    @Override
    public String customAdvertisingPacketGenerator(Sensor sensor) {
        return sensor.getId() + "," +
                sensor.getPulse() + "," +
                sensor.getBloodSaturation() + "," +
                sensor.getBreathPerMinute();
    }
}

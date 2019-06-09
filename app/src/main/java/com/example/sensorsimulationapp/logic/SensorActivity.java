package com.example.sensorsimulationapp.logic;

import com.example.sensorsimulationapp.model.PatientStatus;
import com.example.sensorsimulationapp.model.Sensor;

public interface SensorActivity {

    /**
     * @param patientStatus specify current status of patient
     * @return generated pulse depending on patients status
     */
    int generatePulse(PatientStatus patientStatus);

    /**
     * @param patientStatus specify current status of patient
     * @return generated saturation level (%) depending on patients status
     */
    int generateBloodSaturation(PatientStatus patientStatus);

    /**
     * @param patientStatus specify current status of patient
     * @return generated breathe per minute depending on patients status
     */
    int generateBreathe(PatientStatus patientStatus);

    /**
     * @param toConvert String value to convert
     * @return enum value
     */
    PatientStatus stringToEnumConverter(String toConvert) throws Throwable;

    /**
     * @param sensor        to store data from simulation
     * @param patientStatus status
     */
    void lifeLineSimulation(Sensor sensor, PatientStatus patientStatus);

    /**
     * @param sensor to retrieve data
     * @return data as string
     */
    byte[] customAdvertisingPacketGenerator(Sensor sensor);
}

package com.example.sensorsimulationapp.logic;

import com.example.sensorsimulationapp.model.PatientStatus;

public interface SensorActivity {

    /**
     *
     * @param patientStatus specify current status of patient
     * @return generated pulse depending on patients status
     */
    int generatePulse(String patientStatus);

    /**
     *
     * @param patientStatus specify current status of patient
     * @return generated saturation level (%) depending on patients status
     */
    int generateBloodSaturation(String patientStatus);
}

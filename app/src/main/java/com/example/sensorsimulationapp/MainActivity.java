package com.example.sensorsimulationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.logic.impl.DefaultSensorActivity;

public class MainActivity extends AppCompatActivity {
    SensorActivity sensorActivity = new DefaultSensorActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void beginSimulation(View view) {

        //TODO: how to get values from activity
        int saturationLevel = sensorActivity.generateBloodSaturation("random");
        int pulse = sensorActivity.generateBloodSaturation("random");
    }
}

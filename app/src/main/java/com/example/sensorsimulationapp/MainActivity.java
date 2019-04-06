package com.example.sensorsimulationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.logic.impl.DefaultSensorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void beginSimulation(View view) {
        final Intent chosenPatientStatus = new Intent(this, SimulationResultActivity.class);
        Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpinner);

        statusSpinner.setVisibility(View.VISIBLE);
        String patientStatus = statusSpinner.getSelectedItem().toString();
        chosenPatientStatus.putExtra("patientStatus", patientStatus);

        startActivity(chosenPatientStatus);
    }
}

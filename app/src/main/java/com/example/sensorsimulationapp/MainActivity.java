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
        final Intent intent = new Intent(this, SimulationResultActivity.class);
        Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpinner);

        //TODO: not necessary now, geting selected value from spinner
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String patientStatus = parent.getItemAtPosition(position).toString();
                intent.putExtra("patientStatus", patientStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startActivity(intent);
    }
}

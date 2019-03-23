package com.example.sensorsimulationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.logic.impl.DefaultSensorActivity;
import com.example.sensorsimulationapp.model.Sensor;

public class SimulationResultActivity extends AppCompatActivity {

    private final SensorActivity sensorActivity = new DefaultSensorActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_result);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: think about type of intent extras: String or ENUM ???
        Bundle bundle = getIntent().getExtras();

        Sensor sensor = new Sensor();

        sensor.setBloodSaturation(sensorActivity.generateBloodSaturation("random"));
        sensor.setPulse(sensorActivity.generatePulse("random"));

        Toast toast = Toast.makeText(this,
                "Pulse : " + sensor.getPulse() + "\nSaturation lvl : " + sensor.getBloodSaturation(),
                Toast.LENGTH_SHORT);
        toast.show();
    }
}

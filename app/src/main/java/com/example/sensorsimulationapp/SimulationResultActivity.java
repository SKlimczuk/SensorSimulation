package com.example.sensorsimulationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.logic.impl.DefaultSensorActivity;
import com.example.sensorsimulationapp.model.PatientStatus;
import com.example.sensorsimulationapp.model.Sensor;

public class SimulationResultActivity extends AppCompatActivity {

    private final SensorActivity sensorActivity = new DefaultSensorActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_result);

        Sensor sensor = new Sensor();

        EditText result = findViewById(R.id.printResult);
        ImageView statusImage = findViewById(R.id.statusView);

        try {
            Bundle status = getIntent().getExtras();


            //todo: find the correct place to this code
            PatientStatus patientStatus = sensorActivity.stringToEnumConverter(status.getString("patientStatus"));
            setImageDependsOnPatientStatus(patientStatus, statusImage);
            sensorActivity.lifeLineSimulation(sensor, patientStatus, 1000);
            result.setText(generateOutputMessage(sensor.getPulse(), sensor.getBloodSaturation()));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private String generateOutputMessage(int pulse, int saturation) {
        return "PULSE: " + pulse + "\nSATURATION: " + saturation;
    }

    private void setImageDependsOnPatientStatus(PatientStatus patientStatus, ImageView imageView) {
        if (patientStatus.equals(PatientStatus.BLACK)) {
            imageView.setImageResource(R.drawable.black);
        } else if (patientStatus.equals(PatientStatus.RED)) {
            imageView.setImageResource(R.drawable.red);
        } else if (patientStatus.equals(PatientStatus.YELLOW)) {
            imageView.setImageResource(R.drawable.yellow);
        } else if (patientStatus.equals(PatientStatus.GREEN)) {
            imageView.setImageResource(R.drawable.green);
        } else {
            imageView.setImageResource(R.drawable.random);
        }
    }
}

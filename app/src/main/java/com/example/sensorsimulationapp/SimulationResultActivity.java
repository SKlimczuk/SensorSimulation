package com.example.sensorsimulationapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.logic.impl.DefaultSensorActivity;
import com.example.sensorsimulationapp.model.PatientStatus;
import com.example.sensorsimulationapp.model.Sensor;

import java.util.UUID;

public class SimulationResultActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mAdvertiseButton;
    private Button colorButton;

    private final SensorActivity sensorActivity = new DefaultSensorActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_result);

        Sensor sensor = new Sensor();

//        EditText result = findViewById(R.id.printResult);
        colorButton = findViewById(R.id.colourCircle);
        mAdvertiseButton = findViewById(R.id.broadcastButton);
        mAdvertiseButton.setOnClickListener(this);

        try {
            Bundle status = getIntent().getExtras();

            PatientStatus patientStatus = sensorActivity.stringToEnumConverter(status.getString("patientStatus"));
            setColorAsPatientStatus(patientStatus);
//            sensorActivity.lifeLineSimulation(sensor, patientStatus, 1000);
//            result.setText(generateOutputMessage(sensor.getPulse(), sensor.getBloodSaturation()));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private String generateOutputMessage(int pulse, int saturation) {
        return "PULSE: " + pulse + "\nSATURATION: " + saturation;
    }

    private void setColorAsPatientStatus(PatientStatus patientStatus) {
        if (patientStatus.equals(PatientStatus.BLACK)) {
            colorButton.getBackground()
                    .setColorFilter(new LightingColorFilter(Color.TRANSPARENT, Color.BLACK));
        } else if (patientStatus.equals(PatientStatus.RED)) {
            colorButton.getBackground()
                    .setColorFilter(new LightingColorFilter(Color.TRANSPARENT, Color.RED));
        } else if (patientStatus.equals(PatientStatus.YELLOW)) {
            colorButton.getBackground()
                    .setColorFilter(new LightingColorFilter(Color.TRANSPARENT, Color.YELLOW));
        } else if (patientStatus.equals(PatientStatus.GREEN)) {
            colorButton.getBackground()
                    .setColorFilter(new LightingColorFilter(Color.TRANSPARENT, Color.GREEN));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mAdvertiseButton.getId())
            advertise();
    }

    private void advertise() {
        BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(false)
                .build();

        ParcelUuid pUuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)));

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addServiceUuid(pUuid)
                .build();

        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("BLE", "Advertising onStartFailure: " + errorCode);
                super.onStartFailure(errorCode);
            }
        };

        advertiser.startAdvertising(settings, data, advertisingCallback);
    }


}

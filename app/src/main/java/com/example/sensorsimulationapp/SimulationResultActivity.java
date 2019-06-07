package com.example.sensorsimulationapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.logic.impl.DefaultSensorActivity;
import com.example.sensorsimulationapp.model.PatientStatus;
import com.example.sensorsimulationapp.model.Sensor;

import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SimulationResultActivity extends AppCompatActivity implements View.OnClickListener/*, LocationListener*/ {

    private Sensor sensor = new Sensor();
    private final SensorActivity sensorActivity = new DefaultSensorActivity();

    private Button colorButton;
    private Button mAdvertiseButton;
    private Button mStopAdvertiseButton;
    private TextView bloodText;
    private TextView lungText;
    private TextView heartText;

    private BluetoothLeAdvertiser advertiser;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_result);

        colorButton = findViewById(R.id.colourCircle);
        mAdvertiseButton = findViewById(R.id.broadcastButton);
        mStopAdvertiseButton = findViewById(R.id.stopBroadcastButton);
        bloodText = findViewById(R.id.bloodText);
        lungText = findViewById(R.id.lungText);
        heartText = findViewById(R.id.heartText);

        mAdvertiseButton.setOnClickListener(this);
        mStopAdvertiseButton.setOnClickListener(this);

        advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        Runnable myRunnableThread = new CountDownRunner();
        Thread myThread = new Thread(myRunnableThread);
        myThread.start();

        checkIfGpsIsEnabled();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mAdvertiseButton.getId())
            advertise();
        if (v.getId() == mStopAdvertiseButton.getId())
            stopAdvertise();
    }

    private void advertise() {
        signalingOfBroadcastStateChanged(true);

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setConnectable(false)
                .build();


        String advData = "t";
//        String advData = sensorActivity.customAdvertisingPacketGenerator(sensor);

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(false)
                .addManufacturerData(1, advData.getBytes(Charset.forName("UTF-8")))
                .build();

        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.i("BLE", "started advertising with data:" + advData);
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("BLE", "Advertising onStartFailure: " + errorCode);
                super.onStartFailure(errorCode);
            }
        };

        advertiser.startAdvertising(settings, advertiseData, advertisingCallback);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        advertiser.stopAdvertising(advertisingCallback);
    }

    private void stopAdvertise() {
        signalingOfBroadcastStateChanged(false);
    }

    public void doWork() {
        runOnUiThread(() -> {
            try {
                printMeasurementResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    class CountDownRunner implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void printMeasurementResult() {
        try {
            Bundle status = getIntent().getExtras();

            PatientStatus patientStatus = sensorActivity.stringToEnumConverter(status.getString("patientStatus"));
            setColorAsPatientStatus(patientStatus);
            sensorActivity.lifeLineSimulation(sensor, patientStatus);
            setSimulatedPatientId(sensor);
            bloodText.setText("" + sensor.getBloodSaturation());
            heartText.setText("" + sensor.getPulse());
            lungText.setText("" + sensor.getBreathPerMinute());

        } catch (Throwable e) {
            e.printStackTrace();
        }
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

    private void setSimulatedPatientId(Sensor sensor) {
        try {
            colorButton.setText("" + sensor.getId());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void signalingOfBroadcastStateChanged(boolean state) {
        if (state)
            mAdvertiseButton.getBackground()
                    .setColorFilter(new LightingColorFilter(Color.TRANSPARENT, Color.BLUE));
        else
            mAdvertiseButton.getBackground()
                    .setColorFilter(new LightingColorFilter(Color.TRANSPARENT, Color.BLACK));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkIfGpsIsEnabled() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
        }
    }
}

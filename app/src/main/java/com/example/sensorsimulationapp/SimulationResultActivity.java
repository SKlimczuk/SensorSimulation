package com.example.sensorsimulationapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sensorsimulationapp.logic.SensorActivity;
import com.example.sensorsimulationapp.logic.impl.DefaultSensorActivity;
import com.example.sensorsimulationapp.model.PatientStatus;
import com.example.sensorsimulationapp.model.Sensor;

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

        byte[] advData = sensorActivity.customAdvertisingPacketGenerator(sensor);

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(false)
                .addManufacturerData(1, advData)
                .build();

        AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.i("BLE", "started advertising with data:" + new String(advData));
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("BLE", "Advertising onStartFailure: " + errorCode);
                super.onStartFailure(errorCode);
            }
        };

        advertiser.startAdvertising(settings, advertiseData, advertiseCallback);
    }

    private void stopAdvertise() {
        AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
            }
        };

        advertiser.stopAdvertising(advertiseCallback);
        signalingOfBroadcastStateChanged(false);
    }

    class CountDownRunner implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    periodicMeasurementResultSchedule();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void periodicMeasurementResultSchedule() {
        runOnUiThread(() -> {
            try {
                printMeasurementResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
}

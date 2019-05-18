package com.example.sensorsimulationapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sensorsimulationapp.bluetooth.BluetoothConstants;

public class MainActivity extends AppCompatActivity {

    public BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfBleIsSupported();
        checkIfBtIsEnabled();
    }

    public void beginSimulation(View view) {
        final Intent chosenPatientStatus = new Intent(this, SimulationResultActivity.class);
        Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpinner);

        statusSpinner.setVisibility(View.VISIBLE);
        String patientStatus = statusSpinner.getSelectedItem().toString();
        chosenPatientStatus.putExtra("patientStatus", patientStatus);

        startActivity(chosenPatientStatus);
    }

    private void checkIfBleIsSupported() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble not supported", Toast.LENGTH_LONG).show();
        }
    }

    private void checkIfBtIsEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BluetoothConstants.REQUEST_ENABLE_BT);
        }
    }
}

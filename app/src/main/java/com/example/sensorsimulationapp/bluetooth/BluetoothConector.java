package com.example.sensorsimulationapp.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothConector extends Thread {
    private Handler handler;

    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public BluetoothConector(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
        InputStream tmpInput = null;
        OutputStream tmpOutput = null;

        try {
            tmpInput = bluetoothSocket.getInputStream();
            tmpOutput = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = tmpInput;
        outputStream = tmpOutput;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                bytes = inputStream.read(buffer);
                handler = new Handler();
                Message readMessage = handler.obtainMessage(MessagesConstants.MESSAGE_READ, bytes, -1, buffer);
                readMessage.sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

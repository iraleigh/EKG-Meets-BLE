package com.android.sensorgraph;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.app.Service;


import java.util.ArrayList;
import java.util.Set;

/**
 * Created by iainchf on 1/31/16.
 */
public class ConnectingBLEHealthDevice {

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private boolean mScanning;
    private Handler mHandler;

    //limits scanning to 10 seconds
    private static final long  SCAN_PERIOD = 10000;

    private ArrayList<BluetoothDevice> mLeDevices;


    private String mNameOfDevice;
    private BluetoothGatt mConnectedDevice;
    private final String TAG = "ConnectingBLE";

    public ConnectingBLEHealthDevice(String nameOfDevive){
        this.mNameOfDevice = nameOfDevive;
    }

    public void ConnectToDevice(Context context) {
        mLeDevices = new ArrayList<BluetoothDevice>();
        mHandler = new Handler();


        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {

        }

        //scanLeDevice(mBluetoothAdapter.isEnabled());
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (!pairedDevices.isEmpty()) {
            int i = 0;
            for (BluetoothDevice bd : pairedDevices) {
                if(bd.getAddress().equals(mNameOfDevice)){
                    mConnectedDevice = bd.connectGatt(context, false, mGattCallback);
                }

            }

        }
        //D8:D9:84:55:05:35

    }

    public BluetoothGatt getGattDevice(){
        return mConnectedDevice;
    }

    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        Log.i(TAG, "Connected to GATT server.");
                        Log.i(TAG, "Attempting to start service discovery:" +
                                mConnectedDevice.discoverServices());

                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        //mConnectionState = STATE_DISCONNECTED;
                        Log.i(TAG, "Disconnected from GATT server.");

                    }
                }

                @Override
                // New services discovered
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {

                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @Override
                // Result of a characteristic read operation
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                }
            };
}

package de.ohg.fitag.android.discoveryVehicleRemote;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by Calvin on 24.04.2015.
 */
public class Tools {

    public static boolean enableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled())
            return false;
        mBluetoothAdapter.enable();
        return true;
    }

    public static boolean disableBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled())
            return false;
        mBluetoothAdapter.disable();
        return true;
    }
}

package de.ohg.fitag.android.discoveryVehicleRemote;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.os.PowerManager;
import android.widget.Toast;

/**
 * Created by Calvin on 06.04.2015.
 */
public class LejosBackgroundService extends Service{

    public static final String BROADCAST_ACTION = "de.ohg.fitag.android.discoveryVehicleRemote.service";
    private static final String TAG = "LejosService";
    private Intent updateIntent;
    private LejosBackgroundTask backgroundTask;
    private SensorManager sensorManager;
    private WakeLock wakelock;

    private Sensor magnetometer;
    private Sensor accelerator;

    private boolean running;
    private ConnectionState connection;

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

    public void sendStateUpdate() {
        updateIntent.putExtra("running", running);
        updateIntent.putExtra("connection", connection);
        sendBroadcast(updateIntent);
    }

    public void setRunningVariable(boolean running){
        this.running = running;
    }

    public void setConnectionVariable(ConnectionState connectionState){
        this.connection = connectionState;
    }

    @Override
    public void onCreate() {
        registerReceiver(receiver, new IntentFilter(MainActivity.BROADCAST_ACTION));
        updateIntent = new Intent(BROADCAST_ACTION);
        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LejosBackgroundService");

        accelerator = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connection = ConnectionState.PENDING;
        wakelock.acquire();
        running = true;
        sendStateUpdate();
        if (enableBluetooth())
            Toast.makeText(this, "Bluetooth aktiviert", Toast.LENGTH_SHORT).show();
        backgroundTask = new LejosBackgroundTask(this);
        backgroundTask.start();
        sensorManager.registerListener(backgroundTask, accelerator, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(backgroundTask, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //dont provide
        return null;
    }

    @Override
    public void onDestroy() {
        backgroundTask.initiateConnectionClosed();
        sensorManager.unregisterListener(backgroundTask);
        wakelock.release();
        backgroundTask.interrupt();
        //dont disable bluetooth on connection fail, so it is activated for next try
        if(connection != ConnectionState.FAILED)
            disableBluetooth();
        setRunningVariable(false);
        sendStateUpdate();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendStateUpdate();
        }
    };
}

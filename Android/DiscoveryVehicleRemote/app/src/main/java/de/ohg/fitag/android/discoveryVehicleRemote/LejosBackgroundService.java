package de.ohg.fitag.android.discoveryVehicleRemote;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 06.04.2015.
 */
public class LejosBackgroundService extends Service{

    private static final String TAG = "LejosService";
    private LejosBackgroundTask backgroundTask;
    private SensorManager sensorManager;
    private PowerManager powerManager;
    private WakeLock wakelock;

    private Sensor magnetometer;
    private Sensor accelerator;

    private static boolean running;
    private final IBinder binder = new LocalBinder();

    private ConnectionState connection;

    private List<LejosServiceObserver> observers;

    public void setConnectionState(ConnectionState connectionState){
        this.connection = connectionState;

        for(LejosServiceObserver observer : observers)
            observer.onConnectionChange(connectionState);
    }

    public void foundWater(float depth){
        for(LejosServiceObserver observer : observers)
            observer.onWaterFound(depth);
    }

    @Override
    public void onCreate() {
        running = true;

        observers = new ArrayList<LejosServiceObserver>();

        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LejosBackgroundService");

        accelerator = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        closeConnection();
        running = false;
    }

    public void registerObserver(LejosServiceObserver observer){
        observers.add(observer);
    }

    public void closeConnection(){
        if(backgroundTask == null)
            return;
        sensorManager.unregisterListener(backgroundTask);
        backgroundTask.initiateConnectionClosed();
        interruptBackgroundTask();

        if(connection != ConnectionState.FAILED)
            Tools.disableBluetooth();

        wakelock.release();
    }

    public void interruptBackgroundTask(){
        backgroundTask.interrupt();
        backgroundTask = null;
    }

    public void openConnection(){
        wakelock.acquire();

        if(backgroundTask != null) {
            Log.d(TAG, "BackroundTask already running - cannot etablish new connection");
            return;
        }
        if (Tools.enableBluetooth())
            Toast.makeText(this, "Bluetooth aktiviert", Toast.LENGTH_SHORT).show();

        setConnectionState(ConnectionState.PENDING);
        backgroundTask = new LejosBackgroundTask(this);
        backgroundTask.start();

        sensorManager.registerListener(backgroundTask, accelerator, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(backgroundTask, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public class LocalBinder extends Binder {

        LejosBackgroundService getService() {
            return LejosBackgroundService.this;
        }

    }

    public interface LejosServiceObserver{

        public void onConnectionChange(ConnectionState connectionState);

        public void onWaterFound(float depth);

    }
}

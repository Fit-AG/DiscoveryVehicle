package de.ohg.fitag.android.discoveryVehicleRemote;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

import de.ohg.landau.lejos.communication.BluetoothCommunicationManager;
import de.ohg.landau.lejos.communication.CommunicationManager;
import de.ohg.landau.lejos.communication.DataMessage;
import de.ohg.landau.lejos.communication.ErrorMessage;
import de.ohg.landau.lejos.communication.Message;
import de.ohg.landau.lejos.communication.MessageObserver;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

/**
 * Created by Calvin on 06.04.2015.
 */
public class LejosBackgroundTask extends Thread implements MessageObserver, SensorEventListener {

    private CommunicationManager communicationManager;
    private LejosBackgroundService service;

    private static final String TAG = "LejosServiceTask";

    public LejosBackgroundTask(LejosBackgroundService service){
        this.service = service;
    }

    public void run(){
        NXTConnector nxtConnector = connect(CONN_TYPE.LEJOS_PACKET);
        if (nxtConnector == null) {
            service.setConnectionVariable(ConnectionState.FAILED);
            service.sendStateUpdate();
            service.stopSelf();
            return;
        }
        service.setConnectionVariable(ConnectionState.CONNECTED);
        service.sendStateUpdate();

        InputStream inputStream = nxtConnector.getInputStream();
        OutputStream outputStream = nxtConnector.getOutputStream();

        communicationManager = new BluetoothCommunicationManager(inputStream, outputStream);
        communicationManager.registerObserver(this);
        Message message = DataMessage.build().append("hand","shake");
        communicationManager.sendMessage(message);
        while(true){
            float full_new_rotation = new_rotation + 180;
            float difference = Math.round(calculateRotationChange(rotation, full_new_rotation));
            if(Math.abs(difference) > tolerance) {
                sendUpdateMessage();
                Log.d(TAG, "rotation: "+Math.round(rotation)+" --> "+Math.round(full_new_rotation)+": "+difference);
            }
            rotation = full_new_rotation;
            try {
                Thread.sleep(750); //Should not block!, too small?
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //stopSelf();
    }

    public float calculateRotationChange(float old_rotation, float new_rotation){
        float difference = new_rotation - old_rotation;
        if(difference > 180) {
            difference =  -(360-difference);
        }else if(difference < -180) {
            difference = 360+difference;
        }
        return difference;
    }

    @Override
    public void onReceive(Message message) {
        Log.d(TAG,"Message received! "+message.parse());
        String placeholder = "placeholder";
        String content = ((DataMessage)message).getString("error", placeholder);
        if(content.equals(ErrorMessage.CONNECTION_CLOSED)) {
            service.setConnectionVariable(ConnectionState.ABORTED);
            service.sendStateUpdate();
            service.stopSelf();
        }
    }

    public void sendUpdateMessage() {
        DataMessage message = DataMessage.build().append("degree", Math.round(new_rotation));
        communicationManager.sendMessage(message);
    }

    private float[] gravity;
    private float[] magnetic;
    private float azimuth;
    private float rotation;
    private float new_rotation;
    private final int tolerance = 7;

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetic = event.values;
        }
        if(gravity == null || magnetic == null)
            return;
        float R[] = new float[9];
        float I[] = new float[9];
        boolean success = SensorManager.getRotationMatrix(R, I, gravity, magnetic);
        if(!success)
            return;
        float orientation[] = new float[3];
        SensorManager.getOrientation(R, orientation);
        azimuth = orientation[0];
        new_rotation = (float)Math.toDegrees(azimuth);
//        Log.d(TAG, "old: "+(-azimuth * 360 / (2 * (float)Math.PI) + " new: "+azimuth*180 / (float)Math.PI % 360)+" newer: "+new_rotation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not needed
    }

    public static enum CONN_TYPE {
        LEJOS_PACKET, LEGO_LCP
    }

    private NXTConnector connect(CONN_TYPE connection_type) {
        Log.d(TAG, " about to add LEJOS listener ");

        NXTConnector conn = new NXTConnector();
        conn.setDebug(true);
        conn.addLogListener(new NXTCommLogListener() {
            public void logEvent(String arg0) {
                Log.e(TAG + "-NXJ:", arg0);
            }

            public void logEvent(Throwable arg0) {
                Log.e(TAG + "-NXJ:", arg0.getMessage(), arg0);
            }
        });
        boolean b = false;
        switch (connection_type) {
            case LEGO_LCP:
                b = conn.connectTo("btspp://NXT", NXTComm.LCP);
                break;
            case LEJOS_PACKET:
                b = conn.connectTo("btspp://");
                break;
        }
        if(!b)
            return null;
        return conn;
    }

}
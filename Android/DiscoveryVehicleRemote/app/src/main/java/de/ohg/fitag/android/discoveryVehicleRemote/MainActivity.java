package de.ohg.fitag.android.discoveryVehicleRemote;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity implements SensorEventListener {
    private ImageView image;

    private TextView tvHeading;
    private TextView tvConnection;
    private ProgressBar pgSpinner;
    private Button btAction;

    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    private ConnectionState connectionState;

    private Intent updateIntent;
    public static final String BROADCAST_ACTION = "de.ohg.fitag.android.discoveryVehicleRemote.activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateIntent = new Intent(BROADCAST_ACTION);

        image = (ImageView) findViewById(R.id.ivCompass);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        tvConnection = (TextView) findViewById(R.id.tvConnectionState);
        pgSpinner = (ProgressBar) findViewById(R.id.pgSpinner);
        btAction = (Button) findViewById(R.id.btAction);
        connectionState = ConnectionState.DISCONNECTED;
        setupNXJCache();

        final Button button = (Button) findViewById(R.id.btAction);
        final Intent serviceIntent = new Intent(this, LejosBackgroundService.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                switch(connectionState){
                    case CONNECTED:
                        stopService(serviceIntent);
                        break;
                    default:
                        startService(serviceIntent);
                }
            }
        });

    }

    private final static String TAG = "LeJOSDroid";

    private BroadcastReceiver serviceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    public void updateUI(Intent intent){
        boolean running = intent.getBooleanExtra("running", true);
        connectionState = (ConnectionState) intent.getSerializableExtra("connection");
        if(running)
            pgSpinner.setVisibility(View.VISIBLE);
        else
            pgSpinner.setVisibility(View.INVISIBLE);
        switch(connectionState){
            case CONNECTED:
                btAction.setEnabled(true);
                btAction.setBackgroundColor(getResources().getColor(R.color.danger));
                btAction.setText(getResources().getText(R.string.btAction_disconnect));
                break;
            case PENDING:
                btAction.setEnabled(false);
                break;
            default:
                btAction.setEnabled(true);
                btAction.setBackgroundColor(getResources().getColor(R.color.normal));
                btAction.setText(getResources().getText(R.string.btAction_connect));
        }
        tvConnection.setTextColor(connectionState.color());
        tvConnection.setText(getResources().getText(connectionState.value()));
    }

    public void requestUpdate(){
        Log.d(TAG,"request update");
        sendBroadcast(updateIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestUpdate();
        registerReceiver(serviceBroadcastReceiver, new IntentFilter(LejosBackgroundService.BROADCAST_ACTION));
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(serviceBroadcastReceiver);
        mSensorManager.unregisterListener(this);
    }

    private void setupNXJCache() {

        File root = Environment.getExternalStorageDirectory();

        try {
            String androidCacheFile = "nxj.cache";
            File mLeJOS_dir = new File(root + "/leJOS");
            if (!mLeJOS_dir.exists()) {
                mLeJOS_dir.mkdir();

            }
            File mCacheFile = new File(root + "/leJOS/", androidCacheFile);

            if (root.canWrite() && !mCacheFile.exists()) {
                FileWriter gpxwriter = new FileWriter(mCacheFile);
                BufferedWriter out = new BufferedWriter(gpxwriter);
                out.write("");
                out.flush();
                out.close();
                Context context = getApplicationContext();
                CharSequence text = "nxj.cache (record of connection addresses) written to: " + mCacheFile.getName();
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not write nxj.cache " + e.getMessage(), e);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        // how long the animation will take place
        ra.setDuration(210);
        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
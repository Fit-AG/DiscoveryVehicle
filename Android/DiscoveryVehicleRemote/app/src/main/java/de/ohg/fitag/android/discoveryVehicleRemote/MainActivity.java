package de.ohg.fitag.android.discoveryVehicleRemote;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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

import android.content.ServiceConnection;

public class MainActivity extends Activity implements SensorEventListener {
    private ImageView image;

    private TextView tvHeading;
    private TextView tvConnection;
    private ProgressBar pgSpinner;
    private Button btAction;

    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    private ConnectionState connectionState;

    private boolean bound;
    private LejosBackgroundService backgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.ivCompass);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        tvConnection = (TextView) findViewById(R.id.tvConnectionState);
        pgSpinner = (ProgressBar) findViewById(R.id.pgSpinner);
        btAction = (Button) findViewById(R.id.btAction);
        connectionState = ConnectionState.DISCONNECTED;
        setupNXJCache();

        final Button button = (Button) findViewById(R.id.btAction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(!bound)
                    return;
                switch(connectionState){
                    case CONNECTED:
                        backgroundService.closeConnection();
                        break;
                    default:
                        backgroundService.openConnection();
                }
            }
        });
    }

    private final static String TAG = "DiscoveryVehicle";

    private LejosBackgroundService.LejosServiceObserver observer = new LejosBackgroundService.LejosServiceObserver(){

        @Override
        public void onConnectionChange(ConnectionState newConnectionState) {
            connectionState = newConnectionState;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tvConnection.setTextColor(connectionState.color());
                    tvConnection.setText(getResources().getText(connectionState.value()));
                }
            });
        }

        @Override
        public void onWaterFound(float depth) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        final Intent serviceIntent = new Intent(this, LejosBackgroundService.class);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop(){
        super.onStop();

        if(bound)
            unbindService(connection);
        bound = false;
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

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            LejosBackgroundService.LocalBinder binder = (LejosBackgroundService.LocalBinder) service;;
            backgroundService = binder.getService();
            backgroundService.registerObserver(observer);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}
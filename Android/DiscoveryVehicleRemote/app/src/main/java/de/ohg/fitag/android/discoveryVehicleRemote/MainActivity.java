package de.ohg.fitag.android.discoveryVehicleRemote;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.ServiceConnection;

public class MainActivity extends Activity {

    private TextView tvConnection;
    private Button btAction;

    private TextView tvWaterDepth;
    private ImageView ivWaterFound;

    private ConnectionState connectionState;

    private boolean bound;
    private LejosBackgroundService backgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvConnection = (TextView) findViewById(R.id.tvConnectionState);
        btAction = (Button) findViewById(R.id.btAction);

        tvWaterDepth = (TextView) findViewById(R.id.tvWaterDepth);
        ivWaterFound = (ImageView) findViewById(R.id.ivWaterFound);

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
                        foundWater(-1);
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
                    tvConnection.setTextColor(getResources().getColor(connectionState.color()));
                    tvConnection.setText(getResources().getText(connectionState.value()));

                    switch(connectionState) {
                        case CONNECTED:
                            btAction.setEnabled(true);
                            btAction.setAlpha(1);
                            btAction.setBackgroundColor(getResources().getColor(R.color.danger));
                            btAction.setText(getResources().getText(R.string.btActionDisconnect));
                            break;
                        case PENDING:
                            btAction.setEnabled(false);
                            btAction.setAlpha(Float.parseFloat(getResources().getString(R.string.disabledOpacity)));
                            break;
                        default:
                            btAction.setEnabled(true);
                            btAction.setAlpha(1);
                            btAction.setBackgroundColor(getResources().getColor(R.color.primary));
                            btAction.setText(getResources().getText(R.string.btActionConnect));
                    }
                    foundWater(-1);
                }
            });
        }

        @Override
        public void onWaterFound(final float depth) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                   foundWater(depth);
                }
            });
        }
    };

    public void foundWater(float depth){
        if(depth == -1){
            tvWaterDepth.setEnabled(false);
            tvWaterDepth.setText(getResources().getText(R.string.tvWaterDepthNotFound));
            ivWaterFound.setImageDrawable(getResources().getDrawable(R.drawable.water_drop_inactive));
        }else{
            tvWaterDepth.setEnabled(true);
            tvWaterDepth.setText(String.format(getResources().getString(R.string.tvWaterDepthFound), depth));
            ivWaterFound.setImageDrawable(getResources().getDrawable(R.drawable.water_drop_active));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent serviceIntent = new Intent(this, LejosBackgroundService.class);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

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
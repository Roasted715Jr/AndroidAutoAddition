package io.github.roasted715jr.easycarapp;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityManager activityManager;
    private List<ActivityManager.RunningAppProcessInfo> runningApps;
    private BluetoothAdapter bluetoothAdapter;
    private Button startBtn, stopBtn;
    private CheckBox enableAirplaneModeCheck, enableWifiCheck;
    private Intent launchIntent;
    private RadioButton androidAutoRadio, samsungMusicRadio;
    private RadioGroup appRadioGroup;
    private SettingsManager settingsManager;
    private TelephonyManager telephonyManager;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init variables
        activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        startBtn = (Button) findViewById(R.id.btn_start);
        stopBtn = (Button) findViewById(R.id.btn_stop);
        //enableAirplaneModeCheck = (CheckBox) findViewById(R.id.check_enable_airplane_mode);
        enableWifiCheck = (CheckBox) findViewById(R.id.check_enable_wifi);
        launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.projection.gearhead");
        androidAutoRadio = (RadioButton) findViewById(R.id.btn_android_auto);
        samsungMusicRadio = (RadioButton) findViewById(R.id.btn_samsung_music);
        appRadioGroup = (RadioGroup) findViewById(R.id.radio_group_choose_app);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //Init the Settings Manager
        settingsManager = new SettingsManager(bluetoothAdapter, telephonyManager, wifiManager);

        //Set the button listeners
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop(enableWifiCheck.isChecked());
            }
        });
        appRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                changeApp(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int ACTION = getIntent().getIntExtra("ACTION", 2);
            Log.i(TAG, String.valueOf(ACTION));
            if (ACTION == 1) {
                start();
            } else if (ACTION == 0) {
                stop(enableWifiCheck.isChecked());
            }
        }
    }

    private void changeApp(int app) {
        if (app == androidAutoRadio.getId())
            launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.projection.gearhead");
        else
            launchIntent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.music");
    }

    private void start() {
        settingsManager.start();

        //Launch the selected app
        startActivity(launchIntent);

        finishAndRemoveTask();
    }

    private void stop(boolean enableWifi) {
        settingsManager.stop(enableWifi);

//                runningApps = activityManager.getRunningAppProcesses();
//                for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
//                    if (runningApp.processName.equals("com.google.android.music")) {
//
//                        activityManager.killBackgroundProcesses("com.google.android.music");
//                        break;
//                    }
//                }

        finishAndRemoveTask();
    }
}

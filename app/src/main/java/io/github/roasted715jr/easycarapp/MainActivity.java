package io.github.roasted715jr.easycarapp;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private int notificationId = 1;
    private Intent launchIntent;

    //Settings Variables
    private BluetoothAdapter bluetoothAdapter;
    private SettingsManager settingsManager;
    private TelephonyManager telephonyManager;
    private WifiManager wifiManager;

    //Notifications
    private Intent stopIntent;
    private PendingIntent stopPendingIntent;
    NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;

    //UI
    private Button startBtn, stopBtn;
    private CheckBox enableWifiCheck;
    private RadioButton androidAutoRadio, samsungMusicRadio;
    private RadioGroup appRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init variables
        launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.projection.gearhead");
        notificationManager = NotificationManagerCompat.from(this);

        //Init settings variables
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        settingsManager = new SettingsManager(bluetoothAdapter, telephonyManager, wifiManager);

        //Init UI variables
        startBtn = (Button) findViewById(R.id.btn_start);
        stopBtn = (Button) findViewById(R.id.btn_stop);
        enableWifiCheck = (CheckBox) findViewById(R.id.check_enable_wifi);
        androidAutoRadio = (RadioButton) findViewById(R.id.btn_android_auto);
        samsungMusicRadio = (RadioButton) findViewById(R.id.btn_samsung_music);
        appRadioGroup = (RadioGroup) findViewById(R.id.radio_group_choose_app);

        stopIntent  = new Intent(this, MainActivity.class).putExtra("ACTION", 0);
        stopPendingIntent = PendingIntent.getActivity(this, 0, stopIntent, 0);
        notificationBuilder  = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_car_white_24dp)
                .setContentTitle("Easy Car App")
                .setContentText("Easy Car App is running")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setShowWhen(false)
                .setOngoing(true)
                .setContentIntent(stopPendingIntent)
                .setAutoCancel(true);

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
            if (ACTION == 1) {
                start();
            } else if (ACTION == 0) {
                stop(true);
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

        notificationManager.notify(notificationId, notificationBuilder.build());

        finishAndRemoveTask();
    }

    private void stop(boolean enableWifi) {
        settingsManager.stop(enableWifi);

        notificationManager.cancel(notificationId);

        finishAndRemoveTask();
    }
}

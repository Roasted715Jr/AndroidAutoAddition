package io.github.roasted715jr.androidautoaddition;

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

public class MainActivity extends AppCompatActivity {

    private static final int DATA_ENABLED = 1;
    private static final String TAG = "MainActivity";

    private BluetoothAdapter bluetoothAdapter;
    private Button startBtn, endBtn;
    private CheckBox enableWifiCheck;
    private Intent launchIntent;
    private RadioButton androidAutoRadio, samsungMusicRadio;
    private RadioGroup appRadioGroup;
    private TelephonyManager telephonyManager;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init variables
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        startBtn = (Button) findViewById(R.id.btn_start);
        endBtn = (Button) findViewById(R.id.btn_end);
        enableWifiCheck = (CheckBox) findViewById(R.id.check_enable_wifi);
        launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.projection.gearhead");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        androidAutoRadio = (RadioButton) findViewById(R.id.btn_android_auto);
        samsungMusicRadio = (RadioButton) findViewById(R.id.btn_samsung_music);
        appRadioGroup = (RadioGroup) findViewById(R.id.radio_group_choose_app);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //Set the button listeners
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Turn off wifi (this must be done first because mobile data won't be used until the wifi is turned off
                wifiManager.setWifiEnabled(false);

                //Wait for mobile data to be turned on
                while (checkDataState() != DATA_ENABLED) {}

                //Turn on Bluetooth so the phone can connect automatically
                if (!bluetoothAdapter.isEnabled())
                    bluetoothAdapter.enable();

                //Launch Android Auto
                startActivity(launchIntent);
            }
        });
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter.disable();
                wifiManager.setWifiEnabled(enableWifiCheck.isChecked());
            }
        });
        appRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                changeApp(i);
            }
        });
    }

    private int checkDataState() {
        switch (telephonyManager.getDataState()) {
            case TelephonyManager.DATA_CONNECTED:
                return DATA_ENABLED;
            case TelephonyManager.DATA_CONNECTING:
                return DATA_ENABLED;
            default:
                return 0;
        }
    }

    private void changeApp(int app) {
        if (app == androidAutoRadio.getId())
            launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.projection.gearhead");
        else
            launchIntent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.music");
    }
}

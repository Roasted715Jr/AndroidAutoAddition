package io.github.roasted715jr.easycarapp;

import android.bluetooth.BluetoothAdapter;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class SettingsManager {

    private static final int DATA_ENABLED = 1;

    private BluetoothAdapter bluetoothAdapter;
    private TelephonyManager telephonyManager;
    private WifiManager wifiManager;

    public SettingsManager(BluetoothAdapter bluetoothAdapter, TelephonyManager telephonyManager, WifiManager wifiManager) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.telephonyManager = telephonyManager;
        this.wifiManager = wifiManager;
    }

    public void start() {
        //Turn off wifi (this must be done first because mobile data won't be used until the wifi is turned off
        wifiManager.setWifiEnabled(false);

        //Wait for mobile data to be turned on
        while (checkDataState() != DATA_ENABLED) {}

        //Turn on Bluetooth so the phone can connect automatically
        if (!bluetoothAdapter.isEnabled())
            bluetoothAdapter.enable();
    }

    public void stop(boolean enableWifi) {
        bluetoothAdapter.disable();
        //Settings.System.putInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, enableAirplaneModeCheck.isChecked() ? 0 : 1);
        wifiManager.setWifiEnabled(enableWifi);
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
}

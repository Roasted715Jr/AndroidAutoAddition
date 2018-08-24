package io.github.roasted715jr.easycarapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * The configuration screen for the {@link HomeAppController HomeAppController} AppWidget.
 */
public class HomeAppControllerConfigureActivity extends Activity {

    private static final String PREFS_NAME = "io.github.roasted715jr.easycarapp.HomeAppController";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    private BluetoothAdapter bluetoothAdapter;
    private Button finishCfgBtn;
//    private EditText mAppWidgetText;
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RadioButton androidAutoRadio, samsungMusicRadio;
    private RadioGroup appRadioGroup;
    private SettingsManager settingsManager;
    private TelephonyManager telephonyManager;
    private WifiManager wifiManager;

    View.OnClickListener finishCfg = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = HomeAppControllerConfigureActivity.this;

            // When the button is clicked, store the string locally
//            String widgetText = mAppWidgetText.getText().toString();
//            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            HomeAppController.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public HomeAppControllerConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.home_app_controller_configure);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        finishCfgBtn = (Button) findViewById(R.id.wdgt_btn_finish_cfg);
        androidAutoRadio = (RadioButton) findViewById(R.id.wdgt_btn_android_auto);
        samsungMusicRadio = (RadioButton) findViewById(R.id.wdgt_btn_samsung_music);
        appRadioGroup = (RadioGroup) findViewById(R.id.wdgt_radio_group_choose_app);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
//        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        //Init the Settings Manager
        //settingsManager = new SettingsManager(bluetoothAdapter, telephonyManager, wifiManager);

        finishCfgBtn.setOnClickListener(finishCfg);
        appRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeApp(checkedId);
            }
        });

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }


//        mAppWidgetText.setText(loadTitlePref(HomeAppControllerConfigureActivity.this, mAppWidgetId));
    }

    private void changeApp(int app) {
        if (app == androidAutoRadio.getId()) return;
            //launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.projection.gearhead");
        else return;
            //launchIntent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.music");
    }
}


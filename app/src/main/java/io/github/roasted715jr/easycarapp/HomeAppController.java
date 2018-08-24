package io.github.roasted715jr.easycarapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link HomeAppControllerConfigureActivity HomeAppControllerConfigureActivity}
 */
public class HomeAppController extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent startIntent = new Intent(context, MainActivity.class);
        startIntent.putExtra("ACTION", 1);
        PendingIntent startPendingIntent = PendingIntent.getActivity(context, 1, startIntent, 0);

        Intent stopIntent = new Intent(context, MainActivity.class);
        stopIntent.putExtra("ACTION", 0);
        PendingIntent stopPendingIntent = PendingIntent.getActivity(context, 2, stopIntent, 0);

//        CharSequence widgetText = HomeAppControllerConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_app_controller);
        views.setOnClickPendingIntent(R.id.wdgt_btn_start, startPendingIntent);
        views.setOnClickPendingIntent(R.id.wdgt_btn_stop, stopPendingIntent);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            HomeAppControllerConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


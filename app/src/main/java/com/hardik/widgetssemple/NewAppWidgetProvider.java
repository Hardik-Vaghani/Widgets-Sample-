package com.hardik.widgetssemple;

import static com.hardik.widgetssemple.NewAppWidgetConfig.KEY_BUTTON_TEXT;
import static com.hardik.widgetssemple.NewAppWidgetConfig.SHARED_PRES;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

public class NewAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent,PendingIntent.FLAG_IMMUTABLE);

            SharedPreferences prefs = context.getSharedPreferences(SHARED_PRES, Context.MODE_PRIVATE);
            String buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId,"Press me");

            Intent serviceIntent = new Intent(context,AppWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
            views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);
            views.setCharSequence(R.id.appwidget_button, "setText" ,buttonText);
            views.setRemoteAdapter(R.id.appwidget_stack_view, serviceIntent);
            views.setEmptyView(R.id.appwidget_stack_view, R.id.appwidget_empty_view);

            Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
            reSizeWidget(appWidgetOptions,views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
        reSizeWidget(newOptions,views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void reSizeWidget(Bundle appWidgetOptions, RemoteViews views) {
        int maxWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int maxHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
        int minWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int minHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

        if (maxHeight > 100) {
            views.setViewVisibility(R.id.appwidget_text, View.VISIBLE);
            views.setViewVisibility(R.id.appwidget_button, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.appwidget_text, View.GONE);
            views.setViewVisibility(R.id.appwidget_button, View.GONE);
        }
    }
}

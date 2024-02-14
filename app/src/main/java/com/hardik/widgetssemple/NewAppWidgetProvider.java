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
import android.widget.Toast;

public class NewAppWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_REFRESH = "actionRefresh";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // clicking on the button intent
            Intent buttonIntent = new Intent(context, MainActivity.class);
            PendingIntent buttonPendingIntent = PendingIntent.getActivity(context, 0,buttonIntent,PendingIntent.FLAG_IMMUTABLE);

            SharedPreferences prefs = context.getSharedPreferences(SHARED_PRES, Context.MODE_PRIVATE);
            String buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId,"Press me");

            // adapter on the stackView intent
            Intent serviceIntent = new Intent(context,AppWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            // stackView template clicking intent
            Intent clickIntent = new Intent(context, NewAppWidgetProvider.class);
            clickIntent.setAction(ACTION_REFRESH);
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_IMMUTABLE);

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
            views.setOnClickPendingIntent(R.id.appwidget_button, buttonPendingIntent);
            views.setCharSequence(R.id.appwidget_button, "setText" ,buttonText);
            views.setRemoteAdapter(R.id.appwidget_stack_view, serviceIntent);
            views.setEmptyView(R.id.appwidget_stack_view, R.id.appwidget_empty_view);
            views.setPendingIntentTemplate(R.id.appwidget_stack_view, clickPendingIntent);

            Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
            reSizeWidget(appWidgetOptions,views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_stack_view);// update our collection view
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

    @Override
    public void onEnabled(Context context) {
        Toast.makeText(context,"onEnabled",Toast.LENGTH_SHORT).show();
        super.onEnabled(context);
    }
    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context,"onDisabled",Toast.LENGTH_SHORT).show();
        super.onDisabled(context);
    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context,"onDeleted",Toast.LENGTH_SHORT).show();
        super.onDeleted(context, appWidgetIds);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_REFRESH.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        Toast.makeText(context,"Clicked position: "+appWidgetId,Toast.LENGTH_SHORT).show();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_stack_view);// update our collection view
        }
        super.onReceive(context, intent);
    }
}

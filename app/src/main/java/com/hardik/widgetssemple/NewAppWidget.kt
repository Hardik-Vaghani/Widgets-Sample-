package com.hardik.widgetssemple

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val prefs: SharedPreferences = context.getSharedPreferences(SHARED_PRES, AppCompatActivity.MODE_PRIVATE)
            val buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId, "Press me")

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.new_app_widget)
            views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent)
            views.setTextViewText(R.id.appwidget_button, buttonText)
//            views.setTextViewText(R.id.appwidget_text, buttonText)

            var appWidgetOptions: Bundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
            resizeWidget(appWidgetOptions, views)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        val views = RemoteViews(context?.packageName, R.layout.new_app_widget)
        resizeWidget(newOptions, views)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        Toast.makeText(context, "onEnabled!", Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        Toast.makeText(context, "onDisabled!", Toast.LENGTH_SHORT).show()
    }

    private fun resizeWidget(appWidgetOptions: Bundle, views: RemoteViews) {
        var minWidth: Int =
            appWidgetOptions.run { getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) }
        var maxWidth: Int =
            appWidgetOptions.run { getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH) }
        var minHeight: Int =
            appWidgetOptions.run { getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) }
        var maxHeight: Int =
            appWidgetOptions.run { getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT) }

        var dimensions =
            "MinWidth:$minWidth\nMaxWidth:$maxWidth\nMinHeight:$minHeight\nMaxHeight:$maxHeight"
        Log.e("TAG", "resizeWidget: $dimensions", )

        if (maxHeight > 100) {
            views.setViewVisibility(R.id.appwidget_text, View.VISIBLE)
        } else {
            views.setViewVisibility(R.id.appwidget_text, View.GONE)

        }
    }
}

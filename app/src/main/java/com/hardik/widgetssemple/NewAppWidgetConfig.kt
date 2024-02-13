package com.hardik.widgetssemple

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RemoteViews

const val SHARED_PRES = "prefs"
const val KEY_BUTTON_TEXT = "keyButtonText"

class NewAppWidgetConfig : AppCompatActivity() {
    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var editTextButton: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_app_widget_config)

        val configIntent: Intent = getIntent()
        val extras: Bundle? = configIntent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        val resultValue: Intent = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }

        editTextButton = findViewById(R.id.edit_text_button)

    }

    @SuppressLint("CommitPrefEdits")
    fun confirmConfiguration(view: View) {
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(this)
        val intent: Intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        var buttonText: String = editTextButton.text.toString()

        val views : RemoteViews = RemoteViews(this.packageName,R.layout.new_app_widget)
        views.setOnClickPendingIntent(R.id.appwidget_button,pendingIntent)
        views.setCharSequence(R.id.appwidget_button,"setText", buttonText)
//        views.setInt(R.id.appwidget_text,"setBackgroundColor",Color.RED)
//        views.setBoolean(R.id.appwidget_text,"setEnabled",false)

        appWidgetManager.updateAppWidget(appWidgetId, views)

        val prefs :SharedPreferences = getSharedPreferences(SHARED_PRES, MODE_PRIVATE)
        val editor:SharedPreferences.Editor = prefs.edit()
        editor.putString(KEY_BUTTON_TEXT + appWidgetId, buttonText)
        editor.apply()

        val resultValue:Intent = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}
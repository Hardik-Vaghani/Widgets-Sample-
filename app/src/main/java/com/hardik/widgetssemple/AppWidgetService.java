package com.hardik.widgetssemple;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppWidgetItemFactory(getApplicationContext(), intent);
    }
    static class AppWidgetItemFactory implements RemoteViewsFactory {
        private Context context;
        private int appWidgetId;
        private String[] exampleData = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};

        public AppWidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            //connect to data source
            SystemClock.sleep(3000);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            //close data source
        }

        @Override
        public int getCount() {
            return exampleData.length;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget_item);
            views.setTextViewText(R.id.appwidget_tv_item, exampleData[position]);
            SystemClock.sleep(500);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

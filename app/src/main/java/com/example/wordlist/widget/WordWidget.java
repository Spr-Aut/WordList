package com.example.wordlist.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.wordlist.R;
import com.example.wordlist.activity.MainActivity;
import com.example.wordlist.broadcast.BroadcastName;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class WordWidget extends AppWidgetProvider {
    Button btnWidgetRefresh;
    TextView tvWidgetWord;
    private static Set idsSet = new HashSet();
    private static final String TAG = "WordWidget";
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG,"收到广播"+intent.getAction());
        if (intent!=null&&intent.getAction().equals(BroadcastName.ACTION_WIDGET_UPDATE)){
            //Log.d(TAG,"小组件收到广播"+idsSet.size());
            updateAll(context,AppWidgetManager.getInstance(context));
            Log.d(TAG,"执行到了updateAll之后");
        }else if (intent!=null&&intent.getAction().equals(BroadcastName.ACTION_WIDGET_BUTTON)){
            Log.d(TAG,"小组件收到广播:按钮");
        }

    }

    static void updateAll(Context context, AppWidgetManager appWidgetManager){
        Log.d(TAG,"更新所有小组件");
        RemoteViews views=getRemoteViews(context);
        appWidgetManager.updateAppWidget(new ComponentName(context,WordWidget.class),views);
    }

    static void updateWidget(Context context,AppWidgetManager appWidgetManager,int appWidgetId){
        RemoteViews views=getRemoteViews(context);
        appWidgetManager.updateAppWidget(appWidgetId,views);
    }

    static RemoteViews getRemoteViews(Context context){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.word_widget);
        //Intent intentClick=new Intent();
        //intentClick.setClass(context, MainActivity.class);
        //intentClick.setAction(BroadcastName.ACTION_WIDGET_BUTTON);
        //intentClick.setClassName(context,"com.example.wordlist.widget.WordWidget");
        //PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intentClick,0);
        //views.setOnClickPendingIntent(R.id.btnWidgetRefresh,pendingIntent);
        views.setTextViewText(R.id.tvWidgetWord,"7890");
        return views;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d(TAG,"小组件添加："+appWidgetId);
            updateWidget(context,appWidgetManager,appWidgetId);

        }

        //Log.d(TAG,"小组件剩余："+idsSet);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId:appWidgetIds){
            Log.d(TAG,"小组件删除："+appWidgetId);
        }
        super.onDeleted(context, appWidgetIds);

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
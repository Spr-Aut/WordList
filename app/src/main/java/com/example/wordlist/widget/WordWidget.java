package com.example.wordlist.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.room.Room;

import com.example.wordlist.R;
import com.example.wordlist.WidgetWordDetailActivity;
import com.example.wordlist.activity.MainActivity;
import com.example.wordlist.activity.WordDetailActivity;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.database.WordDatabase;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class WordWidget extends AppWidgetProvider {
    private static final String TAG = "WordWidget";



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG,"收到广播"+intent.getAction());
        if (intent!=null&&intent.getAction().equals(BroadcastName.ACTION_WIDGET_UPDATE)){
            Log.d(TAG,"小组件收到广播:更新所有小组件");
            updateAll(context,AppWidgetManager.getInstance(context));

        }else if (intent!=null&&intent.getAction().equals(BroadcastName.ACTION_WIDGET_BUTTON)){
            Log.d(TAG,"小组件收到广播:按钮");
            updateAll(context,AppWidgetManager.getInstance(context));

        }else if (intent!=null&&intent.getAction().equals(BroadcastName.ACTION_WIDGET_ACTIVITY)){
            /*String name=TempMsg.getWidgetWordName();
            Log.d(TAG,"小组件收到广播:跳转，name="+name);
            Intent intentActivity=new Intent(context, WordDetailActivity.class);
            intentActivity.putExtra("name",name);
            Log.d(TAG,"启动activity，name为："+name);
            intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            int requestCode=name.hashCode();
            PendingIntent pendingActivity=PendingIntent.getActivity(context,requestCode,intentActivity,PendingIntent.FLAG_IMMUTABLE);
            try {
                pendingActivity.send();
            } catch (PendingIntent.CanceledException e) {
                throw new RuntimeException(e);
            }*/
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
        MyTools.timeStart();
        WordDao wordDao = Room.databaseBuilder(context, WordDatabase.class,"WordInfo").addMigrations().allowMainThreadQueries().build().wordDao();
        //List<String>nameList=wordDao.getWordNameList();
        /*获取随机数，用于从数据库中随机读一个单词*/
        long maxTime = wordDao.getMaxTime();
        long minTime = wordDao.getMinTime();
        long chazhi=maxTime-minTime;
        Log.d(TAG,"获取最大值："+maxTime+",最小值："+minTime+"差值为："+chazhi);
        Random random=new Random();
        long timeStamp=(long)(random.nextFloat()*chazhi)+minTime;
        Log.d(TAG,"随机数为："+timeStamp);

        String name = wordDao.getWordNameRandom(timeStamp);
        Log.d(TAG,name+"");
        WordInfo word=wordDao.getWordByName(name);



        Log.d(TAG,"设置小组件的RemoteViews");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.word_widget);
        /*Intent intentActivity=new Intent(context,WordWidget.class);
        intentActivity.setAction(BroadcastName.ACTION_WIDGET_ACTIVITY);
        int requestCode=name.hashCode();
        PendingIntent pendingActivity=PendingIntent.getBroadcast(context,requestCode,intentActivity,PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.view_group_widget,pendingActivity);*/

        /*跳转到WidgetWordDetailActivity*/
        Intent intentActivity=new Intent(context, WidgetWordDetailActivity.class);
        intentActivity.putExtra("name",name);
        Log.d(TAG,"启动activity，name为："+name);
        intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int requestCode=name.hashCode();
        PendingIntent pendingActivity=PendingIntent.getActivity(context,requestCode,intentActivity,PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.view_group_widget,pendingActivity);

        /*绑定按钮点击事件*/
        Intent intentButton=new Intent(context,WordWidget.class);
        intentButton.setAction(BroadcastName.ACTION_WIDGET_BUTTON);
        PendingIntent pendingButton=PendingIntent.getBroadcast(context,0,intentButton,PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.btnWidgetRefresh,pendingButton);

        if (word!=null){
            views.setTextViewText(R.id.tvWidgetWord,word.getName());
            views.setTextViewText(R.id.tvWidgetSymbolUk,word.getSymbol_uk());
            views.setTextViewText(R.id.tvWidgetSymbolUs,word.getSymbol_us());
            views.setTextViewText(R.id.tvWidgetDesc,word.getDesc());

        }

        MyTools.timeEnd(TAG);
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
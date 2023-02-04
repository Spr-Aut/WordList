package com.example.wordlist.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyTools {
    private static final String TAG = "MyTools";
    public final static long ONE_DAY_MILLIS = 86400000L;
    public final static long TWO_DAY_MILLIS = 172800000L;
    public final static long SIX_DAY_MILLIS = 518400000L;
    public final static long THIRTY_DAY_MILLIS = 2592000000L;
    public final static long TWENTY_MIN_MILLIS = 1200000L;
    public final static long FIFTEEN_MIN_MILLIS = 900000L;
    public final static long EIGHT_HOUR_MILLIS = 28800000L;
    public final static long ONE_HOUR_MILLIS = 3600000L;

    public final static int EXISTENCE = 1;
    public final static int NONEXISTENCE = 0;

    /*判断单词是否出现被安排在在下一个，击中率为(100-memory)/100*/
    public static boolean random_hit(int memory) {
        if (memory < 0)
            memory = 0;
        int num = (int) (Math.random() * 101);
        return num <= 100 - memory;
    }

    /*获取当前时间戳  ***********************/
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /*艾宾浩斯 ****************************/
    public static int ebbinghaus(long relativeTimeStamp) {
        if (relativeTimeStamp > THIRTY_DAY_MILLIS)
            return 15;
        else if (relativeTimeStamp > SIX_DAY_MILLIS)
            return 21;
        else if (relativeTimeStamp > TWO_DAY_MILLIS)
            return 25;
        else if (relativeTimeStamp > ONE_DAY_MILLIS)
            return 28;
        else if (relativeTimeStamp > EIGHT_HOUR_MILLIS)
            return 34;
        else if (relativeTimeStamp > ONE_HOUR_MILLIS)
            return 36;
        else if (relativeTimeStamp > TWENTY_MIN_MILLIS)
            return 44;
        else if (relativeTimeStamp > FIFTEEN_MIN_MILLIS)
            return 58;
        else
            return 100;
    }

    /*获取当前时间*********/
    public static int getDate() {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return Integer.parseInt(simpleDateFormat.format(date));
    }
    public static boolean getNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void showMsg(String msg, Context context) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }

    /*获取登陆状态*/
    public static boolean getLoginStatus(Context context){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean("login_status",false);
    }
    /*存储登陆状态*/
    public static void setLoginStatus(Context context,boolean loginStatus){
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("login_status",loginStatus);
        editor.apply();
    }

    /*处理音标*/
    public static String proSymbol(String str){
        if (str!=null&&str.length()!=0){
            String res = str.split(",")[0];//将str用","分割成ArrayList，然后取第0个
            return "/" + res + "/";
        }else return "";
    }

    /*将str用"-"分割成ArrayList，然后取第0个*/
    public static String splitWithHengGang(String str){
        if (str!=null&&str.length()!=0){
            String res = str.split("-")[0];
            return res;
        }else return "";
    }

    /*处理网址，将http改为https*/
    public static String proURL(String str){
        if (str!=null&&str.length()!=0){
            String later=str.split(":")[1];
            return "https:"+later;
        }else return "";
    }

    /*处理desc，只取第一个意思*/
    public static String briefDesc(String desc){
        if (desc!=null&&desc.length()!=0){
            String briefDesc=desc.split("；")[0];//显示词性
            //去掉词性
            String[] strings = briefDesc.split("\\.");
            Log.d(TAG,briefDesc);
            int len=strings.length;
            Log.d(TAG,len+"");
            if (len!=0){
                briefDesc=strings[len-1];
            }

            return briefDesc;
        }else return "";
    }

    public static void setStatusBar(Activity activity){
        //适配MIUI，沉浸小横条和状态栏
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //状态栏文字显示为黑色
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        /*window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}

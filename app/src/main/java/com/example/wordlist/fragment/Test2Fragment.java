package com.example.wordlist.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.R;
import com.example.wordlist.broadcast.BroadcastName;

public class Test2Fragment extends Fragment {
    private static final String TAG = "Test2Fragment";
    private MyBroadcastReceiver broadcastReceiver;
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_test, container, false);

        textView=mView.findViewById(R.id.tv_test);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver=new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter(BroadcastName.REVIEW_CURRENT_WORD_REFRESH);
        mContext.registerReceiver(broadcastReceiver,filter);
        Log.d(TAG,"启动");
    }

    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(broadcastReceiver);
        Log.d(TAG,"停止");
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null&&intent.getAction().equals(BroadcastName.REVIEW_CURRENT_WORD_REFRESH)){

                Log.d(TAG,"收到广播");
                String name=intent.getStringExtra("name");
                textView.setText(name);
            }
        }
    }


}

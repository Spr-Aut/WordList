package com.example.wordlist.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.R;

public class SettingsFragment extends Fragment {
    private static final String TAG = "TabThirdFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_third.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView tv_third = mView.findViewById(R.id.tv_third);
        tv_third.setText("我是购物车页面");
        long timeMillis = MyTools.getCurrentTimeMillis();
        long time=1675341973622L;
        tv_third.setText(Integer.MAX_VALUE+"");

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"启动");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"继续");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"停止");
    }



    /**
     * 切换Fragment可视状态
     * */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Log.d(TAG,"切换可视状态");
        }else {
        }
    }

}

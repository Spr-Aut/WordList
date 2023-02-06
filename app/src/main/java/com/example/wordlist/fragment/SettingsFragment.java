package com.example.wordlist.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.R;

public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_third.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_main, container, false);

        /*CardView cardView=mView.findViewById(R.id.card_index_start);
        TextView textView=mView.findViewById(R.id.tv_hello);
        WordDao wordDao= MainApplication.getInstance().getWordDB().wordDao();
        WordInfo wordInfo= wordDao.getWordByName("123");
        if (wordInfo==null){
            Log.d(TAG,"name");
        }*/


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

package com.example.wordlist.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wordlist.R;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.util.TempMsg;

public class TestFragment extends Fragment {
    private static final String TAG = "TestFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_test, container, false);

        TextView textView=mView.findViewById(R.id.tv_test);
        textView.setOnClickListener(v -> {
            Intent intent=new Intent(BroadcastName.REVIEW_CURRENT_WORD_REFRESH);
            intent.putExtra("name","123456");
            mContext.sendBroadcast(intent);
            Log.d(TAG,"发送广播,name="+123456);
        });

        return mView;
    }
}


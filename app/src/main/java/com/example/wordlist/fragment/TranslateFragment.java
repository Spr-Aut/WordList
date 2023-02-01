package com.example.wordlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.R;

public class TranslateFragment extends Fragment {
    private static final String TAG = "TranslateFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_translate, container, false);
        TextView tv_second = mView.findViewById(R.id.tv_translate);
        tv_second.setText("我是翻译页面");



        return mView;
    }
}

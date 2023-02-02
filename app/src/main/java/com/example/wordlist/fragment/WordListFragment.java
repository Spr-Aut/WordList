package com.example.wordlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.activity.MainActivity;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.entity.BookInfo;

import java.util.List;

public class WordListFragment extends Fragment {
    private static final String TAG = "WordListFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private static TextView tv_second;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_word_list, container, false);
        tv_second = mView.findViewById(R.id.tv_word_list);
        tv_second.setText("我是词本页面");



        return mView;
    }

    public static void vis(){
        tv_second.setText("收到");
    }

}

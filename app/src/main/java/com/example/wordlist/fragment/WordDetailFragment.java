package com.example.wordlist.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wordlist.R;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.util.MyTools;

import java.util.ArrayList;

public class WordDetailFragment extends Fragment {
    private static final String TAG = "WordDetailFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    private TextView tvName;//单词名
    private TextView tvSymbolUk;//英式音标
    private TextView tvSymbolUs;//美式音标
    private TextView tvDesc;//释义
    private TextView tvSentence;//例句
    private MyBroadcastReceiver broadcastReceiver;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_word_detail, container, false);

        tvName=mView.findViewById(R.id.tv_detail_name);
        tvSymbolUk=mView.findViewById(R.id.tv_detail_symbol_uk);
        tvSymbolUs=mView.findViewById(R.id.tv_detail_symbol_us);
        tvDesc=mView.findViewById(R.id.tv_detail_desc);
        tvSentence=mView.findViewById(R.id.tv_detail_sentence);


        //refresh();





        return mView;
    }

    private void refresh(){

    }


    private void refresh(ArrayList<String> word_detail_list) {
        //Toast.makeText(mContext,TAG+"刷新",Toast.LENGTH_SHORT).show();
        tvName.setText(word_detail_list.get(0));
        tvSymbolUk.setText(word_detail_list.get(1));
        tvSymbolUs.setText(word_detail_list.get(2));
        tvDesc.setText(word_detail_list.get(3));
        tvSentence.setText(word_detail_list.get(4));
    }

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver=new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter(BroadcastName.WORD_DETAIL_REFRESH);
        mContext.registerReceiver(broadcastReceiver,filter);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(mContext,TAG+"启动时刷新",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(broadcastReceiver);
    }



    /**
     * 切换Fragment可视状态
     * */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            refresh();
        }else {
        }
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null&&intent.getAction().equals(BroadcastName.WORD_DETAIL_REFRESH)){

                ArrayList<String> word_detail = intent.getStringArrayListExtra("WORD_DETAIL");
                //MyTools.showMsg("收到广播"+word_detail.get(0),mContext);
                refresh(word_detail);
            }
        }
    }



}

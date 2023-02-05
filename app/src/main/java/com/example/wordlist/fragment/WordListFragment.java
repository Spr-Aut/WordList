package com.example.wordlist.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.adapter.SlideRecyclerViewAdapter;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.customview.SlideRecyclerView;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.entity.BookInfo;
import com.example.wordlist.entity.WordInfo;

import java.util.ArrayList;
import java.util.List;

public class WordListFragment extends Fragment {
    private static final String TAG = "WordListFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private MyBroadcastReceiver broadcastReceiver;
    private SlideRecyclerViewAdapter slideAdapter;
    private SlideRecyclerView mRecyclerView;

    BookDao bookDao= MainApplication.getInstance().getBookDB().bookDao();
    WordDao wordDao=MainApplication.getInstance().getWordDB().wordDao();
    List<BookInfo> allBook;
    List<WordInfo> allWord;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        mView = inflater.inflate(R.layout.fragment_word_list, container, false);// 根据布局文件fragment_tab_second.xml生成视图对象

        initData();

        mRecyclerView=mView.findViewById(R.id.rc_word_list);


        WordInfo wordInfo=new WordInfo();
        wordInfo.setName("原文");
        wordInfo.setDesc("译文");
        allWord.add(wordInfo);

        slideAdapter=new SlideRecyclerViewAdapter(mContext,allWord);

        //设置翻译结果的显示与否
        //SharedPreferences sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE);
        //slideAdapter.TRANS_VISUAL_STATE=sharedPreferences.getInt("trans_visual_state",0);

        mRecyclerView.setAdapter(slideAdapter);

        RecyclerView.LayoutManager linearLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //slideAdapter.setViewType(SlideRecyclerViewAdapter.TYPE_LINEAR_LAYOUT);
        slideAdapter.notifyDataSetChanged();

        //refresh();





        return mView;
    }

    private void initData() {
        allWord=new ArrayList<>();
        Log.d(TAG,"初始化");
    }

    private void refresh() {

        /*allBook = bookDao.getAllBook();
        if (allBook.size()==0){
            tv_second.setText("没有书");
        }else {
            //tv_second.setText(allBook.get(0).getName());
            tv_second.setText(allBook.get(0).getPrice()+"");
        }*/

        allWord=wordDao.getAllWord();
        slideAdapter.refreshData(allWord);
        //Log.d(TAG,"数据库第一个词为"+wordDao.getAllWord().get(0).getName());//数据库空时报错
        Log.d(TAG,"刷新");

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
        Log.d(TAG,"resume");
        refresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

            //refresh();
        }else {

        }
    }



    private class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null&&intent.getAction().equals(BroadcastName.WORD_DETAIL_REFRESH)){
                //refresh();
            }
        }
    }


}

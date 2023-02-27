package com.example.wordlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.tuple.WordNameTransTuple;
import com.example.wordlist.util.MyTools;

import java.util.List;

public class StatisticsFragment extends Fragment {
    private static final String TAG = "StatisticsFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private TextView tvStatistics;
    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_third.xml生成视图对象
        mView = inflater.inflate(R.layout.frgment_statistics, container, false);

        tvStatistics=mView.findViewById(R.id.tv_statistics);

        Button button=mView.findViewById(R.id.btn_add_batch);
        Button button2=mView.findViewById(R.id.btn_delete_batch);
        Button button3=mView.findViewById(R.id.btn_query_batch);
        button.setOnClickListener(v -> addToDB());
        button2.setOnClickListener(v -> deleteAll());
        button3.setOnClickListener(v -> getWordList());

        return mView;
    }

    private void init() {
        Log.d(TAG,"刷新统计数据");
        long time = MyTools.getCurrentTimeMillis();
        List<WordInfo> allWord = wordDao.getAllWord();
        int []memory=new int[4];
        for(WordInfo word:allWord){
            int mem = word.getMemory();
            mem=mem>3?3:mem;
            memory[mem]++;
        }
        time=MyTools.getCurrentTimeMillis()-time;
        Log.d(TAG,"读取数据库耗时"+time);
        tvStatistics.setText(memory[0]+","+memory[1]+","+memory[2]+","+memory[3]);
    }

    private void addToDB(){
        long time = MyTools.getCurrentTimeMillis();
        WordInfo word=wordDao.getWordByName("nothing");
        for(int i=0;i<5000;i++){
            word.setName(i+"");
            wordDao.insertOneWord(word);
        }
        time=MyTools.getCurrentTimeMillis()-time;
        Log.d(TAG,"读取数据库耗时"+time);
    }

    private void deleteAll(){
        WordInfo word=new WordInfo();
        long time = MyTools.getCurrentTimeMillis();
        for(int i=0;i<5000;i++){
            word.setName(i+"");
            wordDao.deleteWord(word);
        }
        time=MyTools.getCurrentTimeMillis()-time;
        Log.d(TAG,"读取数据库耗时"+time);
    }

    private void getWordList(){
        MyTools.timeStart();

        List<WordInfo> wordList = wordDao.getAllWord();
        String name = wordList.get(5000).getName();


        MyTools.timeEnd(TAG);
        Log.d(TAG,name);

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
        //init();
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

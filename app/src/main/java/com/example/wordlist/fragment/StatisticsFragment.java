package com.example.wordlist.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.compose.ui.platform.ComposeView;
import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.XMLParse;
import com.example.wordlist.activity.ComposeActivity;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.tuple.WordNameTransTuple;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StatisticsFragment extends Fragment {
    private static final String TAG = "StatisticsFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    TextView tvStatistics;
    PieChartView pieChart;
    PieChartData pieChartData;
    LinearLayout viewAdminSettings;
    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_third.xml生成视图对象
        mView = inflater.inflate(R.layout.frgment_statistics, container, false);

        tvStatistics=mView.findViewById(R.id.tv_statistics);
        pieChart=mView.findViewById(R.id.pieChart);
        viewAdminSettings=mView.findViewById(R.id.view_admin_settings);

        Button button=mView.findViewById(R.id.btn_add_batch);
        Button button2=mView.findViewById(R.id.btn_delete_batch);
        Button button3=mView.findViewById(R.id.btn_query_batch);
        Button button4=mView.findViewById(R.id.btn_reset_batch);
        button.setOnClickListener(v -> asyncTask(0));
        button2.setOnClickListener(v -> asyncTask(1));
        button3.setOnClickListener(v -> asyncTask(2));
        button4.setOnClickListener(v -> asyncTask(3));

        viewAdminSettings.setVisibility(View.INVISIBLE);
        tvStatistics.setOnClickListener(v -> {
            if (viewAdminSettings.getVisibility()==View.INVISIBLE)viewAdminSettings.setVisibility(View.VISIBLE);
            else viewAdminSettings.setVisibility(View.INVISIBLE);

        });


        return mView;
    }

    private void asyncTask(int taskId){
        tvStatistics.setText("正在执行MyAsyncTask...");
        new MyAsyncTask().execute(taskId);
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

    private void refreshPieChart() {
        MyTools.timeStart();
        pieChartData=new PieChartData();
        int numValues=5;//扇形数量
        int totalNum= wordDao.countAll();
        int[] item=new int[5];
        item[0]= wordDao.countMem0();
        item[1]=wordDao.countMem1();;
        item[2]=wordDao.countMem2();;
        item[3]=wordDao.countMem3Op0();;//mem>=3,op==0
        item[4]=wordDao.countMem3Op1();;//mem>=3,op==1
        int[] color=new int[]{
                getResources().getColor(R.color.pie_color1),
                getResources().getColor(R.color.pie_color2),
                getResources().getColor(R.color.pie_color3),
                getResources().getColor(R.color.pie_color4),
                getResources().getColor(R.color.pie_color5)
        };
        List<SliceValue>values=new ArrayList<>();
        for (int i = 0; i < numValues; i++) {
            SliceValue sliceValue = new SliceValue(item[i], color[i]);
            values.add(sliceValue);
        }
        pieChartData.setValues(values);
        pieChartData.setHasLabels(true);
        pieChartData.setCenterText1(item[4]+"/"+totalNum);
        pieChartData.setCenterText1Color(Color.GRAY);
        pieChartData.setHasCenterCircle(true);
        //pieChartData.setCenterCircleScale(0.4f);//中间圆环的大小
        //pieChartData.setSlicesSpacing(24);//扇形之间的间距
        pieChartData.setCenterText2("已完成");
        pieChartData.setCenterText2Color(Color.GRAY);
        pieChart.startDataAnimation();
        pieChart.setPieChartData(pieChartData);
        MyTools.timeEnd(TAG,"刷新pieChart");
    }



    private void addToDB(){
        long time = MyTools.getCurrentTimeMillis();
        WordInfo word=wordDao.getWordByName("but");
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

    private void resetAll() {
        long time = MyTools.getCurrentTimeMillis();
        List<WordInfo>wordList=wordDao.getAllWord();
        for (WordInfo wordInfo : wordList) {
            wordInfo.setMemory(0);
            wordInfo.setWord_operation(0);
            wordDao.updateWord(wordInfo);
        }

        time=MyTools.getCurrentTimeMillis()-time;
        Log.d(TAG,"读取数据库耗时"+time);
    }

    private void getWordList(){
        MyTools.timeStart();

        /*List<WordInfo> wordList = wordDao.getAllWord();
        String name = wordList.get(5000).getName();*/
        Log.d(TAG,wordDao.countAll()+",Mem0:"+wordDao.countMem0()+",Mem1:"+wordDao.countMem1()+",Mem2:"+wordDao.countMem2()+",Mem30:"+wordDao.countMem3Op0()+",Mem31:"+wordDao.countMem3Op1()+"");

        MyTools.timeEnd(TAG);
        //Log.d(TAG,name);

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
        refreshPieChart();
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

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... integers) {
            Log.d(TAG,"正在执行MyAsyncTask...");

            switch (integers[0]){
                case 0 -> addToDB();
                case 1 -> deleteAll();
                case 2 -> getWordList();
                case 3 -> resetAll();
            }
            return true;
        }

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            //    更新ProgressDialog的进度条

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){
                Log.d(TAG,"MyAsyncTask执行完成");
                tvStatistics.setText("数据统计");
                refreshPieChart();
            }else {
                Log.d(TAG,"MyAsyncTask执行失败");
            }
        }
    }

}

package com.example.wordlist.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.activity.WordDetailActivity;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.tuple.WordNameMemTuple;
import com.example.wordlist.tuple.WordNameTransTuple;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.R;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.TempMsg;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class ReviewFragment extends Fragment {
    private static final String TAG = "ReviewFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private TextView tvLastText;
    private TextView tvLast;
    private TextView tvWord;
    private TextView tvSymbol;
    private TextView tvSentence;
    private CardView cvSentence;
    private CardView cvWell;
    private CardView cvKnow;
    private CardView cvAmbiguous;
    private CardView cvUnKnow;
    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();
    private List<WordNameMemTuple> wordList;
    private int current;//当前队列
    private int count=0;//当前队列到第几个了
    private int maxCount=4;//背几个换队列
    private boolean isNotEmpty=false;//数据库是否空
    private boolean isFirstBoot=true;
    private Queue<WordNameMemTuple> queue0=new LinkedList<>();
    private Queue<WordNameMemTuple> queue1=new LinkedList<>();
    private Queue<WordNameMemTuple> queue2=new LinkedList<>();
    private Queue<WordNameMemTuple> queue3=new LinkedList<>();
    private Queue<WordNameMemTuple> currentQueue;
    private int delayToDetail=500;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.activity_learn_word, container, false);

        Log.d(TAG,"执行onCreateView");

        /*避免每次切换fragment都刷新队列*/
        if (isFirstBoot){//是第一次启动
            isFirstBoot=false;
            prepareData();
        }
        //prepareData();
        bindView();

        if (isNotEmpty){
            refreshView();
        }else {
            MyTools.showMsg("数据库空",mContext);
            learnFinish();
        }

        return mView;
    }

    private void bindView(){
        tvLastText=mView.findViewById(R.id.tv_last_learn_text);
        tvLast=mView.findViewById(R.id.tv_last_learn);
        tvWord=mView.findViewById(R.id.tv_word_learn);
        tvSymbol=mView.findViewById(R.id.tv_symbol_learn);
        tvSentence=mView.findViewById(R.id.tv_sentence_learn);
        cvSentence=mView.findViewById(R.id.cv_sentence_learn);
        cvWell=mView.findViewById(R.id.cv_know_well_learn);
        cvKnow=mView.findViewById(R.id.cv_know_learn);
        cvAmbiguous=mView.findViewById(R.id.cv_ambiguous_learn);
        cvUnKnow=mView.findViewById(R.id.cv_not_know_learn);
    }

    private void learnFinish() {
        cvSentence.setVisibility(View.GONE);
        LinearLayout layout=mView.findViewById(R.id.linear_word_bottom);
        layout.setVisibility(View.GONE);
        tvSymbol.setVisibility(View.GONE);
        tvWord.setText("今日单词已学完");
    }

    private void refreshView(){
        String lastWord=TempMsg.LastWordLearn;
        if (lastWord!=null&&lastWord.length()!=0){
            tvLastText.setText("上一个：");
            String str=lastWord+":"+MyTools.briefDesc(wordDao.getWordByName(lastWord).getDesc());
            tvLast.setText(str);
            tvLast.setOnClickListener(v -> turnToDetail(lastWord));
        }
        tvWord.setText(TempMsg.WordLearn.getName());
        tvSymbol.setText(TempMsg.WordLearn.getSymbol_uk());
        tvSymbol.setOnClickListener(v -> {
            playSound(TempMsg.WordLearn.getSound_uk());
        });
        tvSentence.setText("");
        cvSentence.setOnClickListener(v -> {
        Log.d(TAG,"切换句子的显示状态:"+ TempMsg.WordLearn.getSentence());
            if (tvSentence.getText()==""&&isNotEmpty)tvSentence.setText(MyTools.briefSentence(TempMsg.WordLearn.getSentence()));
            else tvSentence.setText("");
        });
        cvWell.setOnClickListener(v -> opWell());
        cvKnow.setOnClickListener(v -> opKnow());
        cvAmbiguous.setOnClickListener(v -> {
            /*try {
                opAmbiguous();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            opAmbiguous();
        });
        cvUnKnow.setOnClickListener(v -> opUnKnow());
    }
    private void playSound(String soundRul) {
        if (soundRul!=null&&soundRul.length()!=0){
            Log.d(TAG, "点击播放声音");
            //String sound="https://res.iciba.com/resource/amp3/oxford/0/6f/0a/6f0a0924a725e59aa0104109317cfa09.mp3";
            Log.d(TAG,"soundUrl:   "+soundRul);
            MediaPlayer mediaPlayer = MediaPlayer.create(mContext, Uri.parse(soundRul));
            mediaPlayer.start();
        }else {
            MyTools.showMsg("声音获取失败",mContext);
        }
    }

    /*初次启动才会执行*/
    private void prepareData() {

        wordList=wordDao.getAllOpWord();//加载所有word_operation=0的词
        if (wordList.size()!=0)isNotEmpty=true;//非空


        if (isNotEmpty){
            addWordListToQueue();//从wordList加载到四个队列
            if (isQueueEmpty()){//0、1、2队列空，没有要学的词，返回到onCreateView，执行learnFinish();
                isNotEmpty=false;
                return;
            }

            currentQueue=getCurrentQueue();
            WordNameMemTuple tuple;
            while (true){
                tuple = getCurrentQueue().remove();
                if (tuple!=null)break;
            }
            TempMsg.WordLearn = MyTools.nameMemToWord(tuple);//把第一个词赋给TempMsg.WordLearn

        }


    }

    /*从wordList加载到四个队列*/
    private void addWordListToQueue() {
        Log.d(TAG,"从wordList加载到四个队列");
        MyTools.timeStart();
        for (WordNameMemTuple word : wordList) {
            //将所有mem置为0
            //word.setMemory(0);
            WordInfo wordInfo = MyTools.nameMemToWord(word);
            if (wordInfo!=null){
                //wordDao.updateWord(wordInfo);
                addWordToQueue(wordInfo);
            }
        }
        MyTools.timeEnd(TAG);
    }

    /*加载单个word到队列*/
    private void addWordToQueue(WordInfo wordInfo){
        WordNameMemTuple word=new WordNameMemTuple(wordInfo.getName(),wordInfo.getMemory());
        switch (word.getMemory()){
            case 0-> queue0.add(word);
            case 1-> queue1.add(word);
            case 2-> queue2.add(word);
            case 3-> queue3.add(word);
        }
    }

    /*获取当前应学习的队列*/
    private Queue<WordNameMemTuple> getCurrentQueue(){
        if (isQueueEmpty()){
            refreshData();
            return currentQueue;
        }
        Log.d(TAG,"队列的size分别为："+ queue0.size()+","+queue1.size()+","+queue2.size()+","+queue3.size());
        if (current==0){
            if (queue0.size()!=0){
                Log.d(TAG,"切换到队列"+current);
                return queue0;
            }
            else {
                current++;
                count=0;
                Log.d(TAG,"切换到队列"+current);
                return getCurrentQueue();
            }
        }else if (current==1){
            if (queue1.size()!=0){
                Log.d(TAG,"切换到队列"+current);
                return queue1;
            }
            else {
                current++;
                count=0;
                Log.d(TAG,"切换到队列"+current);
                return getCurrentQueue();
            }
        }else if (current==2){
            if (queue2.size()!=0){
                Log.d(TAG,"切换到队列"+current);
                return queue2;
            }
            else {
                current++;
                count=0;
                Log.d(TAG,"切换到队列"+current);
                return getCurrentQueue();
            }
        }else {
            Log.d(TAG,"切换到队列"+current);
            current=0;
            count=0;

            return getCurrentQueue();
        }
    }

    private void refreshData(){
        if (isQueueEmpty()){//如果0、1、2队列全空，学习结束
            tvWord.setText("");
            Log.d(TAG,"背完了");
            learnFinish();
            return;
        }
        if (count>=maxCount){//当前队列学习的词达到了切换限值
            count=0;
            current++;
            Log.d(TAG,"切换队列");
        }else {
            count++;
        }


        TempMsg.LastWordLearn=TempMsg.WordLearn.getName();//上一个词
        //currentQueue=getCurrentQueue();//得到当前应学的队列
        WordNameMemTuple tuple;
        WordInfo wordInfo;
        while (true){
            Log.d(TAG,"获取下一个");
            tuple = getCurrentQueue().remove();
            if (isQueueEmpty()){
                tvWord.setText("");
                Log.d(TAG,"背完了");
                learnFinish();
                return;
            }
            wordInfo=MyTools.nameMemToWord(tuple);
            if (wordInfo!=null){
                Log.d(TAG,"赋值");
                TempMsg.WordLearn = MyTools.nameMemToWord(tuple);//把第一个词赋给TempMsg.WordLearn
                break;
            }
            Log.d(TAG,"下一个是null");
        }
        //TempMsg.WordLearn=MyTools.nameMemToWord(currentQueue.remove());//取一个词
        refreshView();
    }

    private boolean isQueueEmpty(){
        if (queue0.size()==0&&queue1.size()==0&&queue2.size()==0){
            return true;
        }else return false;
    }

    private void opWell() {
        Log.d(TAG,"点击了熟知");
        TempMsg.WordLearn.setWord_operation(1);//不再出现
        //wordDao.insertOneWord(TempMsg.WordLearn);
        updateToDB();
        refreshData();
    }

    private void opKnow() {
        Log.d(TAG,"点击了认识");
        int memory = TempMsg.WordLearn.getMemory();
        memory++;
        TempMsg.WordLearn.setMemory(memory);
        //wordDao.insertOneWord(TempMsg.WordLearn);
        updateToDB();
        addWordToQueue(TempMsg.WordLearn);
        turnToDetail(TempMsg.WordLearn.getName());
        //延时一秒刷新，避免切换activity动画过程中，单词刷新
        new Handler().postDelayed(mRefresh,delayToDetail);
    }

    private void opAmbiguous() {
        Log.d(TAG,"点击了模糊");
        //memory不改变
        updateToDB();
        addWordToQueue(TempMsg.WordLearn);
        turnToDetail(TempMsg.WordLearn.getName());
        new Handler().postDelayed(mRefresh,delayToDetail);

    }

    private void opUnKnow(){
        Log.d(TAG,"点击了不认识");
        int memory = TempMsg.WordLearn.getMemory();
        memory=(memory==0)?0:--memory;
        TempMsg.WordLearn.setMemory(memory);
        //wordDao.insertOneWord(TempMsg.WordLearn);
        updateToDB();
        addWordToQueue(TempMsg.WordLearn);
        turnToDetail(TempMsg.WordLearn.getName());
        new Handler().postDelayed(mRefresh,delayToDetail);
    }

    private void updateToDB(){
        Log.d(TAG,"尝试更新到数据库");
        WordInfo word=wordDao.getWordByName(TempMsg.WordLearn.getName());
        if (word==null){
            Log.d(TAG,"数据库中不存在，插入");
            TempMsg.WordLearn.setTime_stamp(MyTools.getCurrentTimeMillis());
            wordDao.insertOneWord(TempMsg.WordLearn);
        }else {
            Log.d(TAG,"数据库中存在，更新");
            wordDao.updateWord(TempMsg.WordLearn);
        }

    }

    private void turnToDetail(String name) {
        Intent intent=new Intent(getActivity(), WordDetailActivity.class);
        intent.putExtra("name",name);
        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(getActivity(),tvWord,"WORD");
        startActivity(intent,options.toBundle());
    }

    private Runnable mRefresh=new Runnable() {
        @Override
        public void run() {
            refreshData();
        }
    };


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

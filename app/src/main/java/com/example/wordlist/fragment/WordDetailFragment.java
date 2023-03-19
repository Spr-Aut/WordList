package com.example.wordlist.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.activity.WordListActivity;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WordDetailFragment extends Fragment {
    private static final String TAG = "WordDetailFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();
    private TextView tvName;//单词名
    private TextView tvSymbolUk;//英式音标
    private TextView tvSymbolUs;//美式音标
    private TextView tvDesc;//释义
    private TextView tvSentence;//例句
    private LinearLayout placeHolder;//顶部占70dp高度
    private FloatingActionButton btnSound;
    private FloatingActionButton btnFavorite;
    private FloatingActionButton btnContinue;
    private MyBroadcastReceiver broadcastReceiver;

    private String wordName="";
    private boolean isFromIntent=false;
    private WordInfo wordInfo;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_word_detail, container, false);
        Log.d(TAG,"创建");

        SharedPreferences userData=mContext.getSharedPreferences("UserData",Context.MODE_PRIVATE);
        TempMsg.setIsUk(userData.getBoolean("isUk",true));


        judgeSource();
        bindView();


        return mView;
    }
    private void bindView(){
        tvName=mView.findViewById(R.id.tv_detail_name);
        tvSymbolUk=mView.findViewById(R.id.tv_detail_symbol_uk);
        tvSymbolUs=mView.findViewById(R.id.tv_detail_symbol_us);
        tvDesc=mView.findViewById(R.id.tv_detail_desc);
        tvSentence=mView.findViewById(R.id.tv_detail_sentence);
        btnSound=mView.findViewById(R.id.btn_sound);
        btnFavorite=mView.findViewById(R.id.btn_favorite);
        btnContinue=mView.findViewById(R.id.btn_continue);
        placeHolder=mView.findViewById(R.id.placeholder_detail_top);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"启动");
        broadcastReceiver=new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter(BroadcastName.WORD_DETAIL_REFRESH);
        mContext.registerReceiver(broadcastReceiver,filter);
        judgeSource();
        refreshView();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"继续");
        MyTools.timeStart();
        judgeSource();
        refreshView();
        MyTools.timeEnd(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"暂停");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"停止");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Log.d(TAG,"切换可视状态");
        }else {
            Log.d(TAG,"不切换可视状态");
        }
    }

    /*判断数据来源*/
    private void judgeSource(){
        //来自Intent的话，不管广播和nameTemp
        Log.d(TAG,"当前的intent为："+getActivity().getIntent());
        String nameIntent = getActivity().getIntent().getStringExtra("name");//Intent
        Log.d(TAG,"当前的intent.string为："+nameIntent);
        //nameBroad在MyBroadCast里赋值了
        String nameTemp=TempMsg.WordInfo.getName();

        if (nameIntent!=null&&nameIntent.length()!=0){//来自Intent，不管广播和nameTemp
            wordName=nameIntent;
            Log.d(TAG,"从Intent启动："+nameIntent);
            isFromIntent=true;
        }else{
            wordName=nameTemp;
            isFromIntent=false;
        }
    }


    private void refreshView(){
        if (tvName.getText().equals(wordName)){
            Log.d(TAG,"无需刷新");
        }else {//刷新
            /*if (wordInfo.getName()==null&&wordInfo.getName().length()==0){
                return;
            }*/
            if (isFromIntent){
                wordInfo= wordDao.getWordByName(wordName);
                btnContinue.setVisibility(View.VISIBLE);
                btnContinue.setOnClickListener(v -> {
                    MyTools.shake(getActivity());
                    getActivity().finish();
                });
                placeHolder.setVisibility(View.GONE);

            }else {
                wordInfo=TempMsg.WordInfo;
                btnContinue.setVisibility(View.GONE);
                placeHolder.setVisibility(View.VISIBLE);
            }

            if (wordInfo.getName()!=null&&wordInfo.getName().length()!=0&&wordDao.getWordByName(wordInfo.getName())!=null){
                btnFavorite.setImageResource(R.drawable.icon_star_fill);
            }else {
                btnFavorite.setImageResource(R.drawable.icon_star);
            }

            tvName.setText(wordInfo.getName());
            tvSymbolUk.setText(wordInfo.getSymbol_uk());
            tvSymbolUs.setText(wordInfo.getSymbol_us());
            tvSentence.setText(wordInfo.getSentence());
            tvDesc.setText(wordInfo.getDesc());
            Log.d(TAG,"刷新Detail");
            //Log.d(TAG,wordInfo.getSentence());

            tvSymbolUk.setOnClickListener(v -> {
                MyTools.shake(getActivity());
                playSound(wordInfo.getSound_uk());
            });
            tvSymbolUs.setOnClickListener(v -> {
                MyTools.shake(getActivity());
                playSound(wordInfo.getSound_us());
            });

            btnSound.setOnClickListener(v -> {
                MyTools.shake(getActivity());
                if (TempMsg.isIsUk()){
                    playSound(wordInfo.getSound_uk());//播放英式
                }else {
                    playSound(wordInfo.getSound_us());//播放美式
                }

            });
            btnFavorite.setOnClickListener(v -> {
                MyTools.shake(getActivity());
                if (wordInfo.getName()!=null&&wordInfo.getName()!=""){
                    if (wordDao.getWordByName(wordInfo.getName())!=null){
                        wordDao.deleteWord(wordInfo);
                        btnFavorite.setImageResource(R.drawable.icon_star);
                        MyTools.showMsg("取消收藏"+wordInfo.getName(),mContext);
                    }else {
                        addToDao();
                        btnFavorite.setImageResource(R.drawable.icon_star_fill);
                        MyTools.showMsg("收藏"+wordInfo.getName(),mContext);
                    }

                }
            });
            /*btnContinue.setOnClickListener(v -> {
                Intent intent=new Intent(getActivity(), WordListActivity.class);
                startActivity(intent);
            });*/


        }
    }

    private void addToDao() {
        wordInfo.setTime_stamp(MyTools.getCurrentTimeMillis());
        wordInfo.setFavorite(1);
        wordDao.insertOneWord(wordInfo);

        Log.d(TAG,"添加单词"+wordInfo.getName());
    }

    private void playSound(String soundRul) {
        if (soundRul!=null&&soundRul.length()!=0){
            Log.d(TAG, "点击播放声音");
            //String sound="https://res.iciba.com/resource/amp3/oxford/0/6f/0a/6f0a0924a725e59aa0104109317cfa09.mp3";
            Log.d(TAG,"soundUrl:   "+soundRul);
            MediaPlayer mediaPlayer = MediaPlayer.create(mContext, Uri.parse(soundRul));
            mediaPlayer.start();
        }else {
            Log.d(TAG,"声音获取失败");
            MyTools.showMsg("声音获取失败",mContext);
        }
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"收到广播");
            if (intent!=null&&intent.getAction().equals(BroadcastName.WORD_DETAIL_REFRESH)){
                wordName=intent.getStringExtra("name");
                Log.d(TAG,"收到广播，name="+wordName);
                refreshView();//暂时去掉，因为现在访问了WordList再清空translate的etOrigin，会导致闪退
            }


        }
    }


}

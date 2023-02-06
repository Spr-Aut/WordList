package com.example.wordlist.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class WordDetailFragmentOld extends Fragment {
    private static final String TAG = "WordDetailFragmentOld";
    private boolean isFromIntent=false;
    private String name="";//用于来自Intent的刷新
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();

    private TextView tvName;//单词名
    private TextView tvSymbolUk;//英式音标
    private TextView tvSymbolUs;//美式音标
    private TextView tvDesc;//释义
    private TextView tvSentence;//例句
    private MyBroadcastReceiver broadcastReceiver;
    private ImageButton btnSound;
    private ImageButton btnFavorite;
    private Button btnContinue;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_word_detail, container, false);

        String name = getActivity().getIntent().getStringExtra("name");

        if (name!=null&&name.length()!=0){
            isFromIntent=true;
            Log.d(TAG,"来自intent,name="+name);
            this.name=name;
            //refresh(name);//写在onResume里了
        }else isFromIntent=false;

        tvName=mView.findViewById(R.id.tv_detail_name);
        tvSymbolUk=mView.findViewById(R.id.tv_detail_symbol_uk);
        tvSymbolUs=mView.findViewById(R.id.tv_detail_symbol_us);
        tvDesc=mView.findViewById(R.id.tv_detail_desc);
        tvSentence=mView.findViewById(R.id.tv_detail_sentence);
        btnSound=mView.findViewById(R.id.btn_sound);
        btnFavorite=mView.findViewById(R.id.btn_favorite);
        btnContinue=mView.findViewById(R.id.btn_continue);

        btnSound.setOnClickListener(v -> {
            playSound(TempMsg.WordInfo.getSound_uk());//默认播放英式
        });
        btnFavorite.setOnClickListener(v -> {
            if (TempMsg.WordInfo.getName()!=null&&TempMsg.WordInfo.getName()!=""){
                addToDao();
            }

        });
        btnContinue.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(),WordListActivity.class);
            startActivity(intent);
        });


        return mView;
    }

    /*判断当前数据来源*/
    private void judgeSource(){

    }

    private void addToDao() {
        TempMsg.WordInfo.setTime_stamp(MyTools.getCurrentTimeMillis());
        TempMsg.WordInfo.setFavorite(1);
        wordDao.insertOneWord(TempMsg.WordInfo);

        Log.d(TAG,"添加单词"+TempMsg.WordInfo.getName());
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


    private void refresh(String name) {//判断当前name和传来的name是否一样，不一样则刷新

         if (tvName.getText().equals(name)){
            Log.d(TAG,"无需刷新,name="+name);
        }else {
            tvName.setText(TempMsg.WordInfo.getName());
            tvSymbolUk.setText(TempMsg.WordInfo.getSymbol_uk());
            tvSymbolUs.setText(TempMsg.WordInfo.getSymbol_us());
            tvDesc.setText(TempMsg.WordInfo.getDesc());
            tvSentence.setText(TempMsg.WordInfo.getSentence());

            Log.d(TAG,"刷新当前Name为"+TempMsg.WordInfo.getName());
        }
    }

    private void refreshFromIntent(){
        WordInfo wordInfo=wordDao.getWordByName(name);
        tvName.setText(wordInfo.getName());
        tvSymbolUk.setText(wordInfo.getSymbol_uk());
        tvSymbolUs.setText(wordInfo.getSymbol_us());
        tvDesc.setText((wordInfo.getDesc()));
        tvSentence.setText(wordInfo.getSentence());
        Log.d(TAG,"刷新当前Name为"+wordInfo.getName());
    }

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver=new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter(BroadcastName.WORD_DETAIL_REFRESH);
        mContext.registerReceiver(broadcastReceiver,filter);
        Log.d(TAG,"启动");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"继续");

        if (isFromIntent){
            refreshFromIntent();
        }else {
            refresh(TempMsg.WordInfo.getName());
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(broadcastReceiver);
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

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null&&intent.getAction().equals(BroadcastName.WORD_DETAIL_REFRESH)){

                Log.d(TAG,"收到广播");
                String name=intent.getStringExtra("name");
                refresh(name);
            }
        }
    }

}

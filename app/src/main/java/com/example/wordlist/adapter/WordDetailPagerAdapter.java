package com.example.wordlist.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;

import java.util.List;

public class WordDetailPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG="WordDetailPagerAdapter";
    private Context mContext;
    private List<WordInfo> mWordList;
    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();

    public WordDetailPagerAdapter(Context context,List<WordInfo> wordList){
        mContext=context;
        mWordList=wordList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharedPreferences userData=mContext.getSharedPreferences("UserData",Context.MODE_PRIVATE);
        TempMsg.setIsUk(userData.getBoolean("isUk",true));//默认为英式音标
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_word_detail,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder=(ItemHolder) holder;
        itemHolder.tvNameWordDetailPager.setText(mWordList.get(position).getName());
        itemHolder.tvSymbolWordDetailPager.setText(mWordList.get(position).getName());
        if (TempMsg.isIsUk()){
            itemHolder.tvSymbolWordDetailPager.setText(mWordList.get(position).getSymbol_uk());
            itemHolder.tvSymbolWordDetailPager.setOnClickListener(v -> {
                playSound(mWordList.get(position).getSound_uk());
            });
        }else {
            itemHolder.tvSymbolWordDetailPager.setText(mWordList.get(position).getSymbol_us());
            itemHolder.tvSymbolWordDetailPager.setOnClickListener(v -> {
                playSound(mWordList.get(position).getSound_us());
            });
        }
        itemHolder.tvDescWordDetailPager.setText(mWordList.get(position).getDesc());
        itemHolder.tvSentenceWordDetailPager.setText(mWordList.get(position).getSentence());
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView tvNameWordDetailPager;
        public TextView tvSymbolWordDetailPager;
        public TextView tvDescWordDetailPager;
        public TextView tvSentenceWordDetailPager;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvNameWordDetailPager=itemView.findViewById(R.id.tvNameWordDetailPager);
            tvSymbolWordDetailPager=itemView.findViewById(R.id.tvSymbolWordDetailPager);
            tvDescWordDetailPager=itemView.findViewById(R.id.tvDescWordDetailPager);
            tvSentenceWordDetailPager=itemView.findViewById(R.id.tvSentenceWordDetailPager);
        }
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
}

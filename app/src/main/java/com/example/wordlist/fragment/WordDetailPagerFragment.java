package com.example.wordlist.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wordlist.Anim.DepthFieldTransformer;
import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.adapter.WordDetailPagerAdapter;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.http.POST;

public class WordDetailPagerFragment extends Fragment {
    private static final String TAG = "WordDetailPagerFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private ViewPager2 viewPager2;
    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();
    private int paddingHorizontal=40;
    private FloatingActionButton btnSoundPager;
    private FloatingActionButton btnFavoritePager;
    private List<WordInfo> mWordList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"执行onCreateView");
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_word_detail_pager, container, false);

        mWordList=wordDao.getAllWord();

        viewPager2=mView.findViewById(R.id.vpWordDetail);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setPageTransformer(new DepthFieldTransformer());
        WordDetailPagerAdapter adapter=new WordDetailPagerAdapter(mContext,mWordList);
        viewPager2.setAdapter(adapter);
        RecyclerView view=(RecyclerView)viewPager2.getChildAt(0);
        view.setPadding(MyTools.dp2px(mContext,paddingHorizontal),0,MyTools.dp2px(mContext,paddingHorizontal),0);
        view.setClipToPadding(false);

        btnSoundPager=mView.findViewById(R.id.btnSoundPager);
        btnFavoritePager=mView.findViewById(R.id.btnFavoritePager);
        btnSoundPager.setOnClickListener(v -> {
            MyTools.shake(getActivity());
            clickSound();
        });
        btnFavoritePager.setOnClickListener(v -> {
            MyTools.shake(getActivity());
            clickFavorite();
        });

        return mView;

    }

    private void clickFavorite() {
        int position = viewPager2.getCurrentItem();
        WordInfo word = mWordList.get(position);
        Log.d(TAG,"当前单词为"+word.getName());
        wordDao.deleteWord(word);
        mWordList.remove(position);
        viewPager2.getAdapter().notifyItemRemoved(position);//显示remove动画
        viewPager2.getAdapter().notifyItemRangeChanged(position,mWordList.size()-position);//删除后刷新被删的Item之后的Item，不要用notifyItemRangeRemoved，不然后面的Item会闪一下

    }

    private void clickSound() {
        int position = viewPager2.getCurrentItem();
        Log.d(TAG,"当前单词为"+mWordList.get(position).getName());
        if (TempMsg.isIsUk()){
            playSound(mWordList.get(position).getSound_uk());//播放英式
        }else {
            playSound(mWordList.get(position).getSound_us());//播放美式
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
            Log.d(TAG,"声音获取失败");
            MyTools.showMsg("声音获取失败",mContext);
        }
    }
}

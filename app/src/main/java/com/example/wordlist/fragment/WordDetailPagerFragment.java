package com.example.wordlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.wordlist.util.MyTools;

public class WordDetailPagerFragment extends Fragment {
    private static final String TAG = "WordDetailPagerFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private ViewPager2 viewPager2;
    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();
    private int paddingHorizontal=40;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_word_detail_pager, container, false);

        viewPager2=mView.findViewById(R.id.vpWordDetail);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setPageTransformer(new DepthFieldTransformer());
        WordDetailPagerAdapter adapter=new WordDetailPagerAdapter(mContext,wordDao.getAllWord());
        viewPager2.setAdapter(adapter);
        RecyclerView view=(RecyclerView)viewPager2.getChildAt(0);
        view.setPadding(MyTools.dp2px(mContext,paddingHorizontal),0,MyTools.dp2px(mContext,paddingHorizontal),0);
        view.setClipToPadding(false);

        Log.d(TAG,"执行onCreateView");



        return mView;

    }
}

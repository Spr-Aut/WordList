package com.example.wordlist.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wordlist.fragment.TranslateFragment;
import com.example.wordlist.fragment.WordDetailFragment;

import java.util.List;

public class WordClassPagerAdapter extends FragmentStateAdapter {
    private List<String> mTitleList; // 声明一个标题文字列表

    // 碎片页适配器的构造方法，传入碎片管理器与标题列表
    public WordClassPagerAdapter(FragmentActivity fa, List<String> titleList) {
        super(fa);
        mTitleList = titleList;
    }

    // 创建指定位置的碎片Fragmen
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) { // 第一页展示翻译
            return new TranslateFragment();
        } else { // 第二页展示单词本
            return new WordDetailFragment();
        }
    }

    // 获取碎片Fragment的个数
    @Override
    public int getItemCount() {
        return mTitleList.size();
    }
}

package com.example.wordlist.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.wordlist.fragment.ReviewFragment;
import com.example.wordlist.fragment.WordListFragment;
import com.example.wordlist.fragment.SettingsFragment;


public class MainPagerAdapter extends FragmentPagerAdapter {

    // 碎片页适配器的构造方法，传入碎片管理器
    public MainPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    // 获取指定位置的碎片Fragment
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ReviewFragment();  // 返回第一个碎片
        } else if (position == 1) {
            return new WordListFragment();  // 返回第二个碎片
        } else if (position == 2) {
            return new SettingsFragment();  // 返回第三个碎片
        } else {
            return null;
        }
    }

    // 获取碎片Fragment的个数
    @Override
    public int getCount() {
        return 3;
    }
}

package com.example.wordlist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.wordlist.R;
import com.example.wordlist.adapter.MainPagerAdapter;
import com.example.wordlist.util.MyTools;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setStatusBar();
        MyTools.setStatusBar(this);

        radioGroup=findViewById(R.id.rg_tabbar);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            for(int pos=0;pos<radioGroup.getChildCount();pos++){
                RadioButton radioButton=(RadioButton) radioGroup.getChildAt(pos);
                if (radioButton.getId()==checkedId){
                    viewPager.setCurrentItem(pos);
                }
            }
        });

        //创建一个翻页适配器
        MainPagerAdapter adapter=new MainPagerAdapter(getSupportFragmentManager());
        viewPager=findViewById(R.id.vp_content);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                radioGroup.check(radioGroup.getChildAt(position).getId());
            }
        });
        //viewPager.setCurrentItem(1);//设置默认页面为第二个
        viewPager.setCurrentItem(1,true);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
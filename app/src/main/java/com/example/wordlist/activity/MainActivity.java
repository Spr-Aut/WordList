package com.example.wordlist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.wordlist.R;
import com.example.wordlist.adapter.MainPagerAdapter;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    MenuItem navCall;
    NavigationView navView;
    private Dialog dialog;

    @SuppressLint("MissingInflatedId")
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

        initView();
    }

    private void initView() {
        /*设置侧滑navigationView的点击事件*/
        navView=findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navCall -> createDialog();
                }
                return true;
            }
        });

    }

    private void createDialog(){
        if(dialog==null){
            Log.d(TAG,"显示弹窗");
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_num_edit,null);
            dialog=new Dialog(MainActivity.this);
            EditText etNum=view.findViewById(R.id.etNum);
            Button btnNumConfirm=view.findViewById(R.id.btnNumConfirm);
            Button btnNumCancel=view.findViewById(R.id.btnNumCancel);
            btnNumConfirm.setOnClickListener(v -> {
                int num= Integer.parseInt(etNum.getText().toString());
                Log.d(TAG,"输入了"+num);
                if (updateLearnNum(num)){
                    MyTools.showMsg("每日学习数设为"+num+"个",MainActivity.this);
                }
                dialog.dismiss();
            });
            btnNumCancel.setOnClickListener(v -> dialog.dismiss());
            dialog.setContentView(view);
        }
        dialog.show();
    }

    private boolean updateLearnNum(int num){
        int learnNum=num;//每日学习的单词数
        int temp=learnNum/3;
        int maxCount=temp<5?temp:5;//背几个换队列（最多5个）
        SharedPreferences shared=getSharedPreferences("Num",MODE_PRIVATE);
        SharedPreferences.Editor editor=shared.edit();
        editor.putInt("learnNum",learnNum);
        editor.putInt("maxCount",maxCount);
        editor.commit();
        setLearnNum(learnNum,maxCount);
        Log.d(TAG,"learnNum:"+learnNum+"maxCount:"+maxCount);
        return true;
    }
    private void setLearnNum(int learnNum,int maxCount){//给TempMsg设置
        TempMsg.setLearnNum(learnNum);
        TempMsg.setMaxCount(maxCount);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
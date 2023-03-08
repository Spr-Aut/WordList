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
import android.view.Menu;
import android.view.MenuInflater;
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
    MenuItem navNumEdit;
    MenuItem navSymbolEdit;
    NavigationView navView;

    private Dialog dialogNum;
    private Dialog dialogSymbol;
    SharedPreferences userData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setStatusBar();
        MyTools.setStatusBar(this);
        userData=getSharedPreferences("UserData",MODE_PRIVATE);

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
        navNumEdit=navView.getMenu().findItem(R.id.navNumEdit);
        navSymbolEdit=navView.getMenu().findItem(R.id.navSymbolEdit);
        navNumEdit.setTitle("每日学习数："+userData.getInt("learnNum",5));
        if (userData.getBoolean("isUk",true)){
            navSymbolEdit.setTitle("默认发音：英式");
        }else {
            navSymbolEdit.setTitle("默认发音：美式");
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navNumEdit -> createDialogNum();
                    case R.id.navSymbolEdit -> createDialogSymbol();
                }
                return true;
            }
        });

    }

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

    private void createDialogNum(){//设置单词学习数
        if(dialogNum==null){
            Log.d(TAG,"弹窗：设置学习数");
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_num_edit,null);
            dialogNum=new Dialog(MainActivity.this);
            EditText etNum=view.findViewById(R.id.etNum);
            Button btnNumConfirm=view.findViewById(R.id.btnNumConfirm);
            Button btnNumCancel=view.findViewById(R.id.btnNumCancel);
            btnNumConfirm.setOnClickListener(v -> {
                int num= Integer.parseInt(etNum.getText().toString());
                Log.d(TAG,"输入了"+num);
                if (updateLearnNum(num)){
                    MyTools.showMsg("每日学习数设为"+num+"个",MainActivity.this);
                }
                dialogNum.dismiss();
            });
            btnNumCancel.setOnClickListener(v -> dialogNum.dismiss());
            dialogNum.setContentView(view);
        }
        dialogNum.show();
    }
    private void createDialogSymbol(){//设置音标
        if(dialogSymbol==null){
            Log.d(TAG,"弹窗：设置音标");
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_symbol_edit,null);
            dialogSymbol=new Dialog(MainActivity.this);
            Button btnNumConfirm=view.findViewById(R.id.btnUk);
            Button btnNumCancel=view.findViewById(R.id.btnUs);
            btnNumConfirm.setOnClickListener(v -> {
                updateSymbol(true);
                dialogSymbol.dismiss();
            });
            btnNumCancel.setOnClickListener(v -> {
                updateSymbol(false);
                dialogSymbol.dismiss();
            });
            dialogSymbol.setContentView(view);
        }
        dialogSymbol.show();
    }

    private void updateSymbol(Boolean isUk) {

        SharedPreferences.Editor edit = userData.edit();
        edit.putBoolean("isUk",isUk);
        edit.commit();
        TempMsg.setIsUk(isUk);
        if (isUk){
            Log.d(TAG,"选择了英式");
            MyTools.showMsg("切换为英式",MainActivity.this);
            navSymbolEdit.setTitle("默认发音：英式");
        }else {
            Log.d(TAG,"选择了美式");
            MyTools.showMsg("切换为美式",MainActivity.this);
            navSymbolEdit.setTitle("默认发音：美式");
        }

    }

    private boolean updateLearnNum(int num){
        int learnNum=num;//每日学习的单词数
        int temp=learnNum/3;
        int maxCount=temp<5?temp:5;//背几个换队列（最多5个）

        SharedPreferences.Editor editor=userData.edit();
        editor.putInt("learnNum",learnNum);
        editor.putInt("maxCount",maxCount);
        editor.commit();

        TempMsg.setLearnNum(learnNum);
        TempMsg.setMaxCount(maxCount);

        navNumEdit.setTitle("每日学习数："+num);//更新navNumEdit

        Log.d(TAG,"learnNum:"+learnNum+"maxCount:"+maxCount);
        return true;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
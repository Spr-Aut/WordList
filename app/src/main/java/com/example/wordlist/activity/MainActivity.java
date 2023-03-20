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
import com.example.wordlist.fragment.WordDetailPagerFragment;
import com.example.wordlist.fragment.WordListFragment;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    MenuItem navNumEdit;
    MenuItem navSymbolEdit;
    MenuItem navListOrCard;
    MenuItem navShake;
    MenuItem navListAnim;
    NavigationView navView;

    private Dialog dialogNum;
    private Dialog dialogSymbol;
    private Dialog dialogListOrCard;
    private Dialog dialogShake;
    private Dialog dialogListAnim;
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
        navListOrCard=navView.getMenu().findItem(R.id.navListOrCard);
        navShake=navView.getMenu().findItem(R.id.navShake);
        navListAnim=navView.getMenu().findItem(R.id.navListAnim);

        navNumEdit.setTitle("每日学习数："+userData.getInt("learnNum",5));
        if (userData.getBoolean("isUk",true)){
            navSymbolEdit.setTitle("默认发音：英式");
            TempMsg.setIsUk(true);
        }else {
            navSymbolEdit.setTitle("默认发音：美式");
            TempMsg.setIsUk(false);
        }
        if (userData.getBoolean("doShake",true)){
            navShake.setTitle("震动：开");
            MyTools.setDoShake(true);
        }else {
            navShake.setTitle("震动：关");
            MyTools.setDoShake(false);
        }
        if(userData.getBoolean("useListAnim",true)){
            navListAnim.setTitle("列表动画：开");
            TempMsg.setUseListAnim(true);
        }else {
            navListAnim.setTitle("列表动画：关");
            TempMsg.setUseListAnim(false);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navNumEdit -> createDialogNum();
                    case R.id.navSymbolEdit -> createDialogSymbol();
                    case R.id.navListOrCard -> createDialogListOrCard();
                    case R.id.navShake -> createDialogShake();
                    case R.id.navListAnim -> createDialogListAnim();
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
        if(userData.getBoolean("isList",true)){
            navListOrCard.setTitle("单词本：列表式");
        }else {
            navListOrCard.setTitle("单词本：卡片式");
        }
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

            //dialog背景透明，否则四个角不正常
            dialogNum.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams=dialogNum.getWindow().getAttributes();
            layoutParams.dimAmount=0.0f;
            dialogNum.getWindow().setAttributes(layoutParams);

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

            //dialog背景透明，否则四个角不正常
            dialogSymbol.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams=dialogSymbol.getWindow().getAttributes();
            layoutParams.dimAmount=0.0f;
            dialogSymbol.getWindow().setAttributes(layoutParams);

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
    private void createDialogListOrCard() {//设置单词本样式
        if(dialogListOrCard==null){
            Log.d(TAG,"弹窗：设置单词本样式");
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_list_card_edit,null);
            dialogListOrCard=new Dialog(MainActivity.this);

            //dialog背景透明，否则四个角不正常
            dialogListOrCard.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams=dialogListOrCard.getWindow().getAttributes();
            layoutParams.dimAmount=0.0f;
            dialogListOrCard.getWindow().setAttributes(layoutParams);

            boolean isList = userData.getBoolean("isList", true);
            RadioGroup group=view.findViewById(R.id.rgWordListDialog);
            if (isList){
                group.check(R.id.rbListDialog);
            }else {
                group.check(R.id.rbCardDialog);
            }

            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId==R.id.rbListDialog){
                        updateWordListStyle(true);
                        dialogListOrCard.dismiss();
                    }else if(checkedId==R.id.rbCardDialog){
                        updateWordListStyle(false);
                        dialogListOrCard.dismiss();
                    }
                }
            });

            dialogListOrCard.setContentView(view);
        }
        dialogListOrCard.show();
    }

    private void createDialogShake() {//设置是否震动
        if(dialogShake==null){
            Log.d(TAG,"弹窗：设置是否震动");
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_shake,null);
            dialogShake=new Dialog(MainActivity.this);

            //dialog背景透明，否则四个角不正常
            dialogShake.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams=dialogShake.getWindow().getAttributes();
            layoutParams.dimAmount=0.0f;
            dialogShake.getWindow().setAttributes(layoutParams);

            Button btnDoShake=view.findViewById(R.id.btnDoShake);
            Button btnDoNotShake=view.findViewById(R.id.btnDoNotShake);
            btnDoShake.setOnClickListener(v -> {
                updateShake(true);
                dialogShake.dismiss();
            });
            btnDoNotShake.setOnClickListener(v -> {
                updateShake(false);
                dialogShake.dismiss();
            });

            dialogShake.setContentView(view);
        }
        dialogShake.show();
    }

    private void createDialogListAnim() {
        if(dialogListAnim==null){
            Log.d(TAG,"弹窗：设置是否启用列表动画");
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_list_anim,null);
            dialogListAnim=new Dialog(MainActivity.this);

            //dialog背景透明，否则四个角不正常
            dialogListAnim.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams=dialogListAnim.getWindow().getAttributes();
            layoutParams.dimAmount=0.0f;
            dialogListAnim.getWindow().setAttributes(layoutParams);

            Button btnUseListAnim=view.findViewById(R.id.btnUseListAnim);
            Button btnNoListAnim=view.findViewById(R.id.btnNoListAnim);
            btnUseListAnim.setOnClickListener(v -> {
                updateListAnim(true);
                dialogListAnim.dismiss();
            });
            btnNoListAnim.setOnClickListener(v -> {
                updateListAnim(false);
                dialogListAnim.dismiss();
            });

            dialogListAnim.setContentView(view);
        }
        dialogListAnim.show();
    }

    private void updateListAnim(boolean useListAnim) {
        SharedPreferences.Editor edit=userData.edit();
        edit.putBoolean("useListAnim",useListAnim);
        edit.commit();
        TempMsg.setUseListAnim(useListAnim);
        if(useListAnim){
            MyTools.showMsg("启用列表动画",this);
            navListAnim.setTitle("列表动画：开");
        }else {
            MyTools.showMsg("关闭列表动画",this);
            navListAnim.setTitle("列表动画：关");
        }

    }

    private void updateShake(Boolean doShake) {
        SharedPreferences.Editor edit = userData.edit();
        edit.putBoolean("doShake",doShake);
        edit.commit();
        MyTools.setDoShake(doShake);
        if(doShake){
            MyTools.showMsg("开启震动",this);
            navShake.setTitle("震动：开");
        }else {
            MyTools.showMsg("关闭震动",this);
            navShake.setTitle("震动：关");
        }
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

    private void updateWordListStyle(Boolean isList){
        if (isList){
            navListOrCard.setTitle("单词本：列表式");
            MyTools.showMsg("单词本设为列表式",this);
            SharedPreferences.Editor edit = userData.edit();
            edit.putBoolean("isList",true).commit();
        }else {
            navListOrCard.setTitle("单词本：卡片式");
            MyTools.showMsg("单词本设为卡片式",this);
            SharedPreferences.Editor edit = userData.edit();
            edit.putBoolean("isList",false).commit();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
package com.example.wordlist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.wordlist.R;
import com.example.wordlist.fragment.Test2Fragment;
import com.example.wordlist.fragment.TestFragment;
import com.example.wordlist.fragment.WordDetailPagerFragment;
import com.example.wordlist.fragment.WordListFragment;
import com.example.wordlist.util.MyTools;

public class WordListActivity extends AppCompatActivity {
    private static final String TAG = "WordListActivity";
    RelativeLayout rlTop;
    FragmentManager manager;
    RadioGroup radioGroup;
    Boolean isList;
    SharedPreferences userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        MyTools.setStatusBar(this);
        userData=getSharedPreferences("UserData",MODE_PRIVATE);
        isList=userData.getBoolean("isList",true);

        radioGroup=findViewById(R.id.rgWordList);

        manager=getSupportFragmentManager();
        if (isList){
            manager.beginTransaction().add(R.id.container_word_list,new WordListFragment()).commit();
            radioGroup.check(R.id.rbList);
        }else {
            manager.beginTransaction().add(R.id.container_word_list,new WordDetailPagerFragment()).commit();
            radioGroup.check(R.id.rbCard);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction=manager.beginTransaction();
                if (checkedId==R.id.rbList){
                    transaction.replace(R.id.container_word_list,new WordListFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                    SharedPreferences.Editor edit = userData.edit();
                    edit.putBoolean("isList",true).commit();
                }else if(checkedId==R.id.rbCard){
                    transaction.replace(R.id.container_word_list,new WordDetailPagerFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                    SharedPreferences.Editor edit = userData.edit();
                    edit.putBoolean("isList",false).commit();
                }
            }
        });

        /*rlTop=findViewById(R.id.bar_word_list_top);
        rlTop.setOnClickListener(v -> switchFrag());*/

    }

    private void switchFrag(){
        Log.d(TAG,"切换Fragment ");
        FragmentTransaction transaction=manager.beginTransaction();

        if(true){
            transaction.replace(R.id.container_word_list,new WordListFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }else {
            transaction.replace(R.id.container_word_list,new WordDetailPagerFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }
    }
}
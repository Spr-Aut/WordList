package com.example.wordlist.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordlist.R;
import com.example.wordlist.fragment.WordDetailFragment;
import com.example.wordlist.util.MyTools;

public class WidgetWordDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        MyTools.setStatusBar(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_word_detail,new WordDetailFragment())
                .commit();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
package com.example.wordlist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.wordlist.R;
import com.example.wordlist.fragment.WordDetailFragment;
import com.example.wordlist.util.MyTools;

public class WordDetailActivity extends AppCompatActivity {

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
    public void onBackPressed() {
        finish();
    }
}
package com.example.wordlist.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordlist.R;
import com.example.wordlist.fragment.WordDetailFragment;
import com.example.wordlist.fragment.WordDetailPagerFragment;
import com.example.wordlist.util.MyTools;

public class WordDetailPagerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        MyTools.setStatusBar(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_word_detail,new WordDetailPagerFragment())
                .commit();


    }
}

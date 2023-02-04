package com.example.wordlist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.wordlist.R;
import com.example.wordlist.fragment.WordListFragment;
import com.example.wordlist.util.MyTools;

public class WordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        MyTools.setStatusBar(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container_word_list,new WordListFragment())
                .commit();
    }
}
package com.example.wordlist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import com.example.wordlist.R;
import com.example.wordlist.adapter.SlideRecyclerViewAdapter;
import com.example.wordlist.customview.SlideRecyclerView;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.database.WordDatabase;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.tuple.WordNameTransTuple;
import com.example.wordlist.util.MyTools;

import java.util.ArrayList;
import java.util.List;

public class WordListDownloadActivity extends AppCompatActivity {
    private final static String TAG="WordListDownloadActivity";
    private SlideRecyclerView recyclerView;
    private SlideRecyclerViewAdapter adapter;
    private List<WordNameTransTuple> mWordList;
    private WordDao wordDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_download);
        MyTools.setStatusBar(this);

        mWordList=new ArrayList<>();
        mWordList.add(new WordNameTransTuple("123","456"));
        recyclerView=findViewById(R.id.rvWordListDownload);
        adapter=new SlideRecyclerViewAdapter(this,mWordList);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.notifyDataSetChanged();

        initData();
    }

    private void initData() {
        String bookName = getIntent().getStringExtra("bookName");
        Log.d(TAG,"当前词书为"+bookName);
        wordDao= Room.databaseBuilder(this, WordDatabase.class, bookName).addMigrations().allowMainThreadQueries().build().wordDao();
        mWordList=wordDao.getWordList();
        Log.d(TAG,"wordDao:"+mWordList.get(0));
        adapter.refreshData(mWordList);
    }
}
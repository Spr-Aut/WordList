package com.example.wordlist;

import android.app.Application;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.room.Room;

import com.example.wordlist.dao.BookDao;
import com.example.wordlist.database.BookDatabase;
import com.example.wordlist.database.WordDatabase;

import java.util.HashMap;

public class MainApplication extends Application {
    private static MainApplication mainApplication;
    private BookDatabase bookDatabase;
    private WordDatabase wordDatabase;


    //获取当前应用的唯一实例
    public static MainApplication getInstance(){
        return mainApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mainApplication=this;

        //构建图书数据库的示例
        bookDatabase= Room.databaseBuilder(mainApplication,BookDatabase.class,"BookInfo").addMigrations().allowMainThreadQueries().build();
        wordDatabase=Room.databaseBuilder(mainApplication,WordDatabase.class,"WordInfo").addMigrations().allowMainThreadQueries().build();

    }

    public BookDatabase getBookDB(){
        return bookDatabase;
    }

    public WordDatabase getWordDB(){return wordDatabase;}

}

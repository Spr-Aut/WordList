package com.example.wordlist.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.XMLParse;
import com.example.wordlist.adapter.BookRecyclerViewAdapter;
import com.example.wordlist.bean.BookBean;
import com.example.wordlist.bean.WordBean;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.entity.BookInfo;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {
    StringBuilder url;
    private static final String TAG = "TestActivity";

    private RecyclerView rcBookList;
    private BookRecyclerViewAdapter bookAdapter;
    private Button btnGetBookList;
    private ArrayList<BookBean.BookDataBean> mBookList;
    private SharedPreferences userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        MyTools.setStatusBar(this);

        btnGetBookList=findViewById(R.id.btnGetBookList);
        btnGetBookList.setOnClickListener(v -> {
            MyTools.shake(this);
            getBookList();
        });
        rcBookList=findViewById(R.id.rcBookList);
        mBookList=new ArrayList<>();
        bookAdapter=new BookRecyclerViewAdapter(this,mBookList);
        userData=getSharedPreferences("UserData",MODE_PRIVATE);

        rcBookList.setAdapter(bookAdapter);
        RecyclerView.LayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcBookList.setLayoutManager(linearLayoutManager);
        bookAdapter.notifyDataSetChanged();

    }

    private void getBookList() {

        MyTools.timeStart();
        url=new StringBuilder();
        url.append("https://rw.ylapi.cn/reciteword/list.u?uid=12363&appkey=a8b7bf005ab94e9c38f2a514bc015669&name=");
        url.append("");

        new MyAsyncTask().execute(url.toString());


    }

    private void func(String resJson){
        BookBean bookBean=new Gson().fromJson(resJson,BookBean.class);
        mBookList=bookBean.getDatas();

        if (!userData.getBoolean("isBookListExist",false)){
            //把词书列表添加到数据库
            BookDao bookDao = MainApplication.getInstance().getBookDB().bookDao();
            for (BookBean.BookDataBean bookDataBean : mBookList) {
                bookDao.insertOneBook(new BookInfo(bookDataBean.getTitle(),false));
            }
        }

        bookAdapter.refreshData(mBookList);
        MyTools.timeEnd(TAG);
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {


        /**
         * 下载单词书列表
         */
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(params[0]).build();
            try {
                Response response = client.newCall(request).execute();
                // publishProgress(++i);
                return Objects.requireNonNull(response.body()).string();
            } catch (IOException e) {
                return e.getMessage();
            }

        }

        /**
         * 解析XML为TempMsg.WordInfo
         */
        @Override
        protected void onPostExecute(String params) {
            if (params != null && params.length() != 0) {
                func(params);
            }

        }
    }
}
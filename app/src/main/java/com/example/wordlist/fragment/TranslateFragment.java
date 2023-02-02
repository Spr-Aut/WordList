package com.example.wordlist.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.entity.BookInfo;

public class TranslateFragment extends Fragment {
    private static final String TAG = "TranslateFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    BookDao bookDao= MainApplication.getInstance().getBookDB().bookDao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_translate, container, false);

        Button btnDelete =mView.findViewById(R.id.btn_clear_translate);
        btnDelete.setOnClickListener(v -> {
            bookDao.deleteAllBook();
            notifyWord();
        } );

        Button btnAdd=mView.findViewById(R.id.btn_check_translate);
        btnAdd.setOnClickListener(v -> {
            BookInfo bookInfo=new BookInfo();
            bookInfo.setName("书");
            bookInfo.setPrice(100);
            bookDao.insertOneBook(bookInfo);
            notifyWord();
        });



        return mView;
    }
    private void notifyWord(){
        Intent intent=new Intent(BroadcastName.WORD_DETAIL_REFRESH);
        mContext.sendBroadcast(intent);
    }


}

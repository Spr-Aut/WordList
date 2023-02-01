package com.example.wordlist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.activity.MainActivity;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.entity.BookInfo;

import java.util.List;

public class WordListFragment extends Fragment {
    private static final String TAG = "TabSecondFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private TextView tvName,tvPrice;
    private BookDao bookDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_word_list, container, false);
        TextView tv_second = mView.findViewById(R.id.tv_second);
        tv_second.setText("我是分类页面");
        tvName=mView.findViewById(R.id.tv_name);
        tvPrice=mView.findViewById(R.id.tv_price);
        bookDao= MainApplication.getInstance().getBookDB().bookDao();

        setText();
        /*for (int i = 0; i < 5; i++) {
            method(i);
        }*/
        //method(6);

        return mView;
    }

    private void method(int count){
        BookInfo bookInfo=new BookInfo();
        bookInfo.setName("第"+count+"本书");
        bookInfo.setPrice(100);
        bookDao.insertOneBook(bookInfo);
        Toast.makeText(getActivity(),"插入一本书",Toast.LENGTH_SHORT).show();
    }

    private void setText(){
        BookInfo bookList = bookDao.getBookByName("第2本书"); // 获取所有书籍记录
        /*String desc = String.format("数据库查询到%d条记录，详情如下：", bookList.size());
        for (int i = 0; i < bookList.size(); i++) {
            BookInfo info = bookList.get(i);
            desc = String.format("%s\n第%d条记录信息如下：", desc, i + 1);

            desc = String.format("%s\n　价格为%f", desc, info.getPrice());
        }*/
        String desc=bookList.getName();

        tvName.setText(desc);
    }


}

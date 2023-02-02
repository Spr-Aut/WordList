package com.example.wordlist.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.entity.BookInfo;

import java.util.List;

public class WordListFragment extends Fragment {
    private static final String TAG = "WordListFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private TextView tv_second;
    private MyBroadcastReceiver broadcastReceiver;

    BookDao bookDao= MainApplication.getInstance().getBookDB().bookDao();
    List<BookInfo> allBook;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_word_list, container, false);
        tv_second = mView.findViewById(R.id.tv_word_list);

        tv_second.setText(getTag());

        Button btnTest=mView.findViewById(R.id.btn_test);
        btnTest.setOnClickListener(v -> Toast.makeText(mContext,"刷新",Toast.LENGTH_SHORT).show());


        refresh();





        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver=new MyBroadcastReceiver();
        IntentFilter filter=new IntentFilter(BroadcastName.WORD_DETAIL_REFRESH);
        mContext.registerReceiver(broadcastReceiver,filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

            refresh();
        }else {

        }
    }

    private void refresh() {
        Toast.makeText(mContext,"刷新",Toast.LENGTH_SHORT).show();
        allBook = bookDao.getAllBook();
        if (allBook.size()==0){
            tv_second.setText("没有书");
        }else {
            tv_second.setText(allBook.get(0).getName());
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null&&intent.getAction().equals(BroadcastName.WORD_DETAIL_REFRESH)){
                refresh();
            }
        }
    }


}

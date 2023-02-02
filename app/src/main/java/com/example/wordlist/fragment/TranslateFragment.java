package com.example.wordlist.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.XMLParse;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.MyTools;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TranslateFragment extends Fragment {
    private static final String TAG = "TranslateFragment";
    private final static int STATE_BEFORE_INPUT=0;//输入前
    private final static int STATE_DURING_INPUT=1;//输入中
    private final static int STATE_DURING_TRANS=2;//翻译中
    private final static int STATE_AFTER_TRANS=3;//翻译后
    private final static int STATE_AFTER_ADD=4;//添加后
    private final static int STATE_INIT=5;//初始状态
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    private Button btnTrans;//翻译
    private Button btnClear;//清空
    private Button btnAdd;//添加到单词本
    private EditText etOrigin;//原文
    private TextView tvResult;//译文

    BookDao bookDao= MainApplication.getInstance().getBookDB().bookDao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_translate, container, false);

        btnTrans=mView.findViewById(R.id.btn_trans_translate);
        btnClear=mView.findViewById(R.id.btn_clear_translate);
        btnAdd=mView.findViewById(R.id.btn_add_translate);
        etOrigin=mView.findViewById(R.id.et_origin_translate);
        tvResult=mView.findViewById(R.id.tv_trans_translate);

        btnTrans.setOnClickListener(v -> translate());
        btnClear.setOnClickListener(v -> clear());
        btnAdd.setOnClickListener(v -> add());

        return mView;
    }

    /*@Override
    public void onClick(View v) {
        if (btnTrans.equals(v)) {
            translate();
        } else if (btnClear.equals(v)) {
            clear();
        } else if (btnAdd.equals(v)) {
            add();
        }
    }*/

    private void add() {

    }

    private void clear() {

    }

    private void translate(){
        StringBuilder url=new StringBuilder();
        url.append("https://dict-co.iciba.com/api/dictionary.php?w=");
        url.append(etOrigin.getText());
        url.append("&key=0EAE08A016D6688F64AB3EBB2337BFB0");
        new MyAsyncTask().execute(url.toString());

        //addToDetail();//弃用，用notifyWord();

        notifyWord();

        //待做：添加到单词本

    }

    private void addToDetail() {

    }

    /**
     * 刷新WordDetailFragment
     * */
    private void notifyWord(){
        Intent intent=new Intent(BroadcastName.WORD_DETAIL_REFRESH);
        mContext.sendBroadcast(intent);
    }

    //控件状态切换
    private void stateSwitch(int state){
        switch (state){
            case STATE_BEFORE_INPUT:
                tvResult.setText("");
                btnTrans.setVisibility(View.VISIBLE);//显示翻译按钮
                btnClear.setVisibility(View.VISIBLE);//显示清除按钮
                btnAdd.setVisibility(View.GONE);//隐藏添加按钮
                break;
            case STATE_DURING_INPUT:
                btnTrans.setVisibility(View.VISIBLE);//显示翻译按钮
                btnClear.setVisibility(View.VISIBLE);//显示清除按钮
                btnAdd.setVisibility(View.GONE);//隐藏添加按钮
                break;
            case STATE_DURING_TRANS:
                btnTrans.setText("翻译中...");
                btnTrans.setEnabled(false);//不可更改，同样就无法点击
                btnTrans.setVisibility(View.VISIBLE);//显示翻译按钮
                btnClear.setVisibility(View.VISIBLE);//显示清除按钮
                btnAdd.setVisibility(View.GONE);//隐藏添加按钮
                break;
            case STATE_AFTER_TRANS:
                btnTrans.setText("翻译");
                btnTrans.setEnabled(true);
                btnTrans.setVisibility(View.GONE);//隐藏翻译按钮
                btnAdd.setVisibility(View.VISIBLE);//显示添加按钮
                btnClear.setVisibility(View.VISIBLE);//显示清除按钮
                break;
            case STATE_AFTER_ADD:
                MyTools.showMsg("添加 "+etOrigin.getText(),mContext);
                stateSwitch(STATE_INIT);
                break;
            case STATE_INIT:
                etOrigin.setText("");
                tvResult.setText("");
                btnTrans.setVisibility(View.VISIBLE);//显示翻译按钮
                btnClear.setVisibility(View.VISIBLE);//显示清除按钮
                btnAdd.setVisibility(View.GONE);//隐藏添加按钮
                break;
            default:
                break;
        }
    }


    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

        }

        /**
         * 在线翻译
         * */
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

        @Override
        protected void onProgressUpdate(Integer... values) {
            //    更新ProgressDialog的进度条

        }

        /**
         * 解析XML为WordInfo
         * */
        @Override
        protected void onPostExecute(String params) {
            String text = "null";
            WordInfo wordInfo=new WordInfo();
            if (params != null) {
                long time= MyTools.getCurrentTimeMillis();
                try {
                    wordInfo = XMLParse.parseXmlWithPull(params, true);
                    Log.d(TAG,text);
                    //DBHelper.setExampleSentence(context, text, wordId);
                } catch (Exception e) {
                    Log.d(TAG, "onPostExecute: " + text);
                }
                //tv_result.setText(wordInfo.toString());
                text=wordInfo.getDesc();
                tvResult.setText(text);
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("myTAG", "onCancelled: ");
        }

    }


}

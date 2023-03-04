package com.example.wordlist.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.XMLParse;
import com.example.wordlist.activity.WordListActivity;
import com.example.wordlist.broadcast.BroadcastName;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;

import java.io.IOException;
import java.util.ArrayList;
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

    private Button btnWordList;
    private Button btnTrans;//翻译
    private Button btnClear;//清空
    private Button btnAdd;//添加到单词本
    private EditText etOrigin;//原文
    private TextView tvResult;//译文
    private Activity mActivity;

    //BookDao bookDao= MainApplication.getInstance().getBookDB().bookDao();
    //TempMsg.WordInfo TempMsg.WordInfo=new TempMsg.WordInfo();

    WordDao wordDao=MainApplication.getInstance().getWordDB().wordDao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_translate, container, false);




        btnWordList=mView.findViewById(R.id.btn_chek_word_list);
        btnTrans=mView.findViewById(R.id.btn_trans_translate);
        btnClear=mView.findViewById(R.id.btn_clear_translate);
        btnAdd=mView.findViewById(R.id.btn_add_translate);
        etOrigin=mView.findViewById(R.id.et_origin_translate);
        tvResult=mView.findViewById(R.id.tv_trans_translate);

        btnWordList.setOnClickListener(v -> checkWordList());
        btnTrans.setOnClickListener(v -> translate());
        btnClear.setOnClickListener(v -> clear());
        btnAdd.setOnClickListener(v -> addToDB());

        //popUpKeyboard(etOrigin);

        return mView;
    }

    private void checkWordList() {
        startActivity(new Intent(mContext, WordListActivity.class));
    }

    private void addToDB() {
        TempMsg.WordInfo.setTime_stamp(MyTools.getCurrentTimeMillis());
        TempMsg.WordInfo.setFavorite(1);
        wordDao.insertOneWord(TempMsg.WordInfo);

        stateSwitch(STATE_AFTER_ADD);
        Log.d(TAG,"添加单词"+TempMsg.WordInfo.getName());
    }

    private void clear() {
        TempMsg.WordInfo.initWord();//清空TempMsg.WordInfo
        etOrigin.setText("");
        tvResult.setText("");
        notifyWord();
        stateSwitch(STATE_DURING_INPUT);
        Log.d(TAG,"清空TempMsg.WordInfo");
    }

    private void translate(){
        String origin=etOrigin.getText().toString();
        if (origin==null||origin.length()==0){
            MyTools.showMsg("请输入单词",mContext);
            return;
        }
        stateSwitch(STATE_DURING_TRANS);//翻译中
        StringBuilder url=new StringBuilder();
        url.append("https://dict-co.iciba.com/api/dictionary.php?w=");
        url.append(origin);
        url.append("&key=0EAE08A016D6688F64AB3EBB2337BFB0");
        new MyAsyncTask().execute(url.toString());

        //addToDetail();//弃用，用notifyWord();

        //notifyWord();//要在MyAsyncTask()里调用，否则是同步的

        //待做：添加到单词本

    }


    /**
     * 刷新WordDetailFragment
     * */
    private void notifyWord(){
        //广播
        Intent intent=new Intent(BroadcastName.WORD_DETAIL_REFRESH);
        String name=TempMsg.WordInfo.getName();
        intent.putExtra("name",name);
        mContext.sendBroadcast(intent);
        Log.d(TAG,"发送广播,name="+name);

        /*TempMsg.WordInfo.setName(TempMsg.WordInfo.getName());
        TempMsg.WordInfo.setSymbol_us(TempMsg.WordInfo.getSymbol_us());
        TempMsg.WordInfo.setSymbol_us(TempMsg.WordInfo.getSymbol_us());
        TempMsg.WordInfo.setDesc(TempMsg.WordInfo.getDesc());
        TempMsg.WordInfo.setSentence(TempMsg.WordInfo.getSentence());*/

        //XML解析得到的信息已经存入TempMsg.WordInfo TempMsg.WordInfo=TempMsg.tempTempMsg.WordInfo;
        Log.d(TAG,"提醒WordDetail刷新");

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
         * 解析XML为TempMsg.WordInfo
         * */
        @Override
        protected void onPostExecute(String params) {
            String text = "null";
            if (params != null) {
                long time= MyTools.getCurrentTimeMillis();
                try {
                    TempMsg.WordInfo = XMLParse.parseXmlWithPull(params, true);
                    Log.d(TAG,"XML处理完毕，存入TempMsg.tempTempMsg.WordInfo,Name为"+TempMsg.WordInfo.getName());
                    //DBHelper.setExampleSentence(context, text, wordId);
                } catch (Exception e) {
                    Log.d(TAG, "onPostExecute: " + text);
                }
                //显示到翻译结果上
                tvResult.setText(MyTools.briefDesc(TempMsg.WordInfo.getDesc()));
                //tvTranslation.setText("翻译");
                //tvTranslation.setEnabled(true);
                stateSwitch(STATE_AFTER_TRANS);//获取到了翻译结果，显示添加按钮
                notifyWord();

            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("myTAG", "onCancelled: ");
        }

    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"启动");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"继续，Name为"+TempMsg.WordInfo.getName());
        //popUpKeyboard(etOrigin);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"停止");
    }



    /**
     * 切换Fragment可视状态
     * */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Log.d(TAG,"切换可视状态");
        }else {
        }
    }

    //自动弹出键盘
    private void popUpKeyboard(EditText editText){
        editText.requestFocus();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    //收起键盘
    private void hideKeyboard(){
        InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager!=null){
            inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(),0);
        }
    }


}

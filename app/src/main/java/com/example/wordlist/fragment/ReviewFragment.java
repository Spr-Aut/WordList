package com.example.wordlist.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordlist.util.MyTools;
import com.example.wordlist.R;
import com.example.wordlist.XMLParse;
import com.example.wordlist.entity.WordInfo;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ReviewFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TabFirstFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private TextView tv_result;
    private Button btnTrans;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_review, container, false);
        tv_result = mView.findViewById(R.id.tv_result);
        btnTrans=mView.findViewById(R.id.btn_trans);
        btnTrans.setOnClickListener(this);




        return mView;
    }

    //"http://dict-co.iciba.com/api/dictionary.php?w=world&key=0EAE08A016D6688F64AB3EBB2337BFB0"
    @Override
    public void onClick(View v) {
        new MyAsyncTask().execute("https://dict-co.iciba.com/api/dictionary.php?w=world&key=0EAE08A016D6688F64AB3EBB2337BFB0");
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

        }

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

        /*解析XML为WordInfo*/
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
                tv_result.setText(wordInfo.toString());
                //Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
                tv_result.setGravity(Gravity.START);
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
        Log.d(TAG,"继续");
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

}

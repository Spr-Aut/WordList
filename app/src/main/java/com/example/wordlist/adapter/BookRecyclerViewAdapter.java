package com.example.wordlist.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.activity.WordListActivity;
import com.example.wordlist.activity.WordListDownloadActivity;
import com.example.wordlist.bean.BookBean;
import com.example.wordlist.bean.WordBean;
import com.example.wordlist.dao.BookDao;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.database.WordDatabase;
import com.example.wordlist.entity.BookInfo;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.util.MyTools;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final static String TAG="BookRecyclerViewAdapter";
    private Context mContext;
    private List<BookBean.BookDataBean> mBookList;
    private Dialog dialogDownLoad;
    private SharedPreferences userData;
    private boolean isBookListExist=false;

    public BookRecyclerViewAdapter(Context context,List<BookBean.BookDataBean> bookList){
        mContext=context;
        mBookList=bookList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_book,parent,false);
        userData=mContext.getSharedPreferences("UserData",MODE_PRIVATE);
        isBookListExist=userData.getBoolean("isBookListExist",false);
        return new BookRecyclerViewAdapter.ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookRecyclerViewAdapter.ItemHolder itemHolder=(BookRecyclerViewAdapter.ItemHolder) holder;
        itemHolder.tvItemBookName.setText(mBookList.get(position).getTitle());
        itemHolder.wholeItem.setOnClickListener(v -> wholeItemClicked(mBookList.get(position)));

        //Log.d(TAG,"onBindViewHolder:当前位置为："+position);
    }

    private void wholeItemClicked(BookBean.BookDataBean bookDataBean){
        String bookName = bookDataBean.getTitle();
        Log.d(TAG,"词书列表"+isBookListExist);
        if (isBookListExist){//BookInfo数据库已经创建了
            BookDao bookDao = MainApplication.getInstance().getBookDB().bookDao();
            boolean hasDownload = bookDao.getBookByName(bookName).isHasDownload();
            Log.d(TAG,"这本书"+hasDownload);
            if (hasDownload){//这本书下载过了
                Intent intent = new Intent(mContext, WordListDownloadActivity.class);
                intent.putExtra("bookName",bookName);
                Log.d(TAG,"打开"+bookName);
                mContext.startActivity(intent);
            }else {
                createDialog(bookDataBean);
            }
        }else {
            createDialog(bookDataBean);
        }


    }

    private void createDialog(BookBean.BookDataBean bookDataBean) {
        String bookName=bookDataBean.getTitle();
        Log.d(TAG,"弹窗：确认是否下载"+bookName);
        View view= LayoutInflater.from(mContext).inflate(R.layout.dialog_download_book,null);
        dialogDownLoad=new Dialog(mContext);

        //dialog背景透明，否则四个角不正常
        dialogDownLoad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams layoutParams=dialogDownLoad.getWindow().getAttributes();
        layoutParams.dimAmount=0.0f;
        dialogDownLoad.getWindow().setAttributes(layoutParams);

        TextView tvBookName=view.findViewById(R.id.tvDownloadBook);
        tvBookName.setText("请确认是否下载词书《"+bookName+"》");
        Button btnDownloadBook=view.findViewById(R.id.btnDownloadBook);
        Button btnNotDownloadBook=view.findViewById(R.id.btnNotDownloadBook);
        btnDownloadBook.setOnClickListener(v -> {
            downloadBook(bookDataBean);
            dialogDownLoad.dismiss();
        });
        btnNotDownloadBook.setOnClickListener(v -> {
            dialogDownLoad.dismiss();
        });

        dialogDownLoad.setContentView(view);
        dialogDownLoad.show();
    }

    private void downloadBook(BookBean.BookDataBean bookDataBean) {
        new MyAsyncTask().execute(bookDataBean);
    }

    private class MyAsyncTask extends AsyncTask<BookBean.BookDataBean, Integer, ArrayList<String>> {
        /**
         * 下载单词书
         */
        @Override
        protected ArrayList<String> doInBackground(BookBean.BookDataBean... params) {
            ArrayList<BookBean.BookDataBean.BookDataChildListBean> bookList = params[0].getChild_list();
            ArrayList<String>resList=new ArrayList<>();
            resList.add(params[0].getTitle());//resList[0]存的是词书的名字
            OkHttpClient client = new OkHttpClient();
            Log.d(TAG,"开始下载单词");
            for (BookBean.BookDataBean.BookDataChildListBean bean : bookList) {
                for (int i = 1; i <= Integer.parseInt(bean.getCourse_num()); i++) {
                    StringBuilder sb=new StringBuilder();
                    sb.append("https://rw.ylapi.cn/reciteword/wordlist.u?uid=12363&appkey=a8b7bf005ab94e9c38f2a514bc015669&class_id=");
                    sb.append(bean.getClass_id());
                    sb.append("&course=");
                    sb.append(i+"");
                    Request request = new Request.Builder().url(sb.toString()).build();
                    try {
                        Response response = client.newCall(request).execute();
                        // publishProgress(++i);
                        String res = Objects.requireNonNull(response.body()).string();
                        Log.d(TAG,res);
                        resList.add(res);
                    } catch (IOException e) {
                        //return e.getMessage();
                    }
                }

            }

            return resList;//resList里存的是20个单词组成的Json

        }

        /**
         * 解析XML为TempMsg.WordInfo
         */
        @Override
        protected void onPostExecute(ArrayList<String> resList) {
            if (resList != null && resList.size() != 0) {
                Log.d(TAG,"准备数据库");
                //MainApplication mainApplication=MainApplication.getInstance();
                String bookName=resList.get(0);
                Log.d(TAG,"wordDao");
                WordDao wordDao = Room.databaseBuilder(mContext, WordDatabase.class, bookName).addMigrations().allowMainThreadQueries().build().wordDao();
                //mainApplication.getWordDbMap().put(bookName,wordDb);
                Log.d(TAG,"将下载完的单词添加到数据库");
                for (int i=1;i<resList.size();i++) {
                    String s=resList.get(i);
                    Log.d(TAG,s);
                    Log.d(TAG,"转为bean");
                    WordBean wordBean = new Gson().fromJson(s, WordBean.class);
                    Log.d(TAG,"wordList");
                    ArrayList<WordBean.WordDataBean> wordList = wordBean.getDatas();
                    Log.d(TAG,"从wordList取词");
                    for (WordBean.WordDataBean word : wordList) {
                        Log.d(TAG,"转为WordInfo");
                        WordInfo wordInfo=new WordInfo(word.getName(),word.getDesc(),word.getSymbol(),word.getSound(),word.getSymbol(),word.getSound(),"", MyTools.getCurrentTimeMillis(),0,0,0,0,0);
                        Log.d(TAG,"insert");
                        wordDao.insertOneWord(wordInfo);
                    }
                }
                Log.d(TAG,"添加完成");
                //将该单词书标记为hasDownload，存入bookDao
                BookDao bookDao = MainApplication.getInstance().getBookDB().bookDao();
                bookDao.updateBook(new BookInfo(resList.get(0),true));

                SharedPreferences.Editor edit = userData.edit();
                edit.putBoolean("isBookListExist",true);
                edit.commit();
            }

        }
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }



    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView tvItemBookName;
        public CardView wholeItem;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvItemBookName=itemView.findViewById(R.id.tvItemBookName);
            wholeItem=itemView.findViewById(R.id.WholeBookItem);
        }
    }

    public void refreshData(List<BookBean.BookDataBean> bookList){
        this.mBookList=bookList;
        notifyDataSetChanged();
    }
}

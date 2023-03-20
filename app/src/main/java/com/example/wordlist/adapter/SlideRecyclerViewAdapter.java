package com.example.wordlist.adapter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordlist.MainApplication;
import com.example.wordlist.R;
import com.example.wordlist.activity.WordDetailActivity;
import com.example.wordlist.dao.WordDao;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.tuple.WordNameTransTuple;
import com.example.wordlist.util.MyTools;
import com.example.wordlist.util.TempMsg;

import java.util.List;

public class SlideRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "LinearDynamicAdapter";
    private Context mContext; // 声明一个上下文对象
    private List<WordNameTransTuple> mWordList;

    private LayoutInflater mLayoutInflater;
    private int viewType;
    public static final int TYPE_LINEAR_LAYOUT = 0;
    public static final int TYPE_GRID_LAYOUT = 1;
    public int TRANS_VISUAL_STATE=0;
    private WordDao wordDao = MainApplication.getInstance().getWordDB().wordDao();

    public SlideRecyclerViewAdapter(Context context,List<WordNameTransTuple> wordList){
        this.mContext=context;
        this.mWordList=wordList;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    public void setViewType(int viewType){
        this.viewType=viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==TYPE_LINEAR_LAYOUT){
            View view=mLayoutInflater.inflate(R.layout.item_word_info_brief,parent,false);
            WordListViewHolder myWordListViewHolder=new WordListViewHolder(view);
            return myWordListViewHolder;
        }else if (viewType==TYPE_GRID_LAYOUT){
            return null;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder==null){
            return;
        }
        if (holder instanceof WordListViewHolder){
            bindWordListViewHolder((WordListViewHolder)holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    public void refreshData(List<WordNameTransTuple> wordList){
        this.mWordList=wordList;
        notifyDataSetChanged();
    }

    //事件
    private void bindWordListViewHolder(WordListViewHolder holder,int position){
        //解决显示错乱问题
        holder.mTvOrigin.setVisibility(View.VISIBLE);
        if (TRANS_VISUAL_STATE == 1) {
            holder.mTvTrans.setVisibility(View.VISIBLE);
        } else {
            holder.mTvTrans.setVisibility(View.GONE);
        }
        holder.mViewMain.setVisibility(View.VISIBLE);
        holder.mViewSlideDelete.setVisibility(View.VISIBLE);
        holder.mViewSlideMore.setVisibility(View.VISIBLE);
        holder.mTvSymbol.setVisibility(View.VISIBLE);

        WordNameTransTuple wordInfo=mWordList.get(position);

        holder.mTvOrigin.setText(wordInfo.getName());
        holder.mTvTrans.setText(MyTools.briefDesc(wordInfo.getDesc()));
        if(TempMsg.isIsUk()){//WordListFragment中设置了
            holder.mTvSymbol.setText(wordInfo.getSymbol_uk());
        }else {
            holder.mTvSymbol.setText(wordInfo.getSymbol_us());
        }
        /*float blurRadius=20f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            holder.mTvTrans.setRenderEffect(RenderEffect.createBlurEffect(blurRadius,blurRadius, Shader.TileMode.CLAMP));

        }*/

        holder.mViewSlideDelete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                shake();
                deleteItem(wordInfo,position);
            }
        });
        holder.mViewSlideMore.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                shake();
                showMsg("更多功能正在开发中");
            }
        });
        holder.mViewMain.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                shake();
                transVisualStateSwitch(holder);
            }
        });

        holder.mViewMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(mContext, WordDetailActivity.class);
                WordNameTransTuple wordInfo=mWordList.get(position);
                intent.putExtra("name",wordInfo.getName());
                mContext.startActivity(intent);


                return true;
            }
        });

        //为整个卡片设置动画
        if (TempMsg.isUseListAnim()){
            holder.mViewMain.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_anim));
        }

    }

    //切换单条数据翻译结果的显示与否
    private void transVisualStateSwitch(WordListViewHolder holder){
        if (holder.mTvTrans.getVisibility()==View.VISIBLE){
            holder.mTvTrans.setVisibility(View.GONE);
        }else {
            holder.mTvTrans.setVisibility(View.VISIBLE);
        }
    }

    //切换全部数据翻译结果的显示与否
    /*private void transVisualStateSwitchWhole(){
        if (TRANS_VISUAL_STATE==1){
            TRANS_VISUAL_STATE=0;
        }else {
            TRANS_VISUAL_STATE=1;
        }
    }*/

    private void deleteItem(WordNameTransTuple word,int position){
        //int row=mNoteDbOpenHelper.deleteFromDbById(note.getId()+"");
        if(true){//row>0
            WordInfo wordInfo=new WordInfo();
            wordInfo.setName(word.getName());
            wordDao.deleteWord(wordInfo);
            removeData(position);
            showMsg("删除");
        }
    }

    private void removeData(int position) {
        mWordList.remove(position);
        //显示remove动画
        notifyItemRemoved(position);
        //删除后刷新被删的Item之后的Item，不要用notifyItemRangeRemoved，不然后面的Item会闪一下
        notifyItemRangeChanged(position,mWordList.size()-position);
    }

    //控件设置
    class WordListViewHolder extends RecyclerView.ViewHolder{

        TextView mTvOrigin;
        TextView mTvTrans;
        TextView mTvSymbol;
        CardView mViewMain;
        LinearLayout mViewSlideDelete;
        LinearLayout mViewSlideMore;


        public WordListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvOrigin=itemView.findViewById(R.id.tv_item_origin);
            this.mTvTrans=itemView.findViewById(R.id.tv_item_trans);
            this.mViewMain=itemView.findViewById(R.id.view_item_brief);
            this.mTvSymbol=itemView.findViewById(R.id.tv_item_symbol);
            this.mViewSlideDelete=itemView.findViewById(R.id.view_item_brief_slide_delete);
            this.mViewSlideMore=itemView.findViewById(R.id.view_item_brief_slide_more);
        }
    }

    private void showMsg(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void shake(){
        //震动
        Vibrator vibrator=(Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
        }
    }

}
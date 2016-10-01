package com.example.peacemaker.oneplayer;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.peacemaker.oneplayer.databinding.OneSingerDetailActivityBinding;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/7/18.
 */

public class OneSingerDetailActivity extends OneActivity{
    @BindView(R.id.one_singerlist_singer_detail_recycler)
    public RecyclerView recyclerView;
    @BindView(R.id.one_singer_detail_toolbar)
    public Toolbar toolbar;
    @BindView(R.id.singer_detail_collapsing_toolbar_layout)
    public CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.id_appbarlayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.id_imageView)
    public ImageView imageView;
    private OneSingerDetailActivityBinding binding;
    private OneSingerDetailItemAdapter oneSingerDetailItemAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT > 19) {
            setTheme(R.style.Oneplayer);
            Log.v("OneSingerDetailActivity", "我选择OnePlayer主题");
        } else {
            setTheme(R.style.One);
            Log.v("OneSingerDetailActivity", "我选择One主题");
        }
        super.onCreate(savedInstanceState);
        initialize();
    }
    protected void initialize(){
        final Music music = oneApplication.getTargetMusic();
        Log.v("OneSingerDetailActivity","initialize()检查目标音乐"+music.getDisplayName());
        binding = DataBindingUtil.setContentView(this, R.layout.one_singer_detail_activity);
        ButterKnife.bind(this);
        binding.setMusicState(oneApplication.musicState);
        binding.setMusic(oneApplication.currentMusic);
        binding.setOneConfig(oneApplication.getOneConfig());
        binding.setHandler(new OneClickHandler());
        binding.setDisplayName(music.getDisplayName());
        //KitKatTool.setCollapsingToolbar(this,coordinatorLayout,appBarLayout,imageView,toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        oneSingerDetailItemAdapter = new OneSingerDetailItemAdapter(music.getSecondItems());
        oneSingerDetailItemAdapter.setOnItemHitListener(new OnItemHitListener() {
            @Override
            public void onItemHit(int position, Music music) {
                selectMusic(music,position);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(oneSingerDetailItemAdapter);
        final Paint paint = new Paint();
        paint.setColor(Color.rgb(192,192,192));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                final int left = parent.getPaddingLeft()+20;
                final int right = parent.getMeasuredWidth() - parent.getPaddingRight()-20;
                final int childSize = parent.getChildCount() ;
                for(int i=0 ; i <childSize ; i ++){
                    final View child = parent.getChildAt(i) ;
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                    final int top = child.getBottom() + layoutParams.bottomMargin ;
                    final int bottom = top + 2 ;
                    c.drawRect(left,top,right,bottom,paint);
                }
            }
        });
        final Bitmap bitmap = OneBitmapUtil.zoomImg(this,R.drawable.ic_arrow_back_white_48dp,12);
        Drawable drawable = new Drawable() {
            Paint paint = new Paint();
            @Override
            public void draw(Canvas canvas) {

                canvas.drawBitmap(bitmap,canvas.getWidth()/2-bitmap.getWidth()/2,canvas.getHeight()/2-bitmap.getHeight()/2,paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        toolbar.setNavigationIcon(drawable);
//        ((ViewGroup)toolbar.getChildAt(0)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                quitSingerDetail();
//            }
//        });
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("OneSingerDetailActivity","navagation点击");
                quitSingerDetail();
            }
        });
        oneApplication.setOneActivity(this);
        super.initialize();

    }
    protected void disableListview(){
        recyclerView.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.GONE);
    }
    protected void enableListview(){
        recyclerView.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.VISIBLE);
    }
    private void quitSingerDetail(){
        this.finish();
    }

    @Override
    protected void onDestroy() {
       // oneApplication.unRegister();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(isPlayView){
            quitPlayView();
        }else {
            quitSingerDetail();
        }
    }

    public class OneSingerDetailItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private OnItemHitListener onItemHitListener;
        private ArrayList<Music> musicArrayList;
        public void setOnItemHitListener(OnItemHitListener onItemHitListener){
            this.onItemHitListener = onItemHitListener;
        }

        public OneSingerDetailItemAdapter(ArrayList<Music> musicArrayList) {
            this.musicArrayList = musicArrayList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            OneSongViewHolder oneSongViewHolder = OneSongViewHolder.createHolder(LayoutInflater.from(parent.getContext()),parent);
            return oneSongViewHolder;


        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof OneSongViewHolder) {
                Music music = musicArrayList.get(position);
                final OneSongViewHolder oneSongViewHolder = (OneSongViewHolder) holder;
                oneSongViewHolder.bind(music,false);
                oneSongViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = oneSongViewHolder.getLayoutPosition();
                        if (onItemHitListener != null && position != -1) {
                            onItemHitListener.onItemHit(position, (Music) v.getTag());
                        }
                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            if(musicArrayList!=null) {
                return musicArrayList.size();
            }
            return 0;
        }

    }




}

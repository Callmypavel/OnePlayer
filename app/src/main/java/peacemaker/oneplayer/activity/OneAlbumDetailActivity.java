package peacemaker.oneplayer.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
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

import peacemaker.oneplayer.entity.AlbumInfo;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.entity.SingerInfo;
import peacemaker.oneplayer.listener.OnItemHitListener;
import peacemaker.oneplayer.OneClickHandler;
import peacemaker.oneplayer.tool.LogTool;
import peacemaker.oneplayer.tool.MusicProvider;
import peacemaker.oneplayer.view.ListViewDecorarion;
import peacemaker.oneplayer.view.OneSongViewHolder;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.databinding.OneAlbumDetailActivityBinding;
import peacemaker.oneplayer.tool.OneBitmapUtil;
import peacemaker.oneplayer.view.UniversalAdapter;

import java.util.ArrayList;


/**
 * Created by ouyan on 2016/10/8.
 */

public class OneAlbumDetailActivity extends OneActivity {
//    @BindView(R.id.one_albumlist_album_detail_recycler)
//    public RecyclerView recyclerView;
//    @BindView(R.id.one_album_detail_toolbar)
//    public Toolbar toolbar;
//    @BindView(R.id.album_detail_collapsing_toolbar_layout)
//    public CollapsingToolbarLayout collapsingToolbarLayout;
//    @BindView(R.id.id_appbarlayout)
//    public AppBarLayout appBarLayout;
//    @BindView(R.id.id_imageView)
//    public ImageView imageView;
    private String albumId;
    private AlbumInfo albumInfo = new AlbumInfo("");
    private ObservableArrayList<Music> songs = new ObservableArrayList<>();
    private OneAlbumDetailActivityBinding binding;
   // private OneAlbumDetailActivity.OneAlbumDetailItemAdapter oneAlbumDetailItemAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }
    protected void initialize(){
        albumId = getIntent().getStringExtra("albumId");
        UniversalAdapter<Music> adapter = new UniversalAdapter(R.layout.item_music);
        adapter.setDataSource(songs);
        adapter.setOnClickListener(this);

        MusicProvider.loadAlbum(albumId,albumInfo);
        MusicProvider.loadSongsByAlbumId(albumId,songs,albumInfo);
        LogTool.log(this,"initialize()检查目标专辑"+albumId);

//        final Music music = oneApplication.getTargetMusic();
//        Log.v("OneSingerDetailActivity","initialize()检查目标音乐"+music.getUrl());
//        Log.v("OneSingerDetailActivity","initialize()检查目标音乐"+music.getMiddleAlbumArt(this));
        binding = DataBindingUtil.setContentView(this, R.layout.one_album_detail_activity);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.addItemDecoration(new ListViewDecorarion(1));
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.setMusicState(oneApplication.musicState);
        binding.setMusic(oneApplication.currentMusic);
        binding.setHandler(new OneClickHandler());
        binding.setAlbumInfo(albumInfo);
        binding.setOneConfig(oneApplication.getOneConfig());
        //KitKatTool.setCollapsingToolbar(this,coordinatorLayout,appBarLayout,imageView,toolbar);
        binding.albumDetailCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        binding.albumDetailCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);


//        oneAlbumDetailItemAdapter = new OneAlbumDetailActivity.OneAlbumDetailItemAdapter(music.getSecondItems());
//        oneAlbumDetailItemAdapter.setOnItemHitListener(new OnItemHitListener() {
//            @Override
//            public void onItemHit(int position, Music music) {
//                selectMusic(music,position);
//            }
//        });
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        binding.oneAlbumlistAlbumDetailRecycler.setLayoutManager(linearLayoutManager);
//        binding.oneAlbumlistAlbumDetailRecycler.setHasFixedSize(true);

        final Paint paint = new Paint();
        paint.setColor(Color.rgb(192,192,192));

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
                return PixelFormat.OPAQUE;
            }
        };
        binding.oneAlbumDetailToolbar.setNavigationIcon(drawable);
//        ((ViewGroup)toolbar.getChildAt(0)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                quitSingerDetail();
//            }
//        });
        setSupportActionBar(binding.oneAlbumDetailToolbar);
        binding.oneAlbumDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("OneSingerDetailActivity","navagation点击");
                quitSingerDetail();
            }
        });
        oneApplication.setOneActivity(this);
        super.initialize();

    }


    @Override
    public void onPermissionGranted(Boolean isPermissionUpdated) {
        if (!isPermissionUpdated){
            initPermission();
        }
    }
    protected void disableListview(){
        binding.recyclerview.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.GONE);
    }
    protected void enableListview(){
        binding.recyclerview.setVisibility(View.VISIBLE);
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
        if(oneApplication.musicState.getIsPlayView()){
            quitPlayView();
        }else {
            quitSingerDetail();
        }
    }

    public class OneAlbumDetailItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private OnItemHitListener onItemHitListener;
        private ArrayList<Music> musicArrayList;
        public void setOnItemHitListener(OnItemHitListener onItemHitListener){
            this.onItemHitListener = onItemHitListener;
        }

        public OneAlbumDetailItemAdapter(ArrayList<Music> musicArrayList) {
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_item:
                //点击歌曲列表中的item
                OneApplication.selectMusic((Music)view.findViewById(R.id.MusicName).getTag(),(int)view.getTag(),songs);
                break;
        }
    }
}

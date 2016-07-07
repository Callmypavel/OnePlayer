package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.MainActivity;
import com.example.peacemaker.oneplayer.Music;
import com.example.peacemaker.oneplayer.R;

import java.util.ArrayList;
import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/6/9.
 */

public class OneAblumListFragment extends Fragment {
    @BindView(R.id.one_albumlist_fragment_recyclerView)
    public RecyclerView recyclerView;
    private OneImageCache imageCache = new OneImageCache();
    private Bitmap defaultBitmap;
    private MainActivity activity;
    private boolean isFlinging = false;
    private GridLayoutManager gridLayoutManager ;
    private OneAlbumItemAdapter oneAlbumItemAdapter;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    //private boolean isEnable = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        oneAlbumItemAdapter = new OneAlbumItemAdapter(activity.getAlbumArraylist());
        oneAlbumItemAdapter.setOnItemHitListener(new OnItemHitListener() {
            @Override
            public void onItemHit(int position, Music music) {
                activity.itemSelected(music,position);
            }
        });
        recyclerView.setAdapter(oneAlbumItemAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(activity,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //gridLayoutManager.scrollToPositionWithOffset(activity.albumSelectedPosition, activity.albumSelectedOffset);
    }
//    public void disable(){
////        if(oneGridView!=null) {
////            oneGridView.setEnabled(false);
////            oneGridView.setClickable(false);
////        }
//        isEnable = false;
//    }
//    public void enable(){
////        if(oneGridView!=null) {
////            oneGridView.setEnabled(true);
////            oneGridView.setClickable(true);
////        }
//        isEnable = true;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_albumlist_fragment,container,false);
        ButterKnife.bind(this,view);
        initialize();
        return view;
    }
    public void initialize(){
        defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.music);
        //oneAlbumItemAdapter = new OneAlbumItemAdapter(getActivity());


//        oneGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                //Log.v("OneAblumListFragment","滚动状态改变"+scrollState+","+AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                    selectedPosition = oneGridView.getFirstVisiblePosition();
//                    //Log.v("OneAblumListFragment","停止滚动"+selectedPosition);
////                    boolean needToFresh = false;
////                    if(isFlinging){
////                        needToFresh = true;
////                    }
//                    isFlinging = false;
//                    isScolling = false;
////                    Log.v("OneAblumListFragment","没在滚动");
////                    if(needToFresh) {
////                        oneAlbumItemAdapter.notifyDataSetChanged();
////                    }
//                }else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
////                    if(oneGridView.getFirstVisiblePosition()!=selectedPosition) {
//                        isFlinging = true;
////                    }
//                    //isScolling = true;
//                    //Log.v("OneAblumListFragment","正在fling");
//                }else if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                    isScolling = true;
//                    //Log.v("OneAblumListFragment","正在滚动");
//                }
//            }

//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
    }

    @Override
    public void onDestroy() {
//        int position = gridLayoutManager.findFirstVisibleItemPosition();
//        activity.albumSelectedPosition = position;
//        View view = recyclerView.getChildAt(position);
//        if (view != null) {
//            activity.albumSelectedOffset = view.getTop();
//        }
        super.onDestroy();
    }
    public class OneAlbumItemAdapter extends RecyclerView.Adapter<OneAlbumItemAdapter.OneAblumViewHolder>{
        private ArrayList<Music> musicArrayList;
        private OnItemHitListener onItemHitListener;
        public void setOnItemHitListener(OnItemHitListener onItemHitListener){
            this.onItemHitListener = onItemHitListener;
        }

        public OneAlbumItemAdapter(ArrayList<Music> musicArrayList) {
            this.musicArrayList = musicArrayList;
        }

        @Override
        public OneAlbumItemAdapter.OneAblumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.v("OneAblumListFragment","onCreateViewHolder()");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_albumlist_item, parent, false);
            OneAblumViewHolder oneViewHolder = new OneAblumViewHolder(view);
            return oneViewHolder;
        }

        @Override
        public void onBindViewHolder(final OneAlbumItemAdapter.OneAblumViewHolder holder, final int position) {
//            Log.v("OneAblumListFragment","onBindViewHolder()");
            final Music music = musicArrayList.get(position);
            holder.singerTextView.setText(music.getSecondItems().get(0).getArtist());
            holder.albumTextView.setText(music.getDisplayName());
            holder.albumImageView.setTag(music.getSecondItems().get(0).getId());
            Log.v("OneAblumListFragment","tag设为id"+music.getSecondItems().get(0).getId());
            if(music.getSecondItems()!=null) {
                holder.songNumberTextView.setText(music.getSecondItems().size() + "首歌曲");
            }
            Bitmap bitmap = imageCache.getBitmapFromCache(music.getSecondItems().get(0).getId() + "");
            if (bitmap == null) {
                holder.albumImageView.setImageBitmap(defaultBitmap);
                //Log.v("OneAblumListFragment","设置默认bitmap"+defaultBitmap);
                final Handler handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int id = msg.what;
                        Bitmap ablumBitmap = msg.getData().getParcelable("ablumBitmap");
                        int position = msg.getData().getInt("position");
//                        Log.v("OneAblumListFragment","handler接到的：根据id找tag"+id);
//                        Log.v("OneAblumListFragment","handler接到的：position"+position);
//                        Log.v("OneAblumListFragment","handler接到的：View"+recyclerView.findViewWithTag(id));
                        ImageView imageView = (ImageView) recyclerView.findViewWithTag(id);
//                        Log.v("OneAblumListFragment","handler接到的：imageView"+imageView);
//                        Log.v("OneAblumListFragment","handler接到的：ablumBitmap"+ablumBitmap);
                        imageCache.addToCache(id + "", ablumBitmap);
                        if (imageView != null) {
                            imageView.setImageBitmap(ablumBitmap);
//                            Log.v("OneAblumListFragment","handler接到的：设置bitmap"+ablumBitmap);

                        }
                        return false;
                    }
                });
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap ablumBitmap = music.getSecondItems().get(0).getMiddleAlbumArt(activity);
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("ablumBitmap", ablumBitmap);
                        bundle.putInt("position",position);
                        message.setData(bundle);
                        message.what = music.getSecondItems().get(0).getId();
                        Log.v("OneAblumListFragment","发出id"+message.what);
                        handler.sendMessage(message);
                    }
                });
            } else {
                holder.albumImageView.setImageBitmap(bitmap);
                Log.v("OneAblumListFragment","缓存取bitmap"+bitmap);
            }
            holder.itemView.setTag(music);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    if(onItemHitListener!=null&&position!=-1){
                        onItemHitListener.onItemHit(position,(Music)v.getTag());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (musicArrayList != null) {
                return musicArrayList.size();
            }
            return 0;
        }

        public class OneAblumViewHolder extends RecyclerView.ViewHolder {
            public TextView singerTextView;
            public TextView albumTextView;
            public TextView songNumberTextView;
            public ImageView albumImageView;

            public OneAblumViewHolder(View itemView) {
                super(itemView);
                singerTextView = ButterKnife.findById(itemView, R.id.one_albumlist_singer_name);
                albumTextView = ButterKnife.findById(itemView, R.id.one_albumlist_album_name);
                albumImageView = ButterKnife.findById(itemView, R.id.one_albumlist_album_image);
                songNumberTextView = ButterKnife.findById(itemView, R.id.one_albumlist_song_number);
            }
        }

    }




//    private class OneAlbumItemAdapter extends ArrayAdapter<Music> {
//        private Activity activity;
////        @Override
////        public boolean areAllItemsEnabled() {
////            return isEnable;
////        }
//
//        public OneAlbumItemAdapter(Activity context) {
//            super(context, R.layout.music_list, new ArrayList<Music>());
//            activity = context;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
////            if(parent.getChildCount() == position){
//                Music music = getItem(position);
//                //Log.v("OneAblumListFragment", "我要getView" + music.getDisplayName());
//                OneAlbumItemAdapter.OneAblumViewHolder oneViewHolder = new OneAlbumItemAdapter.OneAblumViewHolder();
//                return oneViewHolder.getHolderView(convertView, activity, parent, music, position);
////            }else {
////                return null;
////            }
//        }
//
//
//        public class OneAblumViewHolder{
//            private TextView singerTextView;
//            private TextView albumTextView;
//            private TextView songNumberTextView;
//            private ImageView albumImageView;
//
//
//            View getHolderView(View convertView, final Activity activity, ViewGroup parent, final Music music, final int position) {
//                final OneAblumViewHolder oneAblumViewHolder;
//                if(convertView==null){
//                    convertView = activity.getLayoutInflater().inflate(R.layout.one_albumlist_item,parent,false);
//                    oneAblumViewHolder = new OneAblumViewHolder();
//                    oneAblumViewHolder.singerTextView = ButterKnife.findById(convertView,R.id.one_albumlist_singer_name);
//                    oneAblumViewHolder.albumTextView = ButterKnife.findById(convertView,R.id.one_albumlist_album_name);
//                    oneAblumViewHolder.albumImageView = ButterKnife.findById(convertView,R.id.one_albumlist_album_image);
//                    oneAblumViewHolder.songNumberTextView = ButterKnife.findById(convertView,R.id.one_albumlist_song_number);
//                    convertView.setTag(oneAblumViewHolder);
//                }else {
//                    oneAblumViewHolder = (OneAblumViewHolder) convertView.getTag();
//                }
//                oneAblumViewHolder.singerTextView.setText(music.getSecondItems().get(0).getArtist());
//                oneAblumViewHolder.albumTextView.setText(music.getDisplayName());
//                oneAblumViewHolder.songNumberTextView.setText(music.getSecondItems().size()+"首歌曲");
//                oneAblumViewHolder.albumImageView.setTag(music.getSecondItems().get(0).getId());
//                Bitmap bitmap = imageCache.getBitmapFromCache(music.getSecondItems().get(0).getId()+"");
//                if(bitmap==null) {
//                    //Log.v("OneAblumListFragment","缓存中没有图片");
//                    oneAblumViewHolder.albumImageView.setImageBitmap(defaultBitmap);
//                    //if(!isFlinging&&!isScolling) {
//                        final Handler handler = new Handler(new Handler.Callback() {
//                            @Override
//                            public boolean handleMessage(Message msg) {
//                                //if(msg.what==music.getId()){
//                                int id = msg.what;
//                                final Bitmap ablumBitmap = msg.getData().getParcelable("ablumBitmap");
//                                //if(position>=selectedPosition&&position<=selectedPosition+5) {
//                                final ImageView imageView = (ImageView) oneGridView.findViewWithTag(id);
//                                imageCache.addToCache(id+"",ablumBitmap);
//                                if(imageView!=null) {
//                                    imageView.setImageBitmap(ablumBitmap);
//                                    //oneAlbumItemAdapter.notifyDataSetChanged();
//                                    //Log.v("OneAblumListFragment","更新专辑封面:"+id);
//                                }
//                                //}
//                                //}
//                                return false;
//                            }
//                        });
////                    final Handler handler = new Handler(new Handler.Callback() {
////                        @Override
////                        public boolean handleMessage(Message msg) {
//////                            if(msg.what==music.getSecondItems().get(0).getId()){
//////                            Bitmap bitmap = msg.getData().getParcelable("ablumBitmap");
//////                            oneAblumViewHolder.albumImageView.setImageBitmap(bitmap);
//////                            Log.v("OneAblumListFragment", "Handler已get" + bitmap+",艾迪是"+music.getSecondItems().get(0).getId());
//////                            }
////                            return false;
////                        }
////                    });
////                Thread thread = new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Bitmap ablumBitmap = music.getSecondItems().get(0).getMiddleAlbumArt(activity);
////                        Message message = new Message();
////                        Bundle bundle = new Bundle();
////                        bundle.putParcelable("ablumBitmap",ablumBitmap);
////                        message.setData(bundle);
////                        message.what=music.getId();
////                        handler.sendMessage(message);
////                    }
////                });
////                thread.start();
//                        executorService.submit(new Runnable() {
//                            @Override
//                            public void run() {
//                                Bitmap ablumBitmap = music.getSecondItems().get(0).getMiddleAlbumArt(activity);
//                                Message message = new Message();
//                                Bundle bundle = new Bundle();
//                                bundle.putParcelable("ablumBitmap",ablumBitmap);
//                                message.setData(bundle);
//                                message.what=music.getSecondItems().get(0).getId();
//                                //while(isScolling){
//                                //Log.v("OneAblumListFragment","正在滚动");
//                                //}
//                                handler.sendMessage(message);
////                            final String url = music.getSecondItems().get(0).getUrl();
////                            handler.post(new Runnable() {
////                                @Override
////                                public void run() {
////                                    final Bitmap ablumBitmap = music.getSecondItems().get(0).getMiddleAlbumArt(activity);
////                                    //if(music.getSecondItems().get(0).getUrl().equals(url)) {
////                                        final ImageView imageView = (ImageView) oneGridView.findViewWithTag(url);
////                                        //oneAblumViewHolder.albumImageView.setImageBitmap(ablumBitmap);
////                                        if(imageView!=null){
//////                                            while(isScolling){
//////                                               Log.v("OneAblumListFragment","正在滚动");
//////                                            }
////                                            Thread thread = new Thread(new Runnable() {
////                                                @Override
////                                                public void run() {
////                                                    while(isScolling){
////                                                        Log.v("OneAblumListFragment","正在滚动");
////                                                    }
////                                                    imageView.setImageBitmap(ablumBitmap);
////                                                }
////                                            });
////                                            thread.start();
////
////                                        }
////                                        //Log.v("OneAblumListFragment", "已获取图片" + ablumBitmap + ",艾迪是" + music.getSecondItems().get(0).getId());
////                                        //oneAlbumItemAdapter.notifyDataSetChanged();
////                                    //}
////                                }
////                            });
//                            }
//                        });
//                    //}
//                }else {
//                    //Log.v("OneAblumListFragment","从缓存中获取图片");
//                    oneAblumViewHolder.albumImageView.setImageBitmap(bitmap);
//                }
//
//
//
//
//                return convertView;
//            }
//
//
//        }
//    }

}

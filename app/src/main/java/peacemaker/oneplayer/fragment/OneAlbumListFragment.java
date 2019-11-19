package peacemaker.oneplayer.fragment;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import android.os.Handler;


import peacemaker.oneplayer.R;
import peacemaker.oneplayer.activity.MainActivity;
import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.activity.OneLocalMusicActivity;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.listener.OnItemHitListener;
import peacemaker.oneplayer.view.OneAlbumViewHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * Created by ouyan on 2016/6/9.
 */

public class OneAlbumListFragment extends OneFragment {
    //private OneImageCache imageCache = new OneImageCache();
    private Bitmap defaultBitmap;
    private OneApplication oneApplication;
    private boolean isFlinging = false;
    private GridLayoutManager gridLayoutManager ;
   // private OneAlbumItemAdapter oneAlbumItemAdapter;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        //gridLayoutManager.scrollToPositionWithOffset(activity.albumSelectedPosition, activity.albumSelectedOffset);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_albumlist_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.one_fragment_recyclerView);
        initialize();
        return view;
    }

    public void initialize(){
        defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.music);
        oneApplication = (OneApplication) getActivity().getApplication();
//        oneAlbumItemAdapter = new OneAlbumItemAdapter(oneApplication.getAlbumArrayList());
//        oneAlbumItemAdapter.setOnItemHitListener(new OnItemHitListener() {
//            @Overridem
//            public void onItemHit(int position, Music music) {
//                ((MainActivity)getActivity()).toSecondItemActivity(music);
//            }
//        });
        //recyclerView.setAdapter(oneAlbumItemAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(oneApplication,2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @BindingAdapter({"android:bitmapImage"})
    public static void setImageResource(ImageView imageView,Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }


//    public class OneAlbumItemAdapter extends RecyclerView.Adapter<OneAlbumViewHolder>{
//        private ArrayList<Music> musicArrayList;
//        private OnItemHitListener onItemHitListener;
//        public void setOnItemHitListener(OnItemHitListener onItemHitListener){
//            this.onItemHitListener = onItemHitListener;
//        }
//
//        public OneAlbumItemAdapter(ArrayList<Music> musicArrayList) {
//            this.musicArrayList = musicArrayList;
//        }
//
//        @Override
//        public OneAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            Log.v("OneAblumListFragment","onCreateViewHolder()");
//            OneAlbumViewHolder oneViewHolder = OneAlbumViewHolder.createHolder(LayoutInflater.from(parent.getContext()),parent);
//            return oneViewHolder;
//        }
//
//
//
//        @Override
//        public void onBindViewHolder(final OneAlbumViewHolder holder, final int position) {
//            final Music music = musicArrayList.get(position);
//            //holder.albumImageView.setTag(music.getSecondItems().get(0).getId());
//            //Log.v("OneAblumListFragment","tag设为id"+music.getSecondItems().get(0).getId());
//            Bitmap bitmap = imageCache.getBitmapFromCache(music.getSecondItems().get(0).getId() + "");
//            if (bitmap == null) {
//                holder.albumImageView.setImageBitmap(defaultBitmap);
//                //Log.v("OneAblumListFragment","设置默认bitmap"+defaultBitmap);
//                final Handler handler = new Handler(new Handler.Callback() {
//                    @Override
//                    public boolean handleMessage(Message msg) {
//                        int id = msg.what;
//                        Bitmap ablumBitmap = msg.getData().getParcelable("albumBitmap");
////                        int position = msg.getData().getInt("position");
////                        Log.v("OneAblumListFragment","handler接到的：根据id找tag"+id);
////                        Log.v("OneAblumListFragment","handler接到的：position"+position);
////                        Log.v("OneAblumListFragment","handler接到的：View"+recyclerView.findViewWithTag(id));
//                        ImageView imageView = (ImageView) recyclerView.findViewWithTag(id);
////                        Log.v("OneAblumListFragment","handler接到的：imageView"+imageView);
////                        Log.v("OneAblumListFragment","handler接到的：ablumBitmap"+ablumBitmap);
//                        imageCache.addToCache(id + "", ablumBitmap);
//                        if (imageView != null) {
//                            imageView.setImageBitmap(ablumBitmap);
////                            Log.v("OneAblumListFragment","handler接到的：设置bitmap"+ablumBitmap);
//
//                        }
//                        return false;
//                    }
//                });
//                executorService.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Bitmap ablumBitmap = music.getSecondItems().get(0).getMiddleAlbumArt(getContext());
//                        Message message = new Message();
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable("albumBitmap", ablumBitmap);
//                        bundle.putInt("position",position);
//                        message.setData(bundle);
//                       // message.what = music.getSecondItems().get(0).getId();
//                        //Log.v("OneAblumListFragment","发出id"+message.what);
//                        handler.sendMessage(message);
//                    }
//                });
//            } else {
//                holder.albumImageView.setImageBitmap(bitmap);
//                //Log.v("OneAblumListFragment","缓存取bitmap"+bitmap);
//            }
//            //holder.bind(music, musicArrayList.get(position).getSecondItems().size());
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = holder.getLayoutPosition();
//                    if(onItemHitListener!=null&&position!=-1){
//                        onItemHitListener.onItemHit(position,(Music)v.getTag());
//                    }
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            if (musicArrayList != null) {
//                return musicArrayList.size();
//            }
//            return 0;
//        }
//
//
//
//    }

}
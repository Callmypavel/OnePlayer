package peacemaker.oneplayer.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peacemaker.oneplayer.R;
import peacemaker.oneplayer.activity.MainActivity;
import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.listener.OnItemHitListener;
import peacemaker.oneplayer.view.OneSongViewHolder;

import java.util.ArrayList;


/**
 * Created by ouyan on 2016/6/7.
 */

public class OnePlayListFragment extends OneFragment {
    private OneApplication oneApplication;
    private LinearLayoutManager linearLayoutManager;
    private OneSongItemAdapter oneSongItemAdapter;
    private MainActivity mainActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        oneApplication = (OneApplication) getActivity().getApplication();
        mainActivity = (MainActivity) getActivity();
        oneSongItemAdapter = new OneSongItemAdapter(oneApplication.getSongArrayList());
        oneSongItemAdapter.setOnItemHitListener(new OnItemHitListener() {
            @Override
            public void onItemHit(int position, Music music) {
                //oneApplication.selectMusic(music,position);
                oneSongItemAdapter.notifyDataSetChanged();
            }
        });

        //mainActivity.bindToIndexView(recyclerView);

    }
//    public void updateIndexView(){
//        mainActivity.indexView.setIndexedMusics(oneApplication.getIndexedSongArrayList());
//        mainActivity.indexView.setRecyclerView(recyclerView);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_playlist_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.one_fragment_recyclerView);
        initialize();
        return view;
    }
    public void refresh(){
        if(oneSongItemAdapter!=null) {
            oneSongItemAdapter.notifyDataSetChanged();
        }
    }


    public void initialize(){
        linearLayoutManager = new LinearLayoutManager(oneApplication);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(oneSongItemAdapter);
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
        recyclerView.setHasFixedSize(true);

    }
    private class OneSongItemAdapter extends RecyclerView.Adapter<OneSongViewHolder>{
        private ArrayList<Music> musicArrayList;
        private OnItemHitListener onItemHitListener;
        public OneSongItemAdapter(ArrayList<Music> musicArrayList){
            this.musicArrayList = musicArrayList;
        }
        public void setOnItemHitListener(OnItemHitListener onItemHitListener){
            this.onItemHitListener = onItemHitListener;
        }

        @Override
        public OneSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            OneSongViewHolder oneSongViewHolder = OneSongViewHolder.createHolder(LayoutInflater.from(parent.getContext()),parent);
            return oneSongViewHolder;
        }

        @Override
        public void onBindViewHolder(final OneSongViewHolder holder, int position) {
            if(musicArrayList!=null){
                if(musicArrayList.size()!=0){
                    Music music = musicArrayList.get(position);
                    boolean isShow = music.equals(oneApplication.currentMusic);
                    holder.bind(music,isShow);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = holder.getLayoutPosition();
                            if(onItemHitListener!=null&&position!=-1){
                                onItemHitListener.onItemHit(position,(Music)v.getTag());
                            }
                        }
                    });
                    return;
                }
            }
            Music music = new Music("找不到音乐");
            music.setArtist("请下载音乐后重启app");
            music.setAlbum("One提示");
            holder.nullBind(music);

        }

        @Override
        public int getItemCount() {
            if(musicArrayList!=null){
                if(musicArrayList.size()==0){
                    return 1;
                }
                return musicArrayList.size();
            }

            return 1;
        }

    }

}
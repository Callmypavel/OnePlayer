package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.MainActivity;
import com.example.peacemaker.oneplayer.Music;
import com.example.peacemaker.oneplayer.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/6/7.
 */

public class OnePlayListFragment extends Fragment {
    @BindView(R.id.one_play_recycler_list)
    public RecyclerView recyclerView;
    private OneApplication oneApplication;
    private LinearLayoutManager linearLayoutManager;
    private OneSongItemAdapter oneSongItemAdapter;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        oneApplication = (OneApplication) getActivity().getApplication();
        oneSongItemAdapter = new OneSongItemAdapter(oneApplication.getSongArrayList());
        oneSongItemAdapter.setOnItemHitListener(new OnItemHitListener() {
            @Override
            public void onItemHit(int position, Music music) {
                oneApplication.selectMusic(music,position);
            }
        });
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_playlist_fragment,container,false);
        ButterKnife.bind(this,view);
        initialize();
        return view;
    }


    public void initialize(){

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
        }

        @Override
        public int getItemCount() {
            if(musicArrayList!=null){
                return musicArrayList.size();
            }
            return 0;
        }

    }

}

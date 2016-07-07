package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
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
import com.google.android.gms.auth.firstparty.shared.FACLConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/6/7.
 */

public class OnePlayListFragment extends Fragment {
    @BindView(R.id.one_play_recycler_list)
    public RecyclerView recyclerView;
    private MainActivity activity;
    private LinearLayoutManager linearLayoutManager;
    private OneSongItemAdapter oneSongItemAdapter;


    public void setSelectedPosition(int position){
        oneSongItemAdapter.setSelectedPosition(position);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        oneSongItemAdapter = new OneSongItemAdapter(activity.getSongArraylist());
        oneSongItemAdapter.setOnItemHitListener(new OnItemHitListener() {
            @Override
            public void onItemHit(int position, Music music) {
                activity.itemSelected(music,position);
            }
        });
        linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(oneSongItemAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
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
    private class OneSongItemAdapter extends RecyclerView.Adapter<OneSongItemAdapter.OneSongViewHolder>{
        private ArrayList<Music> musicArrayList;
        private OnItemHitListener onItemHitListener;
        private int selectedPosition = -1;
        public OneSongItemAdapter(ArrayList<Music> musicArrayList){
            this.musicArrayList = musicArrayList;
        }
        public void setOnItemHitListener(OnItemHitListener onItemHitListener){
            this.onItemHitListener = onItemHitListener;
        }
        public void setSelectedPosition(int selectedPosition){
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }

        @Override
        public OneSongItemAdapter.OneSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
            OneSongViewHolder oneSongViewHolder = new OneSongViewHolder(view);
            return oneSongViewHolder;
        }

        @Override
        public void onBindViewHolder(final OneSongViewHolder holder, int position) {
            Music music = musicArrayList.get(position);
            holder.singerTextView.setText(music.getArtist());
            holder.songTextView.setText(music.getDisplayName());
            if(selectedPosition==position){
                holder.stateImageView.setVisibility(View.VISIBLE);
            }else {
                holder.stateImageView.setVisibility(View.GONE);
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
            if(musicArrayList!=null){
                return musicArrayList.size();
            }
            return 0;
        }




        public class OneSongViewHolder extends RecyclerView.ViewHolder{
            private TextView singerTextView;
            private TextView songTextView;
            private ImageView stateImageView;

            public OneSongViewHolder(View itemView) {
                super(itemView);
                singerTextView = ButterKnife.findById(itemView,R.id.SingerxAlbum);
                songTextView = ButterKnife.findById(itemView,R.id.MusicName);
                stateImageView = ButterKnife.findById(itemView,R.id.sound);
            }

        }

    }

}

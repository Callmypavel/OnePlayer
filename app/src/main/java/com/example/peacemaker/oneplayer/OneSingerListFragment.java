package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.MainActivity;
import com.example.peacemaker.oneplayer.Music;
import com.example.peacemaker.oneplayer.R;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/6/10.
 */

public class OneSingerListFragment extends Fragment {
    @BindView(R.id.one_singer_recycler)
    public RecyclerView recyclerView;
    private MainActivity activity;
    private LinearLayoutManager linearLayoutManager;
    private OneSingerItemAdapter oneSingerItemAdapter;


    @Override
    public void onDestroy() {
//        int position = linearLayoutManager.findFirstVisibleItemPosition();
//        activity.singerSelectedPosition = position;
//        View view = recyclerView.getChildAt(position);
//        if (view != null) {
//            activity.singerSelectedOffset = view.getTop();
//        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        //Log.v("OneSingerListFragment","onDestroyView");
        super.onDestroyView();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        oneSingerItemAdapter = new OneSingerItemAdapter(activity.getSingerArraylist());
        oneSingerItemAdapter.setOnItemHitListener(new OnItemHitListener() {
            @Override
            public void onItemHit(int position, Music music) {
                activity.itemSelected(music,position);
            }
        });
        linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(oneSingerItemAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        recyclerView.setHasFixedSize(true);
        //linearLayoutManager.scrollToPositionWithOffset(activity.singerSelectedPosition, activity.singerSelectedOffset);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_singerlist_fragment,container,false);
        Log.v("OneSingerListFragment","onCreateView");
        ButterKnife.bind(this,view);
        initialize();
        return view;
    }


    public void initialize(){

    }
    private class OneSingerItemAdapter extends RecyclerView.Adapter<OneSingerItemAdapter.OneSingerViewHolder>{
        private ArrayList<Music> musicArrayList;
        private OnItemHitListener onItemHitListener;
        public OneSingerItemAdapter(ArrayList<Music> musicArrayList){
            this.musicArrayList = musicArrayList;
        }
        public void setOnItemHitListener(OnItemHitListener onItemHitListener){
            this.onItemHitListener = onItemHitListener;
        }

        @Override
        public OneSingerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_singerlist_item,parent,false);
            OneSingerViewHolder oneSingerViewHolder = new OneSingerViewHolder(view);
            return oneSingerViewHolder;
        }

        @Override
        public void onBindViewHolder(final OneSingerViewHolder holder, int position) {
            Music music = musicArrayList.get(position);
            holder.singerTextView.setText(music.getDisplayName());
                if(music.getSecondItems()!=null) {
                    holder.songsnumberTextView.setText(music.getSecondItems().size()+"首歌曲");
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




        public class OneSingerViewHolder extends RecyclerView.ViewHolder{
            private TextView singerTextView;
            private TextView songsnumberTextView;
            private ImageView singerImageView;

            public OneSingerViewHolder(View itemView) {
                super(itemView);
                singerTextView = ButterKnife.findById(itemView,R.id.one_singer_item_singername);
                songsnumberTextView = ButterKnife.findById(itemView,R.id.one_singer_item_songsnumber);
                singerImageView = ButterKnife.findById(itemView,R.id.one_singer_item_singerimage);

            }



        }



//        public class OneViewHolder{
//            private TextView singerTextView;
//            private TextView songsnumberTextView;
//            private ImageView singerImageView;
//
//            View getHolderView(View convertView,Activity activity,ViewGroup parent,Music music) {
//                OneViewHolder oneViewHolder;
//                if(convertView==null){
//                    convertView = activity.getLayoutInflater().inflate(R.layout.one_singerlist_item,parent,false);
//                    oneViewHolder = new OneViewHolder();
//                    oneViewHolder.singerTextView = ButterKnife.findById(convertView,R.id.one_singer_item_singername);
//                    oneViewHolder.songsnumberTextView = ButterKnife.findById(convertView,R.id.one_singer_item_songsnumber);
//                    oneViewHolder.singerImageView = ButterKnife.findById(convertView,R.id.one_singer_item_singerimage);
//                    convertView.setTag(oneViewHolder);
//                }else {
//                    oneViewHolder = (OneViewHolder) convertView.getTag();
//                }
//                oneViewHolder.singerTextView.setText(music.getDisplayName());
//                if(music.getSecondItems()!=null) {
//                    oneViewHolder.songsnumberTextView.setText(music.getSecondItems().size()+"首歌曲");
//                }
//                return convertView;
//            }
//
//
//        }

    }

}

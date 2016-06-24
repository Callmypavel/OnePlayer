package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    @BindView(R.id.one_play_list)
    public ListView onePlayListView;
    private MainActivity activity;
    private OneMusicItemAdapter oneMusicItemAdapter;
    private boolean isEnable = true;
    private int position;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        if(oneMusicItemAdapter!=null){
            oneMusicItemAdapter.addAll(activity.getSongArraylist());
        }
        oneMusicItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        oneMusicItemAdapter.setSelectedPosition(position);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_playlist_fragment,container,false);
        ButterKnife.bind(this,view);
        initialize();
        return view;
    }
    public void setSelectedPosition(int position){
        this.position = position;
        if(oneMusicItemAdapter!=null) {
            oneMusicItemAdapter.setSelectedPosition(position);
        }
    }
    public void disable(){
//        if(onePlayListView!=null) {
//            onePlayListView.setEnabled(false);
//            onePlayListView.setClickable(false);
//        }
        isEnable = false;
    }
    public void enable(){
//        if(onePlayListView!=null) {
//            onePlayListView.setEnabled(true);
//            onePlayListView.setClickable(true);
//        }
        isEnable = true;
    }
    public void initialize(){
        oneMusicItemAdapter = new OneMusicItemAdapter(getActivity());
        onePlayListView.setAdapter(oneMusicItemAdapter);
        onePlayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(oneMusicItemAdapter!=null) {
                    oneMusicItemAdapter.setSelectedPosition(position);
                    Music music = oneMusicItemAdapter.getItem(position);
                    activity.changeTemp(2);
                    activity.itemSelected(music,position);
                }
            }
        });
    }
    public class OneMusicItemAdapter extends ArrayAdapter<Music> {
        private int selectedPosition=-1;
        private Activity activity;

        @Override
        public boolean areAllItemsEnabled() {
            return isEnable;
        }

        public OneMusicItemAdapter(Activity context) {
            super(context, R.layout.music_item, new ArrayList<Music>());
            activity = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Music music = getItem(position);
            int state;
            if(position==selectedPosition){
                state = 1;
            }else {
                state = 0;
            }
            OneViewHolder oneViewHolder = new OneViewHolder();
            return oneViewHolder.getHolderView(convertView,activity,parent,music,state);
        }
        public void setSelectedPosition(int selectedPosition){
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }


        public class OneViewHolder{
            private int notplaying = 0;
            private int playing = 1;
            private int stateTag = 5;
            private TextView singerTextView;
            private TextView songTextView;
            private ImageView stateImageView;

            View getHolderView(View convertView,Activity activity,ViewGroup parent,Music music,int state) {
                OneViewHolder oneViewHolder;
                Integer cacheState = notplaying;
                if(convertView==null){
                    convertView = activity.getLayoutInflater().inflate(R.layout.music_item,parent,false);
                    oneViewHolder = new OneViewHolder();
                    oneViewHolder.singerTextView = ButterKnife.findById(convertView,R.id.SingerxAlbum);
                    oneViewHolder.songTextView = ButterKnife.findById(convertView,R.id.MusicName);
                    oneViewHolder.stateImageView = ButterKnife.findById(convertView,R.id.sound);
                    convertView.setTag(oneViewHolder);
                }else {
                    oneViewHolder = (OneViewHolder) convertView.getTag();
                    cacheState = (Integer) convertView.getTag(R.id.sound);
                }
                oneViewHolder.singerTextView.setText(music.getArtist());
                oneViewHolder.songTextView.setText(music.getDisplayName());
                if(cacheState==null||cacheState!=state){
                    //当回收状态与实际状态不符合，需要重新设置drawable
                    if (state==notplaying){
                        oneViewHolder.stateImageView.setVisibility(View.GONE);
                    }else if (state==playing){
                        oneViewHolder.stateImageView.setVisibility(View.VISIBLE);
                    }
                    convertView.setTag(R.id.sound,state);
                }

                return convertView;
            }


        }

    }

}

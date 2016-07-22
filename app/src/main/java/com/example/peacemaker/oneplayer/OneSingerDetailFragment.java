package com.example.peacemaker.oneplayer;

import android.app.Activity;
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
import com.example.peacemaker.oneplayer.OnItemClickListener;
import com.example.peacemaker.oneplayer.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ouyan on 2016/6/17.
 */

public class OneSingerDetailFragment extends Fragment {
    @BindView(R.id.one_singer_detail_singername)
    public TextView singerNameTextView;
    @BindView(R.id.one_singer_detail_songsnumber)
    public TextView songsNumberTextView;
    @BindView(R.id.one_singer_detail_listview)
    public ListView singerDetailListView;
    private Music dataMusic;
    //private int selectedPosition = -1;
    private MainActivity activity;
    private Unbinder unbinder;
    //private boolean isEnable;
    private OneMusicItemAdapter oneMusicItemAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_singer_detail_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        //initialize();
        return view;
    }

    @Override
    public void onStart() {
//        if(activity.isPlayView){
//            disable();
//        }else {
//            enable();
//        }
        //oneMusicItemAdapter.setSelectedPosition(position);
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
//    public void setSelectedPosition(int position){
//        this.selectedPosition = position;
////        if(oneMusicItemAdapter!=null) {
////            oneMusicItemAdapter.setSelectedPosition(position);
////        }
//    }

//    @Override
//    public void onDestroy() {
//        this.selectedPosition = -1;
//        super.onDestroy();
//    }
    //    public void disable(){
////        if (singerDetailListView!=null) {
////            singerDetailListView.setEnabled(false);
////            singerDetailListView.setClickable(false);
////        }
//        isEnable = false;
//        if(oneMusicItemAdapter!=null) {
//            oneMusicItemAdapter.notifyDataSetChanged();
//        }
//    }
//    public void enable(){
////        if (singerDetailListView!=null) {
////            singerDetailListView.setEnabled(true);
////            singerDetailListView.setClickable(true);
////        }
//        isEnable = true;
//        if(oneMusicItemAdapter!=null) {
//            oneMusicItemAdapter.notifyDataSetChanged();
//        }
//    }

//    private void initialize(){
//        oneMusicItemAdapter = new OneMusicItemAdapter(getActivity());
//        singerDetailListView.setAdapter(oneMusicItemAdapter);
//        singerDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Music music = oneMusicItemAdapter.getItem(position);
//                activity.changeTemp(0);
//                activity.itemSelected(music,position);
//            }
//        });
//        Music music = activity.getDetailTarget();
//        singerNameTextView.setText(music.getDisplayName());
//        if(music.getSecondItems()!=null) {
//            songsNumberTextView.setText(music.getSecondItems().size() + "首歌曲");
//        }
//        oneMusicItemAdapter.addAll(music.getSecondItems());
//    }
    public class OneMusicItemAdapter extends ArrayAdapter<Music> {
//        @Override
//        public boolean areAllItemsEnabled() {
//            return isEnable;
//        }

        public OneMusicItemAdapter(Activity context) {
            super(context, R.layout.music_item, new ArrayList<Music>());
            activity = (MainActivity) context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            int state;
//            if(position==selectedPosition){
//                state = 1;
//            }else {
//                state = 0;
//            }
            Music music = getItem(position);
            OneMusicItemAdapter.OneViewHolder oneViewHolder = new OneMusicItemAdapter.OneViewHolder();
            return oneViewHolder.getHolderView(convertView,activity,parent,music);
        }
//        public void setSelectedPosition(int selectedPosition){
//            this.selectedPosition = selectedPosition;
//            notifyDataSetChanged();
//        }


        public class OneViewHolder{
            private int notplaying = 0;
            private int playing = 1;
            private TextView singerTextView;
            private TextView songTextView;
            //private ImageView stateImageView;
            View getHolderView(View convertView,Activity activity,ViewGroup parent,Music music) {
                OneMusicItemAdapter.OneViewHolder oneViewHolder;
                Integer cacheState = notplaying;
                if(convertView==null){
                    convertView = activity.getLayoutInflater().inflate(R.layout.music_item,parent,false);
                    oneViewHolder = new OneMusicItemAdapter.OneViewHolder();
                    oneViewHolder.singerTextView = ButterKnife.findById(convertView,R.id.SingerxAlbum);
                    oneViewHolder.songTextView = ButterKnife.findById(convertView,R.id.MusicName);
                    //oneViewHolder.stateImageView = ButterKnife.findById(convertView,R.id.sound);
                    convertView.setTag(oneViewHolder);
                }else {
                    oneViewHolder = (OneMusicItemAdapter.OneViewHolder) convertView.getTag();
                    cacheState = (Integer) convertView.getTag(R.id.sound);
                }
                oneViewHolder.singerTextView.setText(music.getArtist());
                oneViewHolder.songTextView.setText(music.getDisplayName());
//                if(cacheState==null||cacheState!=state){
//                    //当回收状态与实际状态不符合，需要重新设置drawable
//                    if (state==notplaying){
//                        oneViewHolder.stateImageView.setVisibility(View.GONE);
//                    }else if (state==playing){
//                        oneViewHolder.stateImageView.setVisibility(View.VISIBLE);
//                    }
//                    convertView.setTag(R.id.sound,state);
//                }
                return convertView;
            }


        }

    }
}

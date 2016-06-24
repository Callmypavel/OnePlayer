package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.MutableInt;
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
import com.example.peacemaker.oneplayer.MusicService;
import com.example.peacemaker.oneplayer.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/6/10.
 */

public class OneAblumDetailFragment extends Fragment{
    @BindView(R.id.one_albumlist_album_detail_image)
    ImageView albumImageView;
    @BindView(R.id.one_albumlist_album_detail_albumname)
    TextView albumNameTextView;
    @BindView(R.id.one_albumlist_album_detail_songsnumber)
    TextView albumDetailTextView;
    @BindView(R.id.one_albumlist_album_detail_singername)
    TextView singerNameTextView;
    @BindView(R.id.one_albumlist_album_detail_listview)
    ListView albumDetailListView;
    //private int selectedPosition = -1;
    private MainActivity activity;
    //private boolean isEnable = true;
    private OneMusicItemAdapter oneMusicItemAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_album_detail_fragment,container,false);
        ButterKnife.bind(this,view);
        initialize();
        return view;
    }
//    public void setSelectedPosition(int position){
//        this.selectedPosition = position;
//
//    }

    @Override
    public void onStart() {
       // oneMusicItemAdapter.setSelectedPosition(position);
        super.onStart();
    }

//    @Override
//    public void onDestroy() {
//        selectedPosition = -1;
//        super.onDestroy();
//    }

//    public void disable(){
////        if(albumDetailListView!=null) {
////            albumDetailListView.setEnabled(false);
////            albumDetailListView.setClickable(false);
////        }
//        isEnable = false;
//    }
//    public void enable(){
////        if(albumDetailListView!=null) {
////            albumDetailListView.setEnabled(true);
////            albumDetailListView.setClickable(true);
////        }
//        isEnable = true;
//    }
    private void initialize(){
        oneMusicItemAdapter = new OneMusicItemAdapter(getActivity());
        albumDetailListView.setAdapter(oneMusicItemAdapter);
        albumDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Music music = oneMusicItemAdapter.getItem(position);
                    activity.changeTemp(1);
                    activity.itemSelected(music,position);
            }
        });
        Music music = activity.getDetailTarget();
        oneMusicItemAdapter.addAll(music.getSecondItems());
        albumImageView.setImageBitmap(music.getSecondItems().get(0).getAlbumBitmap(activity));
        albumNameTextView.setText(music.getDisplayName());
        albumDetailTextView.setText(music.getSecondItems().size()+"首歌曲");
        singerNameTextView.setText(music.getSecondItems().get(0).getArtist());
    }
//    public void setDataMusic(Music music){
//        dataMusic = music;
//        albumImageView.setImageBitmap(dataMusic.getAlbumBitmap(activity));
//        albumNameTextView.setText(music.getDisplayName());
//        albumDetailTextView.setText(music.getSecondItems().size()+"首歌曲");
//    }
    public class OneMusicItemAdapter extends ArrayAdapter<Music> {

        public OneMusicItemAdapter(Activity context) {
            super(context, R.layout.music_list, new ArrayList<Music>());
            activity = (MainActivity) context;
        }

//        @Override
//        public boolean areAllItemsEnabled() {
//            return isEnable;
//        }

        @Override
            public View getView(int position, View convertView, ViewGroup parent) {
            Music music = getItem(position);
//            int state;
//            if(position==selectedPosition){
//                state = 1;
//            }else {
//                state = 0;
//            }
                OneViewHolder oneViewHolder = new OneViewHolder();
                return oneViewHolder.getHolderView(convertView,activity,parent,music);
            }
//    public void setSelectedPosition(int selectedPosition){
//        this.selectedPosition = selectedPosition;
//        notifyDataSetChanged();
//      }


            public class OneViewHolder {
                private int notplaying = 0;
                private int playing = 1;
                private TextView singerTextView;
                private TextView songTextView;
                //private ImageView stateImageView;

                View getHolderView(View convertView, Activity activity, ViewGroup parent, Music music) {
                    OneViewHolder oneViewHolder;
                    Integer cacheState = notplaying;
                    if (convertView == null) {
                        convertView = activity.getLayoutInflater().inflate(R.layout.music_item, parent, false);
                        oneViewHolder = new OneViewHolder();
                        oneViewHolder.singerTextView = ButterKnife.findById(convertView, R.id.SingerxAlbum);
                        oneViewHolder.songTextView = ButterKnife.findById(convertView, R.id.MusicName);
                        //oneViewHolder.stateImageView = ButterKnife.findById(convertView, R.id.sound);
                        convertView.setTag(oneViewHolder);
                    } else {
                        oneViewHolder = (OneViewHolder) convertView.getTag();
                        //cacheState = (Integer) convertView.getTag(R.id.sound);
                    }
                    oneViewHolder.singerTextView.setText(music.getArtist());
                    oneViewHolder.songTextView.setText(music.getDisplayName());
//                    if(cacheState==null||cacheState!=state){
//                        //当回收状态与实际状态不符合，需要重新设置drawable
//                        if (state==notplaying){
//                            oneViewHolder.stateImageView.setVisibility(View.GONE);
//                        }else if (state==playing){
//                            oneViewHolder.stateImageView.setVisibility(View.VISIBLE);
//                        }
//                        convertView.setTag(R.id.sound,state);
//                    }
                    return convertView;
                }
            }



    }
}

package com.example.peacemaker.oneplayer;

import android.graphics.Outline;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ouyan_000 on 2015/8/19.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>  {
    protected LayoutInflater layoutInflater;
    protected ArrayList<Music> musicArrayList;
    protected OnItemClickListener mListener;
    protected String url = "";
    protected String album = "";
    protected String singer = "";
    protected Boolean isTwolayer = false;
    protected ArrayList<ArrayList<Music>> musicArraylists;
    protected ArrayList<String> items;
    protected Boolean isItem = false;

    public void setlayer(Boolean isTwolayer){
        this.isTwolayer = isTwolayer;
    }
    public void setmusicArraylists(ArrayList<ArrayList<Music>> musicArraylists){
        this.musicArraylists = musicArraylists;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
        public TextView nameTextView;
        public TextView singerTextView;
        public ImageView imageView;
        public RecyclerView detailSongs;

        protected OnItemClickListener mListener;

        public ViewHolder(View v,OnItemClickListener listener) {
            super(v);
            Log.v("PlaylistAdapter","初始化viewholder");
            view = v;
            mListener = listener;
            nameTextView = (TextView)v.findViewById(R.id.MusicName);
            singerTextView = (TextView)v.findViewById(R.id.SingerxAlbum);
            imageView = (ImageView)v.findViewById(R.id.sound);
//            detailSongs = (RecyclerView)v.findViewById(R.id.detailSongs);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                Log.v("PlaylistAdapter",getPosition()+"待老夫阵前打飞机");
                mListener.onItemClick(v,getPosition());
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.music_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(convertView,mListener);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (!isItem) {
            if (musicArrayList != null) {
                String name = musicArrayList.get(position).getDisplayName();
                String singer = musicArrayList.get(position).getArtist();
                String album = musicArrayList.get(position).getAlbum();
                holder.nameTextView.setText(name + "");
                holder.singerTextView.setText(singer + " - " + album);
                if (musicArrayList.get(position).getUrl().equals(this.url)) {
                    holder.imageView.setVisibility(View.VISIBLE);
                } else {
                    holder.imageView.setVisibility(View.GONE);
                }
            }
        }else {
            String name = items.get(position);
            holder.nameTextView.setText(name + "");
            holder.imageView.setVisibility(View.GONE);
            holder.singerTextView.setText("");
            if (items.get(position).equals(this.singer)
                    ||items.get(position).equals(this.album)) {
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        }
//            if(isTwolayer){
//                //初始化recyclerview
//                Log.v("PlaylistAdapter","两层");
//                //holder.indexTextView.setText();
//                PlaylistAdapter detailplaylistAdapter = new PlaylistAdapter(musicArraylists.get(position),layoutInflater);
//                holder.detailSongs.setAdapter(detailplaylistAdapter);
//
//            }
//        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                outline.setRoundRect(0,0,220,80,3);
//            }
//        };
//        holder.view.setOutlineProvider(viewOutlineProvider);

    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        Log.v("PlaylistAdapter","你若好好设置监听，良辰必有重谢");
        mListener = itemClickListener;
    }



    public PlaylistAdapter(ArrayList<Music> musicArrayList,LayoutInflater layoutInflater){
        this.layoutInflater = layoutInflater;
        this.musicArrayList = musicArrayList;
    }

    public PlaylistAdapter(LayoutInflater layoutInflater){
        this.layoutInflater = layoutInflater;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (!isItem) {
            if (musicArrayList != null) {
                return musicArrayList.size();
            }
        }else {
            return items.size();
        }
        return 0;
    }

    public void setDatasource(ArrayList<Music> musicArrayList){
        this.musicArrayList = musicArrayList;
        isItem = false;
        for(Music music : musicArrayList) {
            Log.v("PlayListAdapter","歌曲名"+music.getDisplayName() );
        }
        notifyDataSetChanged();
    }
    public void setDataSource(ArrayList<String> items){
        this.items = items;
        isItem = true;
        notifyDataSetChanged();
    }
}

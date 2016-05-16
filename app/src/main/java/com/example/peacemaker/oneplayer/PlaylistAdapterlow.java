package com.example.peacemaker.oneplayer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ouyan_000 on 2015/8/19.
 */
public class PlaylistAdapterlow extends BaseAdapter {
    protected LayoutInflater layoutInflater;
    protected ArrayList<String> items;
    protected ArrayList<Music> musicArrayList;
    protected Boolean isItem = false;
    protected String url = "";
    protected String singer;
    protected String album;

    public PlaylistAdapterlow(ArrayList<Music> musicArrayList, LayoutInflater layoutInflater){
        this.layoutInflater = layoutInflater;
        this.musicArrayList = musicArrayList;
    }

    @Override
    public int getCount() {
        if (!isItem) {
            if (musicArrayList != null) {
                return musicArrayList.size();
            }
        }else {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Log.v("获取view", " " + position);
        if(convertView==null){
            System.out.println("加载布局");
            convertView = layoutInflater.inflate(R.layout.music_item,null);
            holder = new ViewHolder();
            holder.nameTextView = (TextView)convertView.findViewById(R.id.MusicName);
            holder.singerTextView = (TextView)convertView.findViewById(R.id.SingerxAlbum);
            holder.imageView = (ImageView)convertView.findViewById(R.id.sound);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (!isItem) {
            if (musicArrayList != null) {
                String name = musicArrayList.get(position).getDisplayName();
                String singer = musicArrayList.get(position).getArtist();
                String album = musicArrayList.get(position).getAlbum();
                holder.nameTextView.setText(name + "");
                holder.singerTextView.setText(singer + " - " + album);
                if (musicArrayList.get(position).getUrl().equals(this.url)) {
                    ImageView imageView = (ImageView)convertView.findViewById(R.id.sound);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    ImageView imageView = (ImageView)convertView.findViewById(R.id.sound);
                    imageView.setVisibility(View.GONE);
                }
            }
        }else {
            String name = items.get(position);
            holder.nameTextView.setText(name + "");
            holder.imageView.setVisibility(View.GONE);
            holder.singerTextView.setText("");
        }
        return convertView;
    }
    public void setDatasource(ArrayList<Music> musicArrayList){
        this.musicArrayList = musicArrayList;
        isItem = false;
        notifyDataSetChanged();
    }
    public void setDataSource(ArrayList<String> items){
        this.items = items;
        isItem = true;
        notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView nameTextView;
        public TextView singerTextView;
        public ImageView imageView;

    }


}



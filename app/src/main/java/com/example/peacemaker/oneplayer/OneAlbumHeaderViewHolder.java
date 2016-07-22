package com.example.peacemaker.oneplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.peacemaker.oneplayer.databinding.OneAlbumDetailHeaderBinding;

/**
 * Created by ouyan on 2016/7/20.
 */

public class OneAlbumHeaderViewHolder extends RecyclerView.ViewHolder{
    private OneAlbumDetailHeaderBinding binding;
    public OneAlbumHeaderViewHolder(OneAlbumDetailHeaderBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    static OneAlbumHeaderViewHolder createHolder(LayoutInflater inflater, ViewGroup parent){
        OneAlbumDetailHeaderBinding binding = OneAlbumDetailHeaderBinding.inflate(inflater,parent,false);
        return new OneAlbumHeaderViewHolder(binding);
    }
    public void bind(Music music,int songsNumber){
        binding.setMusic(music);
        binding.setSongsNumber(songsNumber+"首歌曲");
        binding.executePendingBindings();
    }
}
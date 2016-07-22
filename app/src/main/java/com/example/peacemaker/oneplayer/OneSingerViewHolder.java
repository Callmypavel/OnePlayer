package com.example.peacemaker.oneplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.peacemaker.oneplayer.databinding.OneSingerlistItemBinding;

/**
 * Created by ouyan on 2016/7/19.
 */

public class OneSingerViewHolder extends RecyclerView.ViewHolder{
    private OneSingerlistItemBinding binding;
    public OneSingerViewHolder(OneSingerlistItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }
    public static OneSingerViewHolder createHolder(LayoutInflater inflater, ViewGroup parent){
        OneSingerlistItemBinding binding = OneSingerlistItemBinding.inflate(inflater,parent,false);
        return new OneSingerViewHolder(binding);
    }
    public void bind(Music music,int musicNumber){
        binding.setMusic(music);
        binding.setSongsnumber(musicNumber+"首歌曲");
        binding.executePendingBindings();
    }



}
package com.example.peacemaker.oneplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.databinding.MusicItemBinding;

import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/7/19.
 */

public class OneSongViewHolder extends RecyclerView.ViewHolder {
    private MusicItemBinding binding;
    public OneSongViewHolder(MusicItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }
    public static OneSongViewHolder createHolder(LayoutInflater inflater, ViewGroup parent){
        MusicItemBinding binding = MusicItemBinding.inflate(inflater,parent,false);
        return new OneSongViewHolder(binding);
    }
    public void bind(Music music,boolean isShow){
        binding.setIsShow(isShow);
        binding.setMusic(music);
        binding.executePendingBindings();
    }
    public void nullBind(Music music){
        binding.setMusic(music);
        binding.executePendingBindings();
    }
}

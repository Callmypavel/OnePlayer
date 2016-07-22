package com.example.peacemaker.oneplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.databinding.OneAblumlistItemBinding;

import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/7/19.
 */

public class OneAblumViewHolder extends RecyclerView.ViewHolder {
    public ImageView albumImageView;
    private OneAblumlistItemBinding binding;
    public OneAblumViewHolder(OneAblumlistItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        albumImageView = ButterKnife.findById(itemView, R.id.one_albumlist_album_image);
    }
    public static OneAblumViewHolder createHolder(LayoutInflater inflater, ViewGroup parent){
        OneAblumlistItemBinding binding = OneAblumlistItemBinding.inflate(inflater,parent,false);

        return new OneAblumViewHolder(binding);
    }
    public void bind(Music music,int musicNumber){
        binding.setMusic(music);
        binding.setSongsnumber(musicNumber+"首歌曲");
        binding.executePendingBindings();
    }
}
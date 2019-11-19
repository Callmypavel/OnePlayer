package peacemaker.oneplayer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import peacemaker.oneplayer.databinding.ItemMusicBinding;
import peacemaker.oneplayer.entity.Music;


/*
 * Created by ouyan on 2016/7/19.
 */

public class OneSongViewHolder extends RecyclerView.ViewHolder {
    private ItemMusicBinding binding;
    private OneSongViewHolder(ItemMusicBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }
    public static OneSongViewHolder createHolder(LayoutInflater inflater, ViewGroup parent){
        ItemMusicBinding binding = ItemMusicBinding.inflate(inflater,parent,false);
        return new OneSongViewHolder(binding);
    }
    public void bind(Music music, boolean isShow){
//        binding.setIsShow(isShow);
//        binding.setMusic(music);
        binding.executePendingBindings();
    }
    public void nullBind(Music music){
//        binding.setMusic(music);
        binding.executePendingBindings();
    }
}

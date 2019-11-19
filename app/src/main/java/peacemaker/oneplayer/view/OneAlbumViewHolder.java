package peacemaker.oneplayer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import peacemaker.oneplayer.R;
import peacemaker.oneplayer.databinding.ItemAlbumlistBinding;

import peacemaker.oneplayer.entity.Music;



/**
 * Created by ouyan on 2016/7/19.
 */

public class OneAlbumViewHolder extends RecyclerView.ViewHolder {
    public ImageView albumImageView;
    private ItemAlbumlistBinding binding;
    public OneAlbumViewHolder(ItemAlbumlistBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        albumImageView = (ImageView) itemView.findViewById(R.id.one_albumlist_album_image);
    }
    public static OneAlbumViewHolder createHolder(LayoutInflater inflater, ViewGroup parent){
        ItemAlbumlistBinding binding = ItemAlbumlistBinding.inflate(inflater,parent,false);

        return new OneAlbumViewHolder(binding);
    }
    public void bind(Music music, int musicNumber){
//        binding.setMusic(music);
//        binding.setSongsnumber(musicNumber+"首歌曲");
        binding.executePendingBindings();
    }
}
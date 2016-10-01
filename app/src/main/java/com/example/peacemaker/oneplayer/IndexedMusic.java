package com.example.peacemaker.oneplayer;

import java.util.ArrayList;

/**
 * Created by ouyan on 2016/9/17.
 */

public class IndexedMusic {
    private String indexName;
    private ArrayList<Music> musics;
    public IndexedMusic(String indexName,ArrayList<Music> musics){
        this.indexName = indexName;
        this.musics = musics;
    }


    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }

    public void addMusic(Music music){
        if(musics!=null){
            musics.add(music);
        }
    }
}

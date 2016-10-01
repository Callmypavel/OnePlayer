package com.example.peacemaker.oneplayer;

import android.os.Bundle;
import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by ouyan on 2016/9/17.
 */

public class IndexMusicTool {
    public static class IndexType{
        public static final int Singer = 1;
        public static final int Album = 2;
        public static final int Song = 3;
    }
    private int indexType;
    private ArrayList<Music> sortedMusics;
    private ArrayList<IndexedMusic> indexedMusics;

    public static ArrayList<IndexedMusic> getIndexedMusics(ArrayList<Music> sortedMusics, int indexType){
        ArrayList<IndexedMusic> indexedMusics = new ArrayList<>();
        if(sortedMusics!=null) {
            for (Music music : sortedMusics) {
                String initial = MandarinTool.getInitial(music.getDisplayName());
                IndexedMusic indexedMusic = getByInitial(initial, indexedMusics);
                if (indexedMusic == null) {
                    Log.v("IndexMusicTool", "新增" + initial + "字头" + music.getDisplayName());
                    ArrayList<Music> musics = new ArrayList<>();
                    musics.add(music);
                    indexedMusics.add(new IndexedMusic(initial, musics));
                } else {
                    Log.v("IndexMusicTool", "更新" + initial + "字头" + music.getDisplayName());
                    indexedMusic.addMusic(music);
                }
            }
        }
        return indexedMusics;
    }
    private static IndexedMusic getByInitial(String initial,ArrayList<IndexedMusic> indexedMusics){
        for(IndexedMusic indexedMusic : indexedMusics){
            if(indexedMusic.getIndexName().equals(initial)){
                return indexedMusic;
            }
        }
        return null;
    }
    private int getIndex(ArrayList<Music> musicArrayList,Music music){
        int index = -1;
        if(musicArrayList==null){
            return index;
        }
        for(int i=0;i<musicArrayList.size();i++){
            if (musicArrayList.get(i).getUrl().equals(music.getUrl())){
                index = i;
                return index;
            }
        }
        return index;
    }

}

package com.example.peacemaker.oneplayer;

/**
 * Created by 请叫我保尔 on 2015/10/9.
 */
public interface OnMusicListener {
    void onComple();
    void onMusicChanged(Music music);
    void onMusicTickling(int time);
    void onPrepared(int dutation);
    void onError(int what,int extra);
    void onWaveForm(byte[] data);

}

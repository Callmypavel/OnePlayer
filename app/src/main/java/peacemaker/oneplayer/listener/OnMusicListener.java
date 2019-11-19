package peacemaker.oneplayer.listener;

import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.entity.OneConfig;

import java.util.ArrayList;

/**
 * Created by 请叫我保尔 on 2015/10/9.
 */
public interface OnMusicListener {
    void onComple();
    void onMusicChanged(Music music);
    void onMusicTickling(float time);
    void onPrepared(int dutation);
    void onError(int what,int extra);
    void onWaveForm(byte[] data);
    void onPause();
    void onContinue();
    void onSoundEffectLoaded(OneConfig oneConfig);
    void onPlayModeChanged(int playMode);

}

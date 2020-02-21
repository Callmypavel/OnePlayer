package one.peace.oneplayer.global.config;

import android.content.Context;

import java.util.ArrayList;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import one.peace.oneplayer.BR;
import one.peace.oneplayer.database.AppDatabase;
import one.peace.oneplayer.util.ExecutorServiceUtil;

/**
 * Created by pavel on 2020/2/21.
 */
@Entity
public class SoundEffectConfig extends BaseObservable {
    @PrimaryKey
    private int soundEffectConfigId = 1;
    @ColumnInfo(name = "band_levels")
    private ArrayList<Integer> bandLevels;
    @ColumnInfo(name = "bassboost_strength")
    private int bassBoostStrength;
    @ColumnInfo(name = "preset_reverb")
    private int presetReverb;
    @ColumnInfo(name = "virtualizer_strength")
    private int virtualizerStrength;
    @ColumnInfo(name = "bandlevel_range")
    private short[] bandLevelRange;
    @ColumnInfo(name = "environmentreverb_config")
    private EnvironmentReverbConfig environmentReverbConfig;
    private static SoundEffectConfig sInstance;

    public interface SoundEffectConfigListener {
        void onSoundEffectConfigLoaded(SoundEffectConfig soundEffectConfig);
    }

    public static void getInstance(final Context context, final SoundEffectConfigListener listener) {
        ExecutorServiceUtil.submit(new Runnable() {
            @Override
            public void run() {
                if (sInstance == null) {
                    synchronized (Config.class) {
                        if (sInstance == null) {
                            sInstance = AppDatabase.getInstance(context).soundEffectConfigDao().getSoundEffectConfig();
                        }
                    }
                }
                //加载出来还是空
                if (sInstance == null) {
                    sInstance = new SoundEffectConfig();
                }
                listener.onSoundEffectConfigLoaded(sInstance);
            }
        });

    }

    public EnvironmentReverbConfig getEnvironmentReverbConfig() {
        return environmentReverbConfig;
    }

    public void setEnvironmentReverbConfig(EnvironmentReverbConfig environmentReverbConfig) {
        this.environmentReverbConfig = environmentReverbConfig;
    }


    public ArrayList<Integer> getBandLevels() {
        return bandLevels;
    }

    public void setBandLevels(ArrayList<Integer> bandLevels) {
        this.bandLevels = bandLevels;
    }

    public int getBassBoostStrenth() {
        return bassBoostStrength;
    }

    public void setBassBoostStrenth(int bassBoostStrenth) {
        this.bassBoostStrength = bassBoostStrenth;
        notifyPropertyChanged(one.peace.oneplayer.BR.bassBoostStrenth);
    }

    public int getPresetReverb() {
        return presetReverb;
    }

    public void setPresetReverb(int presetReverb) {
        this.presetReverb = presetReverb;
    }

    @Bindable
    public int getBassBoostStrength() {
        return bassBoostStrength;
    }

    public void setBassBoostStrength(int bassBoostStrength) {
        this.bassBoostStrength = bassBoostStrength;
        notifyPropertyChanged(one.peace.oneplayer.BR.bassBoostStrength);
    }

    @Bindable
    public int getVirtualizerStrength() {
        return virtualizerStrength;
    }

    public void setVirtualizerStrength(int virtualizerStrength) {
        this.virtualizerStrength = virtualizerStrength;
        notifyPropertyChanged(BR.virtualizerStrength);
    }

    @Bindable
    public short[] getBandLevelRange() {
        return bandLevelRange;
    }

    public void setBandLevelRange(short[] bandLevelRange) {
        this.bandLevelRange = bandLevelRange;
        notifyPropertyChanged(BR.bandLevelRange);
    }
}

package one.peace.oneplayer.global.config;


import java.util.ArrayList;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import one.peace.oneplayer.BR;
import one.peace.oneplayer.database.AppDatabase;

/**
 * Created by ouyan on 2016/9/24.
 */

@Entity
public class Config extends BaseObservable {
    @PrimaryKey
    private int configId = 1;
    @ColumnInfo(name = "theme_color")
    private int themeColor;
    @ColumnInfo(name = "band_levels")
    private ArrayList<Integer> bandLevels;
    @ColumnInfo(name = "bassboost_strength")
    private int bassBoostStrength;
    @ColumnInfo(name = "preset_reverb")
    private int presetReverb;
    @ColumnInfo(name = "virtualizer_strength")
    private int virtualizerStrength;

    public EnvironmentReverbConfig getEnvironmentReverbConfig() {
        return environmentReverbConfig;
    }

    public void setEnvironmentReverbConfig(EnvironmentReverbConfig environmentReverbConfig) {
        this.environmentReverbConfig = environmentReverbConfig;
        notifyPropertyChanged(BR.environmentReverbConfig);
    }

    private EnvironmentReverbConfig environmentReverbConfig;

    @Bindable
    public ArrayList<Integer> getBandLevels() {
        return bandLevels;
    }

    public void setBandLevels(ArrayList<Integer> bandLevels) {
        this.bandLevels = bandLevels;
        notifyPropertyChanged(BR.bandLevels);
    }
    @Bindable
    public int getBassBoostStrenth() {
        return bassBoostStrength;
    }

    public void setBassBoostStrenth(int bassBoostStrenth) {
        this.bassBoostStrength = bassBoostStrenth;
        notifyPropertyChanged(BR.bassBoostStrenth);
    }

    @Bindable
    public int getPresetReverb() {
        return presetReverb;
    }

    public void setPresetReverb(int presetReverb) {
        this.presetReverb = presetReverb;
        notifyPropertyChanged(BR.presetReverb);
    }

    @Bindable
    public int getVirtualizerStrenth() {
        return virtualizerStrength;
    }

    public void setVirtualizerStrenth(int virtualizerStrenth) {
        this.virtualizerStrength = virtualizerStrenth;
        notifyPropertyChanged(BR.virtualizerStrenth);
    }

    @Bindable
    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
        notifyPropertyChanged(BR.themeColor);

    }

    @Override
    public String toString() {
        return "主题色为"+themeColor+"bandLevels个数"+bandLevels.size()+super.toString();
    }
}

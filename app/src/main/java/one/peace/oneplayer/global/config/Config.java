package one.peace.oneplayer.global.config;


import android.content.Context;
import android.media.audiofx.EnvironmentalReverb;

import java.util.ArrayList;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import one.peace.oneplayer.BR;
import one.peace.oneplayer.database.AppDatabase;
import one.peace.oneplayer.database.dao.ConfigDAO;
import one.peace.oneplayer.music.player.MusicState;

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
    @ColumnInfo(name = "bandlevel_range")
    private short[] bandLevelRange;
    @ColumnInfo(name = "blur_radius")
    private int blurRadius;
    @ColumnInfo(name = "red_value")
    private int redValue;
    @ColumnInfo(name = "green_value")
    private int greenValue;
    @ColumnInfo(name = "blue_value")
    private int blueValue;
    @ColumnInfo(name = "alpha_value")
    private int alphaValue;
    @ColumnInfo(name = "environmentreverb_config")
    private EnvironmentReverbConfig environmentReverbConfig;

    private static Config sInstance;

    @TypeConverter
    public static String convertEnvironmentReverbConfig(EnvironmentReverbConfig environmentalReverbConfig){
        return environmentalReverbConfig.toString();
    }

    @TypeConverter
    public static EnvironmentReverbConfig convertString(String storedValue){

    }

    public static Config getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (Config.class) {
                if (sInstance == null) {
                    sInstance = AppDatabase.getInstance(context).configDao().getConfig();
                }
            }
        }
        return sInstance;
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
    }

    public int getPresetReverb() {
        return presetReverb;
    }

    public void setPresetReverb(int presetReverb) {
        this.presetReverb = presetReverb;
    }

    @Bindable
    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
        notifyPropertyChanged(BR.themeColor);

    }

    @Bindable
    public int getBassBoostStrength() {
        return bassBoostStrength;
    }

    public void setBassBoostStrength(int bassBoostStrength) {
        this.bassBoostStrength = bassBoostStrength;
        notifyPropertyChanged(BR.bassBoostStrength);
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

    @Bindable
    public int getBlurRadius() {
        return blurRadius;
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
        notifyPropertyChanged(BR.blurRadius);
    }

    @Bindable
    public int getRedValue() {
        return redValue;
    }

    public void setRedValue(int redValue) {
        this.redValue = redValue;
        notifyPropertyChanged(BR.redValue);
    }

    @Bindable
    public int getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
        notifyPropertyChanged(BR.greenValue);
    }

    @Bindable
    public int getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
        notifyPropertyChanged(BR.blueValue);
    }

    @Bindable
    public int getAlphaValue() {
        return alphaValue;
    }

    public void setAlphaValue(int alphaValue) {
        this.alphaValue = alphaValue;
        notifyPropertyChanged(BR.alphaValue);
    }

    @Override
    public String toString() {
        return "主题色为"+themeColor+"bandLevels个数"+bandLevels.size()+super.toString();
    }
}

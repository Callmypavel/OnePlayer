package peacemaker.oneplayer.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;

import peacemaker.oneplayer.entity.EnvironmentReverbConfig;
import peacemaker.oneplayer.BR;
import java.util.ArrayList;

/**
 * Created by ouyan on 2016/9/24.
 */

public class OneConfig extends BaseObservable{
    private int themeColor;
    private ArrayList<Integer> bandLevels;
    private int bassBoostStrength;
    private int presetReverb;
    private int virtualizerStrength;
    private short[] bandLevelRange;
    private int blurRadius;
    private int redColor;
    private int greenColor;
    private int blueColor;
    private int alphaColor;
    @Bindable
    public int getRedColor() {
        return redColor;
    }

    public void setRedColor(int redColor) {
        this.redColor = redColor;
        notifyPropertyChanged(BR.redColor);
    }
    @Bindable
    public int getGreenColor() {
        return greenColor;
    }

    public void setGreenColor(int greenColor) {
        this.greenColor = greenColor;
        notifyPropertyChanged(BR.greenColor);
    }
    @Bindable
    public int getBlueColor() {
        return blueColor;
    }

    public void setBlueColor(int blueColor) {
        this.blueColor = blueColor;
        notifyPropertyChanged(BR.blueColor);
    }
    @Bindable
    public int getAlphaColor() {
        return alphaColor;
    }

    public void setAlphaColor(int alphaColor) {
        this.alphaColor = alphaColor;
        notifyPropertyChanged(BR.alphaColor);
    }

    @Bindable
    public int getBlurRadius() {
        return blurRadius;
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
        notifyPropertyChanged(BR.blurRadius);
    }

    public short[] getBandLevelRange() {
        return bandLevelRange;
    }

    public void setBandLevelRange(short[] bandLevelRange) {
        this.bandLevelRange = bandLevelRange;
    }

    public EnvironmentReverbConfig getEnvironmentReverbConfig() {
        return environmentReverbConfig;
    }

    public void setEnvironmentReverbConfig(EnvironmentReverbConfig environmentReverbConfig) {
        this.environmentReverbConfig = environmentReverbConfig;
    }

    private EnvironmentReverbConfig environmentReverbConfig;
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
    public int getVirtualizerStrenth() {
        return virtualizerStrength;
    }

    public void setVirtualizerStrenth(int virtualizerStrenth) {
        this.virtualizerStrength = virtualizerStrenth;
    }

    @Bindable
    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
        updateColorElements(themeColor);
        notifyPropertyChanged(BR.themeColor);
    }
    private void updateColorElements(int themeColor){
        setRedColor(Color.red(themeColor));
        setGreenColor(Color.green(themeColor));
        setBlueColor(Color.blue(themeColor));
        setAlphaColor(Color.alpha(themeColor));
    }


    @Override
    public String toString() {
        return "主题色为"+themeColor+"bandLevels个数"+bandLevels.size()+super.toString();
    }
}

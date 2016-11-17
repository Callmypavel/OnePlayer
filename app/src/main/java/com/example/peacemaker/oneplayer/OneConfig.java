package com.example.peacemaker.oneplayer;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

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
        notifyPropertyChanged(BR.themeColor);
    }

    @Override
    public String toString() {
        return "主题色为"+themeColor+"bandLevels个数"+bandLevels.size()+super.toString();
    }
}

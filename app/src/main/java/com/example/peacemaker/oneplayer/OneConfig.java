package com.example.peacemaker.oneplayer;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by ouyan on 2016/9/24.
 */

public class OneConfig extends BaseObservable{
    private int themeColor;
    public OneConfig(int themeColor){
        this.themeColor = themeColor;
    }
    @Bindable
    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
        notifyPropertyChanged(BR.themeColor);
    }

}

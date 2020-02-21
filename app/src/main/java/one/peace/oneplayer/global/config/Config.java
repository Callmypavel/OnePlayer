package one.peace.oneplayer.global.config;


import android.content.Context;
import android.graphics.Color;
import android.media.audiofx.EnvironmentalReverb;

import com.google.gson.Gson;

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
import one.peace.oneplayer.util.ExecutorServiceUtil;
import one.peace.oneplayer.util.StringUtil;

/**
 * Created by ouyan on 2016/9/24.
 */

@Entity
public class Config extends BaseObservable {
    @PrimaryKey
    private int configId = 1;
    @ColumnInfo(name = "theme_color")
    private int themeColor = Color.GREEN;
    @ColumnInfo(name = "blur_radius")
    private int blurRadius = 5;
    @ColumnInfo(name = "red_value")
    private int redValue;
    @ColumnInfo(name = "green_value")
    private int greenValue;
    @ColumnInfo(name = "blue_value")
    private int blueValue;
    @ColumnInfo(name = "alpha_value")
    private int alphaValue;


    private static Config sInstance;

    public interface ConfigListener {
        void onConfigLoaded(Config config);
    }

    public static void getInstance(final Context context, final ConfigListener listener) {
        ExecutorServiceUtil.submit(new Runnable() {
            @Override
            public void run() {
                if (sInstance == null) {
                    synchronized (Config.class) {
                        if (sInstance == null) {
                            sInstance = AppDatabase.getInstance(context).configDao().getConfig();
                        }
                    }
                }
                //加载出来还是空
                if (sInstance == null) {
                    sInstance = new Config();
                }
                listener.onConfigLoaded(sInstance);
            }
        });

    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
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
        return "主题色为" + themeColor + super.toString();
    }
}

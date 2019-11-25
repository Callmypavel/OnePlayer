package one.peace.oneplayer.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by ouyan on 2016/6/23.
 */

public class ColorUtil {
    public static int getColorByResId(Activity activity, int resId){
        return activity.getResources().getColor(resId);
    }
    public static int getColorByResId(Context context, int resId){
        return context.getResources().getColor(resId);
    }
    public static boolean getContrast(int color1,int color2){
        float luminance1 = getLuminance(Color.red(color1), Color.green(color1), Color.blue(color1));
        float luminance2 = getLuminance(Color.red(color2), Color.green(color2), Color.blue(color2));
        float contrast;
        if(luminance1>luminance2){
            contrast = (luminance1+0.05f)/(luminance2+0.05f);
        }else {
            contrast = (luminance2+0.05f)/(luminance1+0.05f);
        }
        Log.v("ColorUtil","对比度"+contrast);
        if(contrast>4.5){

            return true;
        }else return false;
    }
    public static float getLuminance(int Red,int Green,int Blue){
        float rRgb = Red;
        float gRgb = Green;
        float bRgb = Blue;
        float fixedRed = getFixedValue(rRgb/255);
        float fixedGreen = getFixedValue(gRgb/255);
        float fixedBlue = getFixedValue(bRgb/255);
        float luminance = (float)(0.2126*fixedRed+0.7152*fixedGreen+0.0722*fixedBlue);
        return luminance;
    }
    public static float ten2eight(int ten){
        return Float.parseFloat(Integer.toOctalString(ten));
    }
    public static float getFixedValue(float value){
        float fixedValue;
        if(value<=0.03928){
            fixedValue = (float) (value/12.92);
        }else {
            fixedValue = (float) Math.pow((float)((value+0.055)/1.055),2.4f);
        }
        return fixedValue;
    }

}

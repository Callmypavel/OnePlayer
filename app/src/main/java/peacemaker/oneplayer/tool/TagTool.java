package peacemaker.oneplayer.tool;

import android.content.Context;

import peacemaker.oneplayer.R;

/**
 * Created by ouyang on 2016/11/26.
 */

public class TagTool {
    public static String redTag;
    public static String blueTag;
    public static String greenTag;
    public static String alphaTag;
    public static String radiusTag;

    public static void init(Context context){
        redTag = StringUtil.getString(context, R.string.color_red_tag);
        blueTag = StringUtil.getString(context,R.string.color_blue_tag);
        greenTag = StringUtil.getString(context,R.string.color_green_tag);
        alphaTag = StringUtil.getString(context,R.string.alpha_tag);
        radiusTag = StringUtil.getString(context,R.string.radius_tag);
    }
}

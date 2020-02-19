package one.peace.oneplayer.util;

import android.content.Context;

import com.github.promeg.pinyinhelper.Pinyin;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;
import one.peace.oneplayer.global.config.EnvironmentReverbConfig;

/**
 * Created by ouyan on 2016/10/9.
 */

public class StringUtil {
    private static Comparator<String> mMandarinComparator;
    private static String TAG = "StringUtil";
    private static Gson gson;


    public static String getString(Context context,int resId){
        return context.getResources().getString(resId);
    }

    public static String ten2sixty(float ten) {
        String sixty;
        String minute = (int)ten / 60 + "";
        String second = (int)ten % 60 + "";
        if (ten / 60 >= 0 && ten / 60 < 10) {
            minute = "0" + minute;
        }
        if (ten % 60 >= 0 && ten % 60 < 10) {
            second = "0" + second;
        }
        sixty = minute + ":" + second;
        return sixty;
    }

    public static String entityToJson(Object object) {
        if (object == null) {
            return "null";
        }
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(object);
    }

    public static Object jsonToEntity(String jsonString, Class clazz) {
        if (jsonString == null) {
            return "null";
        }
        if (gson == null) {
            gson = new Gson();
        }
        Object object = gson.fromJson(jsonString, clazz);
        return object;
    }

    public static String shortArrayToString(short[] objects) {
        if (objects == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object element : objects) {
            stringBuilder.append(element.toString());
            stringBuilder.append(",");
        }
        String result = stringBuilder.toString();
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public static String listToString(ArrayList arrayList) {
        if (arrayList == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object element : arrayList) {
            stringBuilder.append(element.toString());
            stringBuilder.append(",");
        }
        String result = stringBuilder.toString();
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public static ArrayList stringToIntList(String text) {
        if (text == null) {
            return null;
        }
        String[] elementTexts = text.split(",");
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (String elementText : elementTexts) {
            arrayList.add(Integer.parseInt(elementText));
        }
        return arrayList;
    }

    public static short[] stringToShortArray(String text) {
        if (text == null) {
            return null;
        }
        String[] elementTexts = text.split(",");
        short[] result = new short[elementTexts.length];
        for (int i = 0; i < elementTexts.length; i++) {
            result[i] = Short.parseShort(elementTexts[i]);
        }
        return result;
    }


    public static String getString(Context context,int resId,Object... args){
        String data = context.getResources().getString(resId);
        return String.format(data,args);
    }

    public static boolean isEndWith(String[] strings,@NonNull String target){
        //LogTool.log(TAG, "我看看待查" + target);
        for (String string : strings) {
            if (target.endsWith(string)) {
                //LogTool.log(TAG, "好起来了");
                return true;
            }
        }
        //LogTool.log(TAG, "坏起来了");
        return false;
    }

    public static String upperFirstCharacter(String text){
        if (text == null || "".equals(text)){
            return "";
        }
        String firstCharacterString = text.substring(0,1);
        return firstCharacterString.toUpperCase()+text.substring(1);
    }

    public static Comparator<String> getMandarinComparator() {
        if (mMandarinComparator == null) {
            mMandarinComparator = new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    if (lhs != null && rhs != null) {
                        return StringUtil.mandarin2Pinyin(lhs).toLowerCase()
                                .compareTo(StringUtil.mandarin2Pinyin(rhs).toLowerCase());
                    } else {
                        return 0;
                    }
                }
            };
        }
        return mMandarinComparator;
    }

    public static String mandarin2Pinyin(String src){
        return Pinyin.toPinyin(src, "");
    }
    public static String mandarin2Pinyin(Object object){
        if (object instanceof String){
            return mandarin2Pinyin((String)object);
        }
        return "";
    }

}

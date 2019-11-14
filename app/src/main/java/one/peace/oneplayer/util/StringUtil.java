package one.peace.oneplayer.util;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Created by ouyan on 2016/10/9.
 */

public class StringUtil {
    public static String getString(Context context,int resId){
        return context.getResources().getString(resId);
    }
    public static String getString(Context context,int resId,Object... args){
        String data = context.getResources().getString(resId);
        return String.format(data,args);
    }
    public static boolean isEndWith(String[] strings,@NonNull String target){
        for (String string : strings) {
            if (string.endsWith(target)){
                return true;
            }
        }
        return false;
    }
    public static String upperFirstCharacter(String text){
        if (text == null || "".equals(text)){
            return "";
        }
        String firstCharacterString = text.substring(0,1);
        return firstCharacterString.toUpperCase()+text.substring(1);
    }

    public static String mandarin2Pinyin(String src){
        char[] t1 ;
        t1=src.toCharArray();
        String[] t2;
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4="";
        int t0=t1.length;
        try {
            for (int i=0;i<t0;i++)
            {
                //判断是否为汉字字符
                if(Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
                {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], outputFormat);
                    t4+=t2[0];
                }
                else
                    t4+= Character.toString(t1[i]);
            }
            return t4;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t4;
    }
    public static String mandarin2Pinyin(Object object){
        if (object instanceof String){
            return mandarin2Pinyin((String)object);
        }
        return "";
    }
}

package com.example.peacemaker.oneplayer;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * Created by ouyan on 2016/9/17.
 */

public class MandarinTool {
    public static String Mandarin2Pinyin(String src){
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
                if(java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
                {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], outputFormat);
                    t4+=t2[0];
                }
                else
                    t4+=java.lang.Character.toString(t1[i]);
            }
            return t4;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t4;
    }
    public static String getInitial(String chinese){
        String pinyin = Mandarin2Pinyin(chinese);
        String initial = String.valueOf(pinyin.toLowerCase().charAt(0));
        return initial;
    }
}

package one.peace.oneplayer.util;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ouyan on 2016/9/22.
 */

public class LogTool {
    private static Boolean isShowlog = true;
    private static Boolean isShowDetail = true;


    /*
    打印出代码所在的行数和调用位置
     */
    public static void log(String tag, String message) {
        if (isShowlog) {
            if (isShowDetail) {
                StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
                StackTraceElement lastElement = stackTraceElements[3];
                if ("LogTool.java".equals(lastElement.getFileName())) {
                    lastElement = stackTraceElements[4];
                }
                Log.v(tag, lastElement.getFileName() + ",第" + lastElement.getLineNumber() + "行," + message);
            } else {
                Log.v(tag, message);
            }
        }

    }

    public static void log(Object object, String message) {
        log(object.getClass().getSimpleName(), message);
    }

    public static void log(Object object, String... messages) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String message : messages) {
            stringBuilder.append(message);
            stringBuilder.append(",");
        }
        log(object.getClass().getSimpleName(), stringBuilder.toString());
    }


    public static void log(Object object,String message,int... values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message+":");
        for (int value : values) {
            stringBuilder.append(value+"");
            stringBuilder.append(",");
        }
        log(object.getClass().getSimpleName(), stringBuilder.toString());
    }


    /*
      打印调用它的所有方法过程
   */
    public static void logCrime(Object tag, String message) {
        if (isShowlog) {
            log(tag, message);
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for (int i = 3; i < stackTraceElements.length; i++) {
                StackTraceElement ste = stackTraceElements[i];
                Log.v(tag.getClass().getSimpleName(), message+","+ste.getFileName() + "," + ste.getMethodName() + "(),第" + ste.getLineNumber()+"行,线程ID:"+android.os.Process.myTid());
            }
        }
    }
    /*
 将一个object的所有属性和值全部打印
  */
    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();// 根据Class对象获得属性 私有的也可以获得
        String s = "";
        try {
            for (Field f : fields) {
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(obj); // 得到此属性的值
                String name = f.getName(); // 得到此属性的名称
                s += name + ":" + val + ",";
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s;
    }
    public static String toString(Object[] objs,String... targetFields) {
        if (objs == null) {
            return "你方数组为空";
        }
        Class clazz = objs[0].getClass();
        Field[] fields = clazz.getDeclaredFields();// 根据Class对象获得属性 私有的也可以获得
        String s = "";
        try {
            for (int i = 0; i < objs.length; i++) {
                s += "["+i+"]:[";
                for (Field f : fields) {
                    f.setAccessible(true); // 设置些属性是可以访问的
                    Object val = f.get(objs[i]); // 得到此属性的值
                    String name = f.getName(); // 得到此属性的名称
                    for (String targetField : targetFields) {
                        if (targetField.equals(name)) {
                            s += name + ":" + val;
                            if(targetFields.length>1){
                                s += ",";
                            }
                        }
                    }
                }
                s += "]\n";
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String toString(Object obj,String... targetFields) {
        if (obj == null) {
            return "你方对象为空";
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();// 根据Class对象获得属性 私有的也可以获得
        String s = "";
        try {
            for (Field f : fields) {
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(obj); // 得到此属性的值
                String name = f.getName(); // 得到此属性的名称
                for (String targetField : targetFields) {
                    if (targetField.equals(name)) {
                        s += name + ":" + val;
                        if(targetFields.length>1){
                            s += ",";
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String toString(Object[] objs) {
        if (objs == null||objs.length==0) {
            return "你方数组为空";
        }
        Class clazz = objs[0].getClass();
        Field[] fields = clazz.getDeclaredFields();// 根据Class对象获得属性 私有的也可以获得
        String s = "";
        try {
            for (int i = 0; i < objs.length; i++) {
                s += "["+i+"]:[";
                for (Field f : fields) {
                    f.setAccessible(true); // 设置些属性是可以访问的
                    Object val = f.get(objs[i]); // 得到此属性的值
                    String name = f.getName(); // 得到此属性的名称
                    s += name + ":" + val + ",";
                }
                s += "]\n";
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String toString(List objs) {
        if (objs == null) {
            return "你方数组为空";
        }

        String s = "";
        try {
            for (int i = 0; i < objs.size(); i++) {
                Class clazz = objs.get(i).getClass();
                Field[] fields = clazz.getDeclaredFields();// 根据Class对象获得属性 私有的也可以获得
                s += "["+i+"]:[";
                for (Field f : fields) {
                    f.setAccessible(true); // 设置些属性是可以访问的
                    Object val = f.get(objs.get(i)); // 得到此属性的值
                    String name = f.getName(); // 得到此属性的名称
                    s += name + ":" + val + ",";
                }
                s += "]\n";
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return s;
    }

}

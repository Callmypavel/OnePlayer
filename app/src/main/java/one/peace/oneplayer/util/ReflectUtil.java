package one.peace.oneplayer.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {

    public static Method getGetMethod(Class clazz,String attributeName){
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredMethod("get"+StringUtil.upperFirstCharacter(attributeName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeGetMethod(Object obj,Method method) {
        if (obj == null) {
            return null;
        }
        try {
            return method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

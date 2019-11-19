package peacemaker.oneplayer.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by ouyan on 2018/1/23.
 */

public class IntentUtil {

    public static void toActivity(@NonNull Context context, @NonNull Class<?> clz){
        Intent intent = new Intent();
        intent.setClass(context,clz);
        context.startActivity(intent);
    }
    public static void toActivity(@NonNull Context context, @NonNull Class<?> clz, int flags){
        Intent intent = new Intent();
        intent.setClass(context,clz);
        intent.setFlags(flags);
        context.startActivity(intent);
    }
    public static void toActivity(@NonNull Context context, @NonNull Class<?> clz, @NonNull String key, @NonNull String value){
        Intent intent = new Intent();
        intent.setClass(context,clz);
        intent.putExtra(key,value);
        context.startActivity(intent);
    }
    public static void toActivity(@NonNull Context context, @NonNull Class<?> clz, @NonNull String key, @NonNull Boolean value){
        Intent intent = new Intent();
        intent.setClass(context,clz);
        intent.putExtra(key,value);
        context.startActivity(intent);
    }
    public static void toActivity(@NonNull Context context, @NonNull Class<?> clz, @NonNull List<String> keys, @NonNull List<String> values){
        Intent intent = new Intent();
        intent.setClass(context,clz);
        for (int i=0;i<keys.size();i++) {
            intent.putExtra(keys.get(i),values.get(i));
        }
        context.startActivity(intent);
    }
    public static void toActivity(@NonNull Context context, @NonNull Class<?> clz, @NonNull String[] keys, @NonNull String[] values){
        Intent intent = new Intent();
        intent.setClass(context,clz);
        for (int i=0;i<keys.length;i++) {
            LogTool.log("IntentUtil","我是京州市委书记李达康"+keys[i]);
            LogTool.log("IntentUtil","你李达康算什么，我是沙瑞金"+values[i]);
            intent.putExtra(keys[i],values[i]);
        }
        context.startActivity(intent);
    }
    public static void toActivity(@NonNull Context context, @NonNull Class<?> clz, @NonNull Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(context,clz);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public static void toActivity(@NonNull Context context, @NonNull Class<?> clz, @NonNull Bundle bundle, @NonNull int flags){
        Intent intent = new Intent();
        intent.setClass(context,clz);
        intent.putExtras(bundle);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

}

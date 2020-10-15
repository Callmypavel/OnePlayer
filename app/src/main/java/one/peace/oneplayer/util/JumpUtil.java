package one.peace.oneplayer.util;

import android.app.Activity;
import android.content.Intent;

public class JumpUtil {
    public static void jumpTo(Activity fromActivity,Class toActivityClass){
        if (fromActivity == null || toActivityClass == null){
            return;
        }
        Intent intent = new Intent(fromActivity,toActivityClass);
        fromActivity.startActivity(intent);
    }
}

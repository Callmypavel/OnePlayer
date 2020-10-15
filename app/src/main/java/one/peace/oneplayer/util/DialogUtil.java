package one.peace.oneplayer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtil {
    public interface DialogClickListener{
        void onPositiveClick();
        void onNegativeClick();
    }
    public static void showDialog(Context context, String titleText, String contentText, String positiveText, String negativeText, final DialogClickListener dialogClickListener){
        if (context == null || dialogClickListener == null){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleText)
                .setMessage(contentText)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogClickListener.onPositiveClick();
                    }
                })
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogClickListener.onNegativeClick();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

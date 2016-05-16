package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 请叫我保尔 on 2015/9/26.
 */
public class SearchingToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public SearchingToast(Context context) {
        super(context);
    }
    public static Toast makeText(Context context, String foldername,String filename, int duration) {
        Toast result = new Toast(context);

        // 获得LayoutInflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 获得View
        View layout = inflater.inflate(R.layout.search_dialog, null);
        TextView folder = (TextView)layout.findViewById(R.id.folder);
        TextView filetext = (TextView)layout.findViewById(R.id.file);
        folder.setText(foldername);
        filetext.setText(filename);
        result.setView(layout);
        result.setGravity(Gravity.CENTER, 0, 0);
        result.setDuration(duration);
        return result;
    }
}

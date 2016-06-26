package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by ouyan on 2016/6/24.
 */

public class OneImageView extends ImageView {
    private int width;
    public OneImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    public OneImageView(Context context) {
        super(context);
        initialize();
    }

    public OneImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }
    private void initialize(){
        DisplayMetrics dm =getResources().getDisplayMetrics();
        width = dm.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, width);
    }
}

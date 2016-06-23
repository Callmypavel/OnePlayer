package com.example.peacemaker.oneplayer;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by ouyan on 2016/6/6.
 */

public class MusicRect extends RectF {
    public MusicRect() {
        super();
    }

    public MusicRect(float left, float top, float right, float bottom) {
        super(left, top, right, bottom);
    }

    public MusicRect(RectF r) {
        super(r);
    }

    public MusicRect(Rect r) {
        super(r);
    }

    public void setTop(float top){
        this.top = top;
    }
    public void setTop(int top){
        this.top = top;
    }
    public void setBottom(float bottom){
        this.bottom = bottom;
    }
    public void setRight(float right){
        this.right = right;
    }
    public void setLeft(float left){
        this.left = left;
    }
}

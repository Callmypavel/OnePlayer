package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by 请叫我保尔 on 2015/10/2.
 */
public class AlbumImageView extends ImageView {
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private final RectF albumDrawableRect = new RectF();
    private final RectF albumBorderRect = new RectF();
    
    private final Paint albumBitmapPaint = new Paint();
    private final Paint albumBorderPaint = new Paint();

    private int albumBorderColor = DEFAULT_BORDER_COLOR;
    private int albumBorderWidth = DEFAULT_BORDER_WIDTH;
    
    private BitmapShader albumBitmapShader;
    private final Matrix albumShaderMatrix = new Matrix();

    private boolean albumReady;
    private boolean albumSetupPending;

    private int albumBitmapWidth;
    private int albumBitmapHeight;

    private float albumDrawableRadius;
    private float albumBorderRadius;
    
    Bitmap albumBitmap;
    float rotateDegree = 5;
    public AlbumImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = canvas.getMatrix();
        matrix.postRotate(rotateDegree, getWidth() / 2, getHeight() / 2);
        canvas.concat(matrix);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        albumBitmap = bm;
    }
    public void setRotateDegree(float rotateDegree){
        this.rotateDegree = rotateDegree;

    }
    public float getRotateDegree(){
        return rotateDegree;
    }
    @Override
    public void setImageDrawable(Drawable drawable) {
        Log.v("调用","setImageDrawable");
        super.setImageDrawable(drawable);
        albumBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        Log.v("调用","setImageResource");
        super.setImageResource(resId);
        albumBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        Log.v("调用","getBitmapFromDrawable");
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
                        COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        Log.v("调用","setup");
        if (!albumReady) {
            albumSetupPending = true;
            return;
        }

        if (albumBitmap == null) {
            return;
        }

        albumBitmapShader = new BitmapShader(albumBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        albumBitmapPaint.setAntiAlias(true);
        albumBitmapPaint.setShader(albumBitmapShader);

        albumBorderPaint.setStyle(Paint.Style.STROKE);
        albumBorderPaint.setAntiAlias(true);
        albumBorderPaint.setColor(albumBorderColor);
        albumBorderPaint.setStrokeWidth(albumBorderWidth);

        albumBitmapHeight = albumBitmap.getHeight();
        albumBitmapWidth = albumBitmap.getWidth();

        albumBorderRect.set(0, 0, getWidth(), getHeight());
        albumBorderRadius = Math.min((albumBorderRect.height() - albumBorderWidth) / 2,
                (albumBorderRect.width() - albumBorderWidth) / 2);

        albumDrawableRect.set(albumBorderWidth, albumBorderWidth, albumBorderRect.width()
                - albumBorderWidth, albumBorderRect.height() - albumBorderWidth);
        albumDrawableRadius = Math.min(albumDrawableRect.height() / 2,
                albumDrawableRect.width() / 2);

        updateShaderMatrix();
        invalidate();
    }
    private void updateShaderMatrix() {
        Log.v("调用","updateShaderMatrix");
        float scale;
        float dx = 0;
        float dy = 0;

        albumShaderMatrix.set(null);

        if (albumBitmapWidth * albumDrawableRect.height() > albumDrawableRect.width()
                * albumBitmapHeight) {
            scale = albumDrawableRect.height() / (float) albumBitmapHeight;
            dx = (albumDrawableRect.width() - albumBitmapWidth * scale) * 0.5f;
        } else {
            scale = albumDrawableRect.width() / (float) albumBitmapWidth;
            dy = (albumDrawableRect.height() - albumBitmapHeight * scale) * 0.5f;
        }

        albumShaderMatrix.setScale(scale, scale);
        albumShaderMatrix.postTranslate((int) (dx + 0.5f) + albumBorderWidth,
                (int) (dy + 0.5f) + albumBorderWidth);

        albumBitmapShader.setLocalMatrix(albumShaderMatrix);
    }
}

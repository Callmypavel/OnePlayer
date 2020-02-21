package one.peace.oneplayer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import androidx.databinding.BindingAdapter;
import one.peace.oneplayer.R;

/**
 * Created by ouyan on 2016/6/25.
 */

public class OneBitmapUtil {
    private static String TAG = "OneBitmapUtil";

    @BindingAdapter("android:bitmapSrc")
    public static void setBitmapSrc(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter({"android:blurBitmap","android:blurRadius"})
    public static void setBlusRadius(View view, Bitmap bitmap, int radius){
        LogTool.log(TAG, "高斯模糊" + bitmap + "," + radius);
        if (bitmap == null || radius == 0) {
            return;
        }
        Bitmap bitmap1 = OneBitmapUtil.getBluredBitmap(bitmap,radius,view.getContext());
        view.setBackground(new BitmapDrawable(view.getContext().getResources(),bitmap1));

    }

    public static Bitmap defaultBitmap;

    public static Bitmap getDafaultBitmap() {
        if (defaultBitmap == null) {
            defaultBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        }
        return defaultBitmap;
    }

    public interface LoadBitmapListener {
        void onBitmapLoaded(Bitmap bitmap);
    }

    public static Bitmap getBluredBitmap(Bitmap bitmap,int radius,Context context){
        int width = bitmap.getWidth();
        int newWidth = (int)(width*9/16.f);
        int newHeight = width<bitmap.getHeight()?width:bitmap.getHeight();
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap,(width-newWidth)/2,0,newWidth,newHeight);
        //LogTool.log("OneBitmapUtil","压缩前大小"+getSize(bitmap1)+"b");
        bitmap1 = zoomImg(bitmap1,bitmap1.getWidth()/16,bitmap1.getHeight()/16);
        //LogTool.log("OneBitmapUtil","压缩后大小"+getSize(bitmap1)+"b");
        return blurBitmap(bitmap1,radius,context);

    }
    public static Bitmap blurBitmap(Bitmap bitmap,int radius,Context context){
        RenderScript renderScript = RenderScript.create(context);
        Allocation allocation = Allocation.createFromBitmap(renderScript,bitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,allocation.getElement());
        blur.setInput(allocation);
        blur.setRadius(radius);
        blur.forEach(allocation);
        allocation.copyTo(bitmap);

        return drawColorToBitmap(bitmap,Color.argb(150,0,0,0), PorterDuff.Mode.SRC_OVER);
    }

    public static Bitmap drawColorToBitmap(Bitmap bitmap,int color,PorterDuff.Mode mode){
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, mode));
        Bitmap result = Bitmap.createBitmap(bitmap).copy(bitmap.getConfig(),true);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap,0,0,paint);
        return result;
    }

    public static Bitmap getBitmapByResId(Context context,int resId){
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static void loadBitmapAsync(final Context context, final String url, final LoadBitmapListener bitmapLoadListener) {
        if (bitmapLoadListener != null) {
            Bitmap cachedBitmap = ImageCache.getImageCache().getBitmapFromCache(url);
            if (cachedBitmap == null) {
                LogTool.log(TAG, "缓存中不存在id为" + url + "的bitmap");
                ExecutorServiceUtil.submit(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap loadedBitmap = getMiddleAlbumArt(context, url);
                        ImageCache.getImageCache().addToCache(url, loadedBitmap);
                        bitmapLoadListener.onBitmapLoaded(loadedBitmap);

                    }
                });
            } else {
                LogTool.log(TAG, "从缓存中成功获取" + url + "的bitmap");
                bitmapLoadListener.onBitmapLoaded(cachedBitmap);
            }
        }
    }

    public static Drawable getBackArrowDrawable(Context context){
        final Bitmap bitmap = OneBitmapUtil.zoomImg(context, R.drawable.arrow_back_black,12);
        Drawable drawable = new Drawable() {
            Paint paint = new Paint();
            @Override
            public void draw(Canvas canvas) {

                canvas.drawBitmap(bitmap,canvas.getWidth()/2-bitmap.getWidth()/2,canvas.getHeight()/2-bitmap.getHeight()/2,paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        };
        return drawable;
    }

    public static Bitmap getMiddleAlbumArt(Context context, String url) {
        Bitmap bitmap = getAlbumBitmap(context, url);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        if (bitmap.getWidth() > width / 2) {
            bitmap = OneBitmapUtil.zoomImg(bitmap, width / 2, width / 2);
        }
        return bitmap;
    }

    public static Bitmap getAlbumBitmap(Context context, String url) {
        //LogTool.log(TAG, "getAlbumBitmap()拿url" + url);
        Bitmap bitmap = getAlbumArt(url);
        if (bitmap == null) {
            //LogTool.log(TAG, "娶不到专辑图");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
        }
        return bitmap;
    }

    public static Bitmap getAlbumArt(String url) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(url); //设置数据源
            byte[] embedPic = mediaMetadataRetriever.getEmbeddedPicture(); //得到字节型数据
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
            //Log.v("OneMusicLoader","专辑封面原始大小：宽："+bitmap.getWidth()+",高："+bitmap.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mediaMetadataRetriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
    public static Bitmap zoomImg(Context context,int resId, int newWidth,int newHeight){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        bitmap = zoomImg(bitmap,newWidth,newHeight);
        return bitmap;
    }
    public static Bitmap zoomImg(Context context,int resId, int scaleTimes){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        bitmap = zoomImg(bitmap,width/scaleTimes,width/scaleTimes);
        return bitmap;
    }
    public static Bitmap drawTextToBitmap(Bitmap bitmap, String singer, String name, String number,int textColor,int ref){
        float scale = bitmap.getWidth()/ref;
        Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap1);
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        paint.setColor(textColor);
        int textSize = (int) (18 * scale);
        paint.setTextSize(textSize);
        paint.setDither(true); //获取更清晰的图像采样
        paint.setFilterBitmap(true);//过滤一些
        Rect bounds = new Rect();
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        paint.getTextBounds(singer, 0, singer.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/10*9 ;
        int y = (bitmap.getHeight() + bounds.height())/10*9;
        canvas.drawText(singer, x , y, paint);
//        paint.getTextBounds(name, 0, name.length(), bounds);
//        canvas.drawText(name,width-name.length()*textColor,height-3*textSize, paint);
//        paint.getTextBounds(singer, 0, singer.length(), bounds);
//        paint.setTextSize((int) (18 * scale));
//        canvas.drawText(singer,width-singer.length()*textColor,height-2*textSize, paint);
//        paint.getTextBounds(number, 0, number.length(), bounds);
//        paint.setTextSize((int) (18 * scale));
//        canvas.drawText(number,width-number.length()*textColor,height-textSize, paint);
        return bitmap1;
    }
    public static Bitmap compressBitmap(Bitmap bitmap){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = (int)(100.f/compressPercent);
//        Bitmap compressedBitmap = BitmapFactory.decodeStream(is,null,options);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int quality = 100;
        // Store the bitmap into output stream(no compress)
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        Log.v("OneBitmapUtil","查看长度"+byteArrayOutputStream.toByteArray().length);
        // Compress by loop
        while ( byteArrayOutputStream.toByteArray().length / 1024 >= 1) {
            Log.v("OneBitmapUtil","查看长度"+byteArrayOutputStream.toByteArray().length);
            // Clean up os
            byteArrayOutputStream.reset();
            // interval 10
            quality -= 10;
            Log.v("OneBitmapUtil","查看质量"+quality);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        }
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Bitmap compressedBitmap = BitmapFactory.decodeStream(inputStream,null,options);

        return compressedBitmap;
    }
    public static Bitmap compressBitmap(Context context,int resId){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        return compressBitmap(bitmap);
    }
    public static Bitmap compressBitmap(Context context,int resId,int targetWidth,int targetHeight){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        bitmap = zoomImg(bitmap,targetWidth,targetHeight);
        return compressBitmap(bitmap);
    }

}

package one.peace.oneplayer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by ouyan on 2016/6/25.
 */

public class OneBitmapUtil {
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
            //e.printStackTrace();
        } finally {
            try {
                mediaMetadataRetriever.release();
            } catch (Exception e2) {
                //e2.printStackTrace();
            }
        }
        return bitmap;
    }
}

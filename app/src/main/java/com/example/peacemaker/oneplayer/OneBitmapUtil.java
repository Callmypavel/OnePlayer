package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

package peacemaker.oneplayer.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.PrivilegedAction;

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
    public static Bitmap drawColorToBitmap(Bitmap bitmap,int color,PorterDuff.Mode mode){
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, mode));
        Bitmap result = Bitmap.createBitmap(bitmap).copy(bitmap.getConfig(),true);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap,0,0,paint);
        return result;
    }
    public static Bitmap drawColorToBitmap(Context context,int resId,int color){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        return drawColorToBitmap(bitmap,color,PorterDuff.Mode.SRC_IN);
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
    public static int getSize(Bitmap bitmap){
        if(bitmap!=null){
            return bitmap.getRowBytes();
        }else {
            return -1;
        }

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
    private static Bitmap compressBitmap(Bitmap bitmap){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = (int)(100.f/compressPercent);
//        Bitmap compressedBitmap = BitmapFactory.decodeStream(is,null,options);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int quality = 0;
        // Store the bitmap into output stream(no compress)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
//        Log.v("OneBitmapUtil","查看长度"+byteArrayOutputStream.toByteArray().length+"b");
//        // Compress by loop
//        while ( byteArrayOutputStream.toByteArray().length >= 1024||quality>=0) {
//            Log.v("OneBitmapUtil","查看长度"+byteArrayOutputStream.toByteArray().length+"b");
//            // Clean up os
//            byteArrayOutputStream.reset();
//            // interval 10
//            quality -= 10;
//            if(quality>=0) {
//                boolean success = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
//                Log.v("OneBitmapUtil","查看质量"+quality+","+success);
//            }
//        }
        boolean success = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        Log.v("OneBitmapUtil","查看质量"+quality+","+success);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return BitmapFactory.decodeStream(inputStream,null,options);
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

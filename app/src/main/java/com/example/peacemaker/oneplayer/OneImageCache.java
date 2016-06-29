package com.example.peacemaker.oneplayer;

import android.graphics.Bitmap;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by ouyan on 2016/6/28.
 */

public class OneImageCache {
    private HashMap<String,SoftReference<Bitmap>> imageCache = new HashMap<>();
    private int cacheSize = 0;
    //单位为byte
    private int maxCacheSize;
    public OneImageCache(){
        maxCacheSize = (int)Runtime.getRuntime().maxMemory()/8;
        Log.v("OneImageCache","OneImageCache()，缓存大小设置为"+maxCacheSize/1024+"kb,也就是"+maxCacheSize+"b");
    }
    public void addToCache(String id,Bitmap bitmap){
        cacheSize+=bitmap.getRowBytes();
        if(cacheSize>maxCacheSize){
            Log.v("OneImageCache","addToCache()，缓存池已满");
            isFullQueue();
        }
        imageCache.put(id,new SoftReference<>(bitmap));
    }
    public Bitmap getBitmapFromCache(String id){
        SoftReference<Bitmap> softReference = imageCache.get(id);
        Bitmap bitmap = null;
        if(softReference!=null) {
            bitmap = softReference.get();

        }
        return bitmap;
    }
    public void releaseAll(){
        if(imageCache!=null) {
            imageCache.clear();
        }
    }
    public void release(String id){
        if(imageCache!=null) {
            imageCache.remove(id);
        }
    }

    private void isFullQueue(){
        Bitmap bitmap = null;
        Object key = null;
        Iterator iter = imageCache.keySet().iterator();
        if (iter.hasNext()) {
            key = iter.next();
            SoftReference<Bitmap> softReference = imageCache.get(key);
            bitmap = softReference.get();
        }
        if(bitmap!=null) {
            cacheSize -= bitmap.getRowBytes();
            imageCache.remove(key);
        }
        if(cacheSize>maxCacheSize){
            isFullQueue();
        }
    }
}

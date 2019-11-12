package peacemaker.oneplayer.tool;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ouyan on 2016/6/28.
 */

public class OneImageCache {
    private static Queue<CacheItem> imageCache = new LinkedBlockingQueue<>();
    private static LruCache<String,Bitmap> cachePool;
    private int cacheSize = 0;
    //单位为byte
    private int maxCacheSize;
    //缓存系数
    private int scale = 8;
    private static OneImageCache instance;
    class CacheItem{
        private String id;
        private Bitmap bitmap;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public CacheItem(String id, Bitmap bitmap) {
            this.id = id;
            this.bitmap = bitmap;
        }
    }
    private OneImageCache(){
        maxCacheSize = (int) Runtime.getRuntime().maxMemory()/scale;
        cachePool = new LruCache<String,Bitmap>(maxCacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }


        };
        Log.v("OneImageCache","OneImageCache()，缓存大小设置为"+maxCacheSize/1024+"kb,"+maxCacheSize+"b");
    }
    public static OneImageCache getImageCache(){
        if(instance == null){
            instance = new OneImageCache();
        }
        return instance;
    }
    public void addToCache(String id, Bitmap bitmap){
//        cacheSize+=bitmap.getRowBytes() * bitmap.getHeight();
//        Log.v("OneImageCache","缓存池当前容量"+cacheSize);
//        if(cacheSize>maxCacheSize){
//            Log.v("OneImageCache","addToCache()，缓存池已满");
//            isFullQueue();
//        }
//        imageCache.add(new CacheItem(id,bitmap));
        cachePool.put(id,bitmap);
    }
    public Bitmap getBitmapFromCache(String id){
//        for (CacheItem cacheItem : imageCache) {
//            if (cacheItem.getId().equals(id)){
//                return cacheItem.getBitmap();
//            }
//        }
//        SoftReference<Bitmap> softReference = imageCache.get(id);
//        Bitmap bitmap = null;
//        if(softReference!=null) {
//            bitmap = softReference.get();
//
//        }

        return cachePool.get(id);
//        Bitmap cacheBitmap = cachePool.get(id);
//        if (cacheBitmap==null){
//            Bitmap bitmap = FileOperator.getBitmapFromFile(StringUtil.getCachePath()+id);
//            if (bitmap!=null) {
//                addToCache(id, bitmap);
//            }
//            return bitmap;
//        }else {
//            return cacheBitmap;
//        }

    }
    public void releaseAll(){
        if(imageCache!=null) {
            imageCache.clear();
        }
    }
    public void release(String id){
        if(imageCache!=null) {
            for (CacheItem cacheItem : imageCache) {
                if (cacheItem.getId().equals(id)){
                    imageCache.remove(cacheItem);
                }
            }

        }
    }

    private void isFullQueue(){
//        Bitmap bitmap = null;
//        Object key = null;
//        Iterator iter = imageCache.keySet().iterator();
//        if (iter.hasNext()) {
//            key = iter.next();
//            SoftReference<Bitmap> softReference = imageCache.get(key);
//            bitmap = softReference.get();
//        }
        Bitmap bitmap = imageCache.peek().getBitmap();
        if(bitmap!=null) {
            bitmap.recycle();
            cacheSize -= bitmap.getRowBytes()*bitmap.getHeight();
            Log.v("OneImageCache","isFullQueue()，释放后的池"+cacheSize);
            imageCache.poll();
        }
        if(cacheSize>maxCacheSize){
            isFullQueue();
        }
    }
}

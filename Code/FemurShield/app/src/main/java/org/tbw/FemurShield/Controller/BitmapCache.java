package org.tbw.FemurShield.Controller;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Marco on 30/06/2015.
 */
public class BitmapCache extends LruCache<String,Bitmap>
{
    private static BitmapCache instance;
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    private BitmapCache(int maxSize) {
        super(maxSize);
    }

    public static BitmapCache getInstance()
    {
        if(instance!=null)
            return instance;

        //Kb di memoria disponibili
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //ne uso 1/8
        final int cacheSize = maxMemory / 8;

        return instance=new BitmapCache(cacheSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        // invece che numero di oggetti ritorno il numero di kilobyte
        return bitmap.getByteCount() / 1024;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        String temp=key.replaceAll("/", "_");
        return get(key);
    }

}

package org.tbw.FemurShield.Controller;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Classe che gestisce una cache di immagini
 *
 * @author Marco Biasin
 */
public class BitmapCache extends LruCache<String, Bitmap> {
    private static BitmapCache instance;

    /**
     * @param maxSize la massima dimensione utilizzabile
     */
    private BitmapCache(int maxSize) {
        super(maxSize);
    }

    public static BitmapCache getInstance() {
        if (instance != null)
            return instance;

        //Kb di memoria disponibili
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //ne uso 1/8
        final int cacheSize = maxMemory / 8;

        return instance = new BitmapCache(cacheSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        // invece che numero di oggetti ritorno il numero di kilobyte
        return bitmap.getByteCount() / 1024;
    }

    /**
     * Aggiungo una immagine alla cache se non presente
     *
     * @param key    il nome dell'immagine
     * @param bitmap l'immagine
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            put(key, bitmap);
        }
    }

    /**
     * Recupera l'immagine se presente nella cache
     *
     * @param key il nome dell'immagine
     * @return l'immagine o null se non presente
     */
    public Bitmap getBitmapFromMemCache(String key) {
        String temp = key.replaceAll("/", "_");
        return get(temp);
    }

}

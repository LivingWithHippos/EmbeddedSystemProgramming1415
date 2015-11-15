package org.tbw.FemurShield.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Classe per il caricamento dal disco della signature di una sessione tramite asynctask per non bloccare il disco
 *
 * @author Marco Biasin
 */
public class SignatureLoaderTask extends AsyncTask<Integer, Void, Bitmap> {

    public final String TAG = this.getClass().getSimpleName();
    private String sessionTimeStamp;
    private final WeakReference<ImageView> ivFallReference;
    private Bitmap signature;

    /**
     * Costruttore della classe
     *
     * @param ivFall           l'imageview da riempire con la signature
     * @param sessionTimeStamp il timestamp della sessione da rappresentare
     */
    public SignatureLoaderTask(ImageView ivFall, String sessionTimeStamp) {
        ivFallReference = new WeakReference<ImageView>(ivFall);
        this.sessionTimeStamp = sessionTimeStamp.replaceAll("/", "_");
    }

    /**
     * Ritorna lo spazio disponibile in megabyte sul dispositivo
     *
     * @return
     */
    public static float spaceAvailable() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSizeLong() * (long) stat.getAvailableBlocksLong();
        //ritorna in megabyte
        return bytesAvailable / (1024.f * 1024.f);
    }

    // carica l'immagine in background
    @Override
    protected Bitmap doInBackground(Integer... params) {

        File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
        if (isExternalStorageReadable()) {

            File picture = new File(appPath, "signature_" + sessionTimeStamp + ".png");
            if (picture.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(picture);
                    signature = BitmapFactory.decodeStream(fis);
                    fis.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Bitmap not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "Error accessing Bitmap: " + e.getMessage());
                }
            } else {
                //Log.e(TAG, "Bitmap non presente sul disco");
            }
        } else {
            Log.e(TAG, "Memoria non accessibile");
        }

        if (signature != null)
            BitmapCache.getInstance().addBitmapToMemoryCache(sessionTimeStamp, signature);
        return signature;
    }

    //mostra l'immagine sulla imageview se è ancora presente sullo schermo
    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (ivFallReference != null && bitmap != null) {
            final ImageView fallImage = ivFallReference.get();
            if (fallImage != null) {
                fallImage.setImageBitmap(bitmap);
            } else {
                //Log.d(TAG, "Problema caricamento riferimento imageView");
            }
        }
    }

    /**
     * Controlla se è presente sul disco il file
     *
     * @return true se presente, false altrimenti
     */
    private boolean signatureExists() {
        boolean exists = false;
        File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
        File picture = new File(appPath, "signature_" + sessionTimeStamp + ".png");
        return picture.exists();
    }

    /**
     * Controlla se sia possibile scrivere sulla memoria
     *
     * @return true se scrivibile, false altrimenti
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Controlla se la memoria è leggibile
     *
     * @return true se è leggibile, false altrimenti
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


}

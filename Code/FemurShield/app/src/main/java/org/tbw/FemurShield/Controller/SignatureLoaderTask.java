package org.tbw.FemurShield.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;


import org.tbw.FemurShield.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Marco on 29/06/2015.
 */
public class SignatureLoaderTask  extends AsyncTask<Integer, Void, Bitmap> {

        public final String TAG = this.getClass().getSimpleName();
        private String sessionTimeStamp;
        private final WeakReference<ImageView> ivFallReference;
        private Bitmap signature;



        public SignatureLoaderTask(ImageView ivFall, String sessionTimeStamp) {
            ivFallReference = new WeakReference<ImageView>(ivFall);
            this.sessionTimeStamp=sessionTimeStamp.replaceAll("/", "_");
        }

        // Load image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {

            File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
            if (isExternalStorageReadable()) {

                File picture = new File(appPath, "signature_" + sessionTimeStamp + ".png");
                if (picture.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(picture);
                        signature=BitmapFactory.decodeStream(fis);
                        fis.close();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Bitmap not found: " + e.getMessage());
                    } catch (IOException e) {
                        Log.e(TAG, "Error accessing Bitmap: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Bitmap non presente sul disco");
                }
            } else {
                Log.e(TAG, "Memoria non accessibile");
            }

            if(signature!=null)
                BitmapCache.getInstance().addBitmapToMemoryCache(sessionTimeStamp,signature);
            return signature;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (ivFallReference != null && bitmap != null) {
                final ImageView fallImage = ivFallReference.get();
                if (fallImage != null) {
                    fallImage.setImageBitmap(bitmap);
                }
                else{Log.d(TAG,"Problema caricamento riferimento imageView");}
            }
        }

    private boolean signatureExists() {
        boolean exists = false;
        File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
        File picture = new File(appPath, "signature_" + sessionTimeStamp + ".png");
        return picture.exists();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }




}

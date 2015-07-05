package org.tbw.FemurShield.Model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import org.tbw.FemurShield.Controller.BitmapCache;
import org.tbw.FemurShield.Controller.ColorsPicker;
import org.tbw.FemurShield.Controller.Controller;
import org.tbw.FemurShield.Controller.SignatureLoaderTask;
import org.tbw.FemurShield.Observer.Observable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Classe che crea la signature di una sessione a partire dai primi secondi
 *
 * @author Marco Biasin
 */
public class SignatureImpl implements Signature, org.tbw.FemurShield.Observer.Observer {

    private static SignatureImpl instance;
    private static String lastSavedSession = "";
    public static final String TAG = "SignatureImpl";
    private String sessionID;
    //variabili bitmap
    protected Bitmap signature;
    private Canvas canvas;
    private int resolution = 500;
    //variabili CIRCLE_MODE
    private MPoint[] startPoint, finishPoint, firstPoint;
    private float beta;
    private final float coeff = 4;
    private int radius;
    private MCircle[] circles;
    private Paint[] circlePaint;
    private String[] palette;
    private int sign = 1;
    private boolean invertSign = true;

    /**
     * Ritorna l'instanza singleton, applicandovi l'ID della sessione da trasformare in immagine
     *
     * @param sessionID l'ID della sessione di cui fare la signature
     * @return l'istanza singleton della classe
     */
    public static SignatureImpl getInstance(String sessionID) {
        if (instance != null) {
            instance.setSession(sessionID);
            return instance;
        }

        instance = new SignatureImpl(sessionID);
        return instance;
    }

    private SignatureImpl(String sessionID) {
        this.sessionID = sessionID.replaceAll("/", "_");
        startDrawing();
    }

    /**
     * Imposta i colori per la signature
     */
    private void setCirclesPaints() {

        palette = ColorsPicker.pickRandomColors();
        circlePaint = new Paint[3];
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor(palette[0]));
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[0] = paint;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor(palette[1]));
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[1] = paint;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor(palette[2]));
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[2] = paint;
    }

    /**
     * Disegna un nuovo punto dei cerchi che rappresentano i dati dell'accelerometro
     * @param arg i dati dell'accelerometro
     */
    private void drawCircle(float[] arg) {
        beta += (2 * Math.PI) / 300;
        if (beta < (2 * Math.PI)) {
            for (int i = 0; i < circles.length; i++) {
                float newX, newY;
                if (i < 2) {
                    float newradius = Math.abs(radius + sign * Math.abs(arg[i]) * coeff);
                    if (newradius < radius / 2)
                        newradius = radius / 2;
                    newX = (float) (circles[i].xCenter + newradius * Math.cos(beta));
                    newY = (float) (circles[i].yCenter + newradius * Math.sin(beta));
                } else {
                    //qui c'e' la gravita' da tenere in conto
                    float newradius = (float) Math.abs(radius + sign * (Math.abs(arg[i]) - 9.81) * coeff);
                    if (newradius < radius / 2)
                        newradius = radius / 2;
                    newX = (float) (circles[i].xCenter + newradius * Math.cos(beta));
                    newY = (float) (circles[i].yCenter + newradius * Math.sin(beta));
                    if (invertSign)
                        sign *= -1;
                }
                finishPoint[i] = new MPoint(checkValue(newX), checkValue(newY));
                if (startPoint[i] != null) {
                    canvas.drawLine(startPoint[i].x, startPoint[i].y, finishPoint[i].x, finishPoint[i].y, circlePaint[i]);
                    startPoint[i].set(finishPoint[i].x, finishPoint[i].y);
                } else {
                    startPoint[i] = new MPoint(finishPoint[i].x, finishPoint[i].y);
                    firstPoint[i] = new MPoint(finishPoint[i].x, finishPoint[i].y);
                }

            }
        } else {

            //unisco l'ultimo e il primo punto
            for (int i = 0; i < 3; i++)
                canvas.drawLine(finishPoint[i].x, finishPoint[i].y, firstPoint[i].x, firstPoint[i].y, circlePaint[i]);
            lastSavedSession = sessionID;
            Controller.getNotification().deattach(this);
            saveSignature();
        }

    }

    /**
     * Controlla che i valori non siano al di fuori dei bordi dell'immagine
     * @param i il float da controllare
     * @return il float originale o modificato se aveva valori troppo grandi o piccoli
     */
    private float checkValue(float i) {
        float temp = i;
        if (i < 0)
            temp = 0;
        if (i > resolution)
            temp = resolution;
        return temp;
    }


    /**
     *
     * @return la Bitmap creata
     */
    @Override
    public Bitmap toBitmap() {
        return signature;
    }

    /**
     * Metodo che indica al disegnatore che deve smettere e salvare la sessione
     */
    public void stopDrawing() {
        if (!lastSavedSession.equalsIgnoreCase(sessionID)) {
            Controller.getNotification().deattach(this);
            saveSignature();
        }
    }

    /**
     * Metodo che inizializza le variabili e si collega come observer per ricevere i dati dell'accelerometro
     */
    public void startDrawing() {
        signature = Bitmap.createBitmap(resolution, resolution, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(signature);
        radius = resolution / 5;
        beta = (float) 0.0;
        setCirclesPaints();
        finishPoint = new MPoint[3];

        MCircle circleX = new MCircle((resolution / 6) * 3, (resolution / 6) * 2, radius);
        MCircle circleY = new MCircle((resolution / 6) * 2, (resolution / 6) * 4, radius);
        MCircle circleZ = new MCircle((resolution / 6) * 4, (resolution / 6) * 4, radius);
        circles = new MCircle[]{circleX, circleY, circleZ};
        startPoint = new MPoint[3];
        firstPoint = new MPoint[3];
        Controller.getNotification().addObserver(this);
    }

    /**
     * Metodo che salva su disco la signature
     * @return true se è stata salvata, false altrimenti
     */
    private boolean saveSignature() {

        boolean result = false;
        if (SignatureLoaderTask.isExternalStorageWritable()) {
            File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
            appPath.mkdirs();
            //nasconde i file immagine dalla galleria
            File nomedia = new File(appPath, ".nomedia");
            if (!nomedia.exists()) {
                try {
                    FileOutputStream fos = new FileOutputStream(nomedia);
                    fos.close();
                } catch (FileNotFoundException e) {
                    //Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    //Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
            //salvo la signature
            File picture = new File(appPath, "signature_" + sessionID + ".png");
            if (!picture.exists())
                try {
                    FileOutputStream fos = new FileOutputStream(picture);
                    toBitmap().compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                    BitmapCache.getInstance().addBitmapToMemoryCache(sessionID, toBitmap());
                    //Log.d(TAG, "immagine scritta");
                    result = true;
                } catch (FileNotFoundException e) {
                    //Log.d(TAG, "File non trovato: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "Errore accesso file: " + e.getMessage());
                }
            else {
                result = true;
            }
        }

        return result;
    }

    /**
     * Carica la signature su Bitmap
     * @return true se è stata trovata e caricata correttamente
     */
    private boolean loadSignature() {

        boolean result = false;
        File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
        if (SignatureLoaderTask.isExternalStorageReadable()) {

            File picture = new File(appPath, "signature_" + sessionID + ".png");
            if (picture.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(picture);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    this.signature = (Bitmap) ois.readObject();
                    ois.close();
                    result = true;
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Bitmap not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "Error accessing Bitmap: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, "Error reading Bitmap: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Bitmap non presente sul disco");
            }
        } else {
            Log.e(TAG, "Memoria non accessibile");
        }
        return result;
    }

    /**
     * Controlla se esiste il file signature
     * @return true se esiste, false altrimenti
     */
    private boolean signatureExists() {
        boolean exists = false;
        File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
        File picture = new File(appPath, "signature_" + sessionID + ".png");
        return picture.exists();
    }

    /**
     * Metodo statico per cancellare un file signature
     * @param sessionDeleteDate il timestamp della signature da cancellare
     * @return true se è stata cancellata, flase altrimenti
     */
    public static boolean deleteSignature(String sessionDeleteDate) {
        boolean result = false;
        if (SignatureLoaderTask.isExternalStorageWritable()) {
            String filename = sessionDeleteDate.replaceAll("/", "_");
            File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
            File picture = new File(appPath, "signature_" + filename + ".png");
            result = picture.delete();
        } else {
            Log.e(TAG, "Memoria non accessibile");
        }
        return result;
    }

    //ottiene i dati dall'accelererometro
    @Override
    public void update(Observable oggettoosservato, Object o) {
        if (o instanceof float[]) {
            float[] arg = (float[]) o;
            drawCircle(arg);
        }
    }


    /**
     * Imposta la sessione da disegnare
     * @param session l'ID della sessione
     */
    public void setSession(String session) {
        this.sessionID = session.replaceAll("/", "_");
    }
}

/**
 * Classe interna per la gestione di elementi geometrici
 */
class MPoint {
    public float x, y;

    public MPoint() {
    }

    public MPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static float calcolaDistanza(MPoint a, MPoint b) {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

}

/**
 * Classe interna per la gestione di elementi geometrici
 */
class MCircle {
    int xCenter, yCenter;
    int radius;

    public MCircle(int xCenter, int yCenter, int radius) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.radius = radius;
    }

    public MPoint getCenter() {
        return new MPoint(xCenter, yCenter);
    }
}


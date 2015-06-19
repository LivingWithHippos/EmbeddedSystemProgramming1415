package org.tbw.FemurShield.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;


import org.tbw.FemurShield.Controller.ColorsPicker;
import org.tbw.FemurShield.Controller.Controller;
import org.tbw.FemurShield.Observer.Observable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Moro on 30/04/15.
 */
public class SignatureImpl implements Signature,org.tbw.FemurShield.Observer.Observer{

    public static final int CIRCLE=0;
    public static final int CLOVER=1;
    public static final int CIRCLE_STATIC=2 ;
    private int MODE;
    private String sessionID;
    //variabili bitmap
    protected Bitmap signature;
    private Canvas canvas;
    private int resolution=500;
    //variabili CIRCLE_MODE
    private MPoint[] startPoint,finishPoint,firstPoint;
    private float beta;
    private final float coeff=4;
    private int radius;
    private MCircle[] circles;
    private Paint[] circlePaint;
    private String[] palette;
    private int sign=1;
    private boolean invertSign=true;


    public SignatureImpl(int mode,String sessionID){

        this.MODE=mode;
        this.sessionID=sessionID.replaceAll("/","_");

        signature= Bitmap.createBitmap(resolution, resolution, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(signature);

        switch(MODE)
        {
            case CIRCLE_STATIC:
            {
                radius=resolution/5;
                beta=(float)0.0;
                setCirclesPaints();
                finishPoint=new MPoint[3];

                MCircle circleX=new MCircle((resolution/6)*3,(resolution/6)*2,radius);
                MCircle circleY=new MCircle((resolution/6)*2,(resolution/6)*4,radius);
                MCircle circleZ=new MCircle((resolution/6)*4,(resolution/6)*4,radius);
                circles=new MCircle[]{circleX,circleY,circleZ};
                startPoint=new MPoint[3];
                firstPoint=new MPoint[3];
                Controller.getNotification().addObserver(this);
            }
        }
    }

    private void setCirclesPaints() {

        palette= ColorsPicker.pickRandomColors();
        circlePaint=new Paint[3];
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor(palette[0]));
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[0]=paint;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor(palette[1]));
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[1]=paint;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor(palette[2]));
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[2]=paint;

    }


    private void drawCircle(float[] arg) {
        beta+= (2*Math.PI)/300;
        if(beta<(2*Math.PI))
        {
            for(int i=0;i<circles.length;i++)
            {
                float newX,newY;
                if(i<2) {
                    float newradius=Math.abs(radius + sign*Math.abs(arg[i]) * coeff);
                    if(newradius<radius/2)
                        newradius=radius/2;
                    newX =(float) (circles[i].xCenter + newradius * Math.cos(beta));
                    newY = (float)(circles[i].yCenter + newradius * Math.sin(beta));
                }else
                {
                    //qui c'e' la gravita' da tenere in conto
                    float newradius=(float)Math.abs(radius + sign*(Math.abs(arg[i])-9.81) * coeff) ;
                    if(newradius<radius/2)
                        newradius=radius/2;
                    newX = (float) (circles[i].xCenter +newradius* Math.cos(beta));
                    newY = (float) (circles[i].yCenter + newradius* Math.sin(beta));
                    if(invertSign)
                        sign*=-1;
                }
                finishPoint[i]=new MPoint(checkValue(newX),checkValue(newY));
                if(startPoint[i]!=null)
                {
                    canvas.drawLine(startPoint[i].x, startPoint[i].y, finishPoint[i].x, finishPoint[i].y,circlePaint[i]);
                    startPoint[i].set(finishPoint[i].x, finishPoint[i].y);
                }
                else
                {
                    startPoint[i]=new MPoint(finishPoint[i].x,finishPoint[i].y);
                    firstPoint[i]=new MPoint(finishPoint[i].x,finishPoint[i].y);
                }



            }
        }
        else{
            Controller.getNotification().deattach(this);
            //unisco l'ulitmo e il primo punto
            for(int i=0;i<3;i++)
                canvas.drawLine(finishPoint[i].x, finishPoint[i].y,firstPoint[i].x,firstPoint[i].y,circlePaint[i]);
            //TODO: impolementa metodo
            saveSignature();
        }

    }

    private float checkValue(float i)
    {
        float temp=i;
        if(i<0)
            temp=0;
        if (i>resolution)
            temp=resolution;
        return temp;
    }



    public void setMode(int mode)
    {
        this.MODE=mode;
    }

    @Override
    public Bitmap toBitmap() {
        return signature;
    }

    private boolean saveSignature() {

        boolean result=false;
        if (isExternalStorageWritable()) {
            File appPath = new File(Environment.getExternalStorageDirectory(), "FemurShield");
            appPath.mkdirs();
            //nasconde i file immagine dalla galleria
            File nomedia = new File(appPath, ".nomedia");
            if (!nomedia.exists()) {
                try {
                    FileOutputStream fos = new FileOutputStream(nomedia);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d("SignatureImpl", "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d("SignatureImpl", "Error accessing file: " + e.getMessage());
                }
            }
            //salvo la signature
            File picture = new File(appPath, "signature_" + sessionID + ".png");
            try {
                FileOutputStream fos = new FileOutputStream(picture);
                toBitmap().compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                result = true;
            } catch (FileNotFoundException e) {
                Log.d("SignatureImpl", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("SignatureImpl", "Error accessing file: " + e.getMessage());
            }
        }
        return result;
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

    @Override
    public void update(Observable oggettoosservato, Object o) {
        if(o instanceof float[]){
            float[] arg=(float[])o;
            switch(MODE)
            {
                case CIRCLE_STATIC: {drawCircle(arg);}
            }
        }
    }


}

class MPoint
{
    public float x,y;
    public MPoint()
    {}
    public MPoint(float x, float y)
    {
        this.x=x;
        this.y=y;
    }

    public void set(float x,float y)
    {
        this.x=x;
        this.y=y;
    }

    public static float calcolaDistanza(MPoint a,MPoint b)
    {
        return (float)Math.sqrt(Math.pow(a.x - b.x,2)+Math.pow(a.y-b.y,2));
    }

}


class MCircle
{
    int xCenter,yCenter;
    int radius;

    public MCircle(int xCenter,int yCenter,int radius)
    {
        this.xCenter=xCenter;
        this.yCenter=yCenter;
        this.radius=radius;
    }

    public MPoint getCenter() {
        return new MPoint(xCenter,yCenter);
    }
}


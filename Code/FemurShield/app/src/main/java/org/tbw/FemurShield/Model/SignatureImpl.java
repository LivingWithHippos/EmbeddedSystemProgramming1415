package org.tbw.FemurShield.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


import org.tbw.FemurShield.Controller.Controller;
import org.tbw.FemurShield.Observer.Observable;

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
    private Context context;
    private String datetime;
    //variabili data
    protected Calendar calendar;
    private int hour;
    private int minute;
    private int second;
    private int day;
    private int month;
    private int year;
    private int[] dates;
    private int sum;
    //variabili bitmap
    protected Bitmap signature;
    private Canvas canvas;
    private Random random;
    private Paint paint;
    private int resolution=500;
    private int xc,yc;
    //variabili CIRCLE_MODE
    private int space;
    private MPoint[] startPoint,finishPoint;
    private float beta;
    private final float coeff=5;
    private int radius;
    private MCircle[] circles;
    private Paint[] circlePaint;



    public SignatureImpl(String date,int mode){

        this.MODE=mode;
        datetime=date;

        try {
            //dati
            calendar=Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat(Session.datePattern).parse(date));
            dates=new int[6];
            elaborateDate();

        } catch (ParseException e) {e.printStackTrace();}


        signature= Bitmap.createBitmap(resolution, resolution, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(signature);

        switch(MODE)
        {
            case CIRCLE_STATIC:
            {
                radius=resolution/4;
                beta=(float)0.0;
                setCirclesPaints();
                finishPoint=new MPoint[3];

                MCircle circleX=new MCircle((resolution/6)*3,(resolution/6)*2,radius);
                MCircle circleY=new MCircle((resolution/6)*2,(resolution/6)*4,radius);
                MCircle circleZ=new MCircle((resolution/6)*4,(resolution/6)*4,radius);
                circles=new MCircle[]{circleX,circleY,circleZ};
                MPoint pointX=new MPoint(circleX.xCenter+radius,circleX.yCenter);
                MPoint pointY=new MPoint(circleY.xCenter+radius,circleY.yCenter);
                MPoint pointZ=new MPoint(circleZ.xCenter+radius,circleZ.yCenter);
                startPoint=new MPoint[]{pointX,pointY,pointZ};

                Controller.getNotification().addObserver(this);
            }
        }
    }

    private void setCirclesPaints() {

        circlePaint=new Paint[3];
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(6);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[0]=paint;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(6);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[1]=paint;
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(6);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        circlePaint[2]=paint;

    }

    private void elaborateDate()
    {
        dates[0]=hour=calendar.get(Calendar.SECOND);
        dates[1]=minute=calendar.get(Calendar.MINUTE);
        dates[2]=second=calendar.get(Calendar.HOUR);
        dates[3]=year=calendar.get(Calendar.YEAR);
        dates[4]=month=calendar.get(Calendar.MONTH);
        dates[5]=day=calendar.get(Calendar.DAY_OF_YEAR);
        for(int i:dates)
            sum += i;
    }


    private void drawCircle(float[] arg) {
        beta+= (2*Math.PI)/360;
        if(beta<(2*Math.PI))
        {
            for(int i=0;i<circles.length;i++)
            {
                float newX,newY;
                if(i<2) {
                    float newradius=(radius + arg[i] * coeff);
                    newX =(float) (circles[i].xCenter + newradius * Math.cos(beta));
                    newY = (float)(circles[i].yCenter + newradius * Math.sin(beta));
                }else
                {
                    float newradius=(float)(radius + (Math.abs(arg[i])-9.81) * coeff) ;
                    newX = (float) (circles[i].xCenter +newradius* Math.cos(beta));
                    newY = (float) (circles[i].yCenter + newradius* Math.sin(beta));
                }
                finishPoint[i]=new MPoint(newX,newY);
                canvas.drawLine(startPoint[i].x, startPoint[i].y, finishPoint[i].x, finishPoint[i].y,circlePaint[i]);
                startPoint[i].set(finishPoint[i].x, finishPoint[i].y);
            }

        }
        else{
            Controller.getNotification().deattach(this);

            saveSignature();
        }

    }


    public void setMode(int mode)
    {
        this.MODE=mode;
    }

    @Override
    public Bitmap toBitmap() {
        return signature;
    }

    private void saveSignature() {

        /*File pictureFile=new File(context.getFilesDir(),"signature_"+datetime+".png");
        if (pictureFile == null) {
            Log.d("SignatureImpl","Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            toBitmap().compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("SignatureImpl", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("SignatureImpl", "Error accessing file: " + e.getMessage());
        }*/
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


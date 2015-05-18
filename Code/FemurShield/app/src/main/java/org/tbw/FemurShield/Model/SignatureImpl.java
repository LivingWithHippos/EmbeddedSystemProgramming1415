package org.tbw.FemurShield.Model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Moro on 30/04/15.
 */
public class SignatureImpl implements Signature{
    //TODO:Campi privati
    public static int signatureCounter=0;
    public static final int PEAKED_CIRCLE=0;
    protected Calendar calendar;
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
    protected Bitmap signature;
    private int hour;
    private int minute;
    private int second;
    private int day;
    private int month;
    private int year;
    private int[] dates;
    private int sum;
    private int space;
    private float[] peaks;
    private Canvas canvas;
    private Random random;
    private Paint paint;

    public SignatureImpl(String date,int mode){//TODO: creazione immagine

        try {
            //dati
            calendar=Calendar.getInstance();
            calendar.setTime(sdf.parse(date));
            dates=new int[6];
            elaborateDate();
            switch (mode) {
                case PEAKED_CIRCLE:usePeaks();
                default: usePeaks(); break;
            }

        } catch (ParseException e) {e.printStackTrace();}
    }

    private void usePeaks()
    {
        double angleGap;
        int radius;
        int peaksNumber;
        int betweenPeaks;
        int maxRandomDistance;
        int resolution=500;

        signature= Bitmap.createBitmap(resolution, resolution, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(signature);

        peaksNumber =6;
        peaks=new float[peaksNumber];
        betweenPeaks=(360/peaksNumber)/3;
        angleGap=2*Math.PI/ (peaksNumber+peaksNumber*betweenPeaks);
        radius=resolution/4;
        space=resolution/2-radius;
        maxRandomDistance=space/10;

        //calcolo le punte
        calculatePeaks();
        peaks[0]=space/2+((hour*120+minute*60+second)/(23*120+59*60+59))*space/2;
        Point point=new Point();
        random=new Random();
        double currentAngle=0;
        currentAngle+=angleGap;
        int x=(int)(resolution / 2 + (radius + random.nextInt(maxRandomDistance)) * Math.sin(currentAngle));
        int y=(int)(resolution / 2 + (radius + random.nextInt(maxRandomDistance)) * Math.cos(currentAngle));

        if(y<resolution/15)
            y=resolution/15;
        if(y>resolution-resolution/15)
            y=resolution-resolution/15;

        point.set(x,y);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(resolution/2,resolution/2+(radius+peaks[0]),x,y,paint);
        int sign=1;
        int index=1;
        for(int j=2;j<(betweenPeaks*peaksNumber+peaksNumber);j++)
        {
            currentAngle+=angleGap;
            if(j%(betweenPeaks+1)!=0) {
                paint.setColor(Color.RED);
                x = (int) (resolution / 2 + (radius + sign * random.nextInt(maxRandomDistance)) * Math.sin(currentAngle));
                y = (int) (resolution / 2 + (radius + sign * random.nextInt(maxRandomDistance)) * Math.cos(currentAngle));

            }else
            {
                paint.setColor(Color.BLUE);
                x = (int) (resolution / 2 + (radius+space/2 + peaks[index]) * Math.sin(currentAngle));
                y = (int) (resolution / 2 + (radius +space/2+  peaks[index++]) * Math.cos(currentAngle));
            }
            if(j%(betweenPeaks+1)==1) {
                paint.setColor(Color.BLUE);
            }

            if(y<resolution/20)
                y=resolution/20;
            if(y>resolution-resolution/20)
                y=resolution-resolution/20;

            canvas.drawLine(point.x, point.y, x, y, paint);
            point.set(x, y);
            sign *= -1;

        }
        paint.setColor(Color.BLUE);
        canvas.drawLine(resolution/2,resolution/2+(radius+peaks[0]),x,y,paint);
    }

    private void calculatePeaks()
    {
        random=new Random();
        for(int i=1;i<peaks.length;i++)
        {
                int temp;
                if (i < dates.length)
                    temp = dates[i];
                else
                    temp = random.nextInt(sum-2);

                peaks[i]=space*temp/sum;

        }

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

    @Override
    public Bitmap toBitmap() {
        //TODO;
        return signature;
    }
}

package org.tbw.FemurShield.Controller;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import org.tbw.FemurShield.Model.Fall;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Created by Marco on 14/06/2015.
 */
public class FallBitmapCreator extends AsyncTask<Integer, Void, Bitmap> {

    public final String TAG = this.getClass().getSimpleName();

    private final WeakReference<ImageView> ivFallReference,ivSentSignReference;

    private float[][] dataBefore;
    private float[][] dataDuring;
    private float[][] dataAfter;

    private float[] oldPointsY;
    private float[] newPointsY;
    private float xPointer,xPointerPlus,xStep;

    private float scale;

    Canvas canvasGraficoAcc;
    Bitmap bitmapGraficoAcc;
    private int signHeight,signWidth;

    Paint paintX = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paintY = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paintZ = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String[] color_palette;


    public FallBitmapCreator(ImageView ivSentSign, ImageView ivFall, Fall fall, int height, int width, String[] palette) {

        ivSentSignReference = new WeakReference<ImageView>(ivSentSign);
        ivFallReference = new WeakReference<ImageView>(ivFall);
        color_palette=palette;
        //quanto occupa sullo schermo il disegno di fall?
        signHeight=height/3;
        signWidth=width;
        scale=4;

        dataBefore=fall.getValuesBeforeFall();
        dataDuring=fall.getFallValues();
        dataAfter=fall.getValuesAfterFall();

        //questo regola la scala in base
        //all'altezza del grafico z
        //scale=(signHeight/2)/(getMax(dataBefore,dataDuring,dataAfter)*1.5f);

        xStep=((float)signWidth)/(dataBefore[0].length+dataDuring[0].length+dataAfter[0].length);
        
        paintX.setColor(Color.parseColor(color_palette[0]));
        paintX.setStrokeWidth(2.0f);

        paintY.setColor(Color.parseColor(color_palette[1]));
        paintY.setStrokeWidth(2.0f);

        paintZ.setColor(Color.parseColor(color_palette[2]));
        paintZ.setStrokeWidth(2.0f);

        bitmapGraficoAcc = Bitmap.createBitmap(signWidth,signHeight, Bitmap.Config.ARGB_8888);
        canvasGraficoAcc = new Canvas(bitmapGraficoAcc);

        xPointer=0.0f;
        oldPointsY=new float[]{signHeight/2,signHeight/2,signHeight/2};


    }

    private float getMax(float[][] x,float[][] y,float[][] z)
    {
        float[] xmax=new float[x.length];
        float[] ymax=new float[y.length];
        float[] zmax=new float[z.length];
        for(int i=0;i<x.length;i++)
            xmax[i]=getMax(x[i]);
        for(int i=0;i<y.length;i++)
            ymax[i]=getMax(y[i]);
        for(int i=0;i<x.length;i++)
            zmax[i]=getMax(z[i]);
        return getMax(xmax,ymax,zmax);
    }

    private float getMax(float[] x,float [] y,float[] z)
    {
        float massimoX=getMax(x);
        float massimoY=getMax(y);
        float massimoZ=getMax(z);

        if(massimoX>massimoY)
            return Math.max(massimoX,massimoZ);
        else
            return Math.max(massimoY,massimoZ);

    }

    private float getMax(float[] f)
    {
        float max=-1;
        if(f.length>0) {
            max = Math.abs(f[0]);

            for (int i = 0; i < f.length; i++) {
                if (Math.abs(f[i]) > max) {
                    max = f[i];
                }
            }
        }
        return max;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {

        for(int i=0;i<dataBefore[0].length;i++)
        {
            drawValues(scale * dataBefore[0][i], scale * dataBefore[1][i], scale * dataBefore[2][i]);
        }
        for(int i=0;i<dataDuring[0].length;i++)
        {
            drawValues(scale * dataDuring[0][i], scale * dataDuring[1][i], scale * dataDuring[2][i]);
        }
        for(int i=0;i<dataAfter[0].length;i++)
        {
            drawValues(scale * dataAfter[0][i], scale * dataAfter[1][i], scale * dataAfter[2][i]);
        }
        return bitmapGraficoAcc;
    }




    public void drawValues(float newX, float newY, float newZ){
        if(xPointer<=signWidth) {
            float X = checkValue(newX+signHeight/2);
            float Y= checkValue(newY+signHeight/2);
            float Z= checkValue(newZ+signHeight/2);

            xPointerPlus=xPointer+xStep;
            newPointsY = new float[]{X, Y, Z};
            canvasGraficoAcc.drawLine(xPointer, oldPointsY[0], xPointerPlus, newPointsY[0], paintX);
            canvasGraficoAcc.drawLine(xPointer, oldPointsY[1], xPointerPlus, newPointsY[1], paintY);
            canvasGraficoAcc.drawLine(xPointer, oldPointsY[2], xPointerPlus, newPointsY[2], paintZ);
            xPointer=xPointerPlus;
            oldPointsY= Arrays.copyOf(newPointsY,newPointsY.length);

        }else{Log.d("FallBitmapCreator","Troppi Dati!");}
    }

    private float checkValue(float i)
    {
        float temp=i;
        if(i<0)
            temp=0;
        if (i>signHeight)
            temp=signHeight;
        return temp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (ivFallReference != null && bitmap != null) {
            final ImageView fallImage = ivFallReference.get();
            final ImageView sentImage = ivSentSignReference.get();
            if (fallImage != null&&sentImage!=null) {
                fallImage.setImageBitmap(bitmap);

                int cx = (sentImage.getLeft() + sentImage.getRight()) / 2;
                int cy = (sentImage.getTop() + sentImage.getBottom()) / 2;

                int finalRadius = Math.max(sentImage.getWidth(), sentImage.getHeight());

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(sentImage, cx, cy, 0, finalRadius);
                
                anim.setDuration(600);
                sentImage.setVisibility(View.VISIBLE);
                anim.start();

            }
            else{Log.d(TAG,"Problema caricamento riferimento imageView");}
        }
    }


}

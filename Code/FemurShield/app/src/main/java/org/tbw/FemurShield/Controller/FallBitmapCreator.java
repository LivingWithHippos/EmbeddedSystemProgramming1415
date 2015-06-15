package org.tbw.FemurShield.Controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.tbw.FemurShield.Model.Fall;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Created by Marco on 14/06/2015.
 */
public class FallBitmapCreator extends AsyncTask<Integer, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    private float[][] dataBefore;
    private float[][] dataDuring;
    private float[][] dataAfter;

    private float[] oldPointsY;
    private float[] newPointsY;
    private float xPointer,xPointerPlus,xStep;

    private int scale;

    Canvas canvasGraficoAcc;
    Bitmap bitmapGraficoAcc;
    private int signHeight,signWidth;
    private View view;

    Paint paintX = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paintY = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paintZ = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String[] color_palette;


    public FallBitmapCreator(View view, ImageView imageView, Fall fall, int height, int width, String[] palette) {

        this.view=view;
        imageViewReference = new WeakReference<ImageView>(imageView);
        color_palette=palette;
        signHeight=height/4;
        signWidth=width;
        scale=4;


        dataBefore=fall.getValuesBeforeFall();
        dataDuring=fall.getFallValues();
        dataAfter=fall.getValuesAfterFall();
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

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
               view.invalidate();

            }
        }
    }


}

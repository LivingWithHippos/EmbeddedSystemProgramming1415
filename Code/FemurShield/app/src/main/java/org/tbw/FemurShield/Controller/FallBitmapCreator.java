package org.tbw.FemurShield.Controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.tbw.FemurShield.Model.Fall;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Classe che si occupa di creare il grafico di una caduta partendo dai suoi valori.
 * Vista la quantità ridotta di dati, viene ricalcolata sempre e mai salvata.
 * Sfrutto un task asincrono in modo da non bloccare la ui col disegno.
 *
 * @author Marco Biasin
 */
public class FallBitmapCreator extends AsyncTask<Integer, Void, Bitmap> {

    public final String TAG = this.getClass().getSimpleName();

    private final WeakReference<ImageView> ivFallReference;

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

    /**
     * Costruttore pubblico
     * @param ivFall l'ImageView su cui mostrare il grafico
     * @param fall il fall da mostrare
     * @param height l'altezza dello schermo
     * @param width la larghezza dello schermo
     * @param palette i 3 colori da utilizzare
     */
    public FallBitmapCreator(ImageView ivFall, Fall fall, int height, int width, String[] palette) {


        ivFallReference = new WeakReference<ImageView>(ivFall);
        color_palette=palette;
        //quanto occupa sullo schermo il disegno di fall?
        signHeight=height/3;
        signWidth=width;
        //viene regolata in checkvalue()
        scale=4;

        dataBefore=fall.getValuesBeforeFall();
        dataDuring=fall.getFallValues();
        dataAfter=fall.getValuesAfterFall();

        //questo regola la scala in base al massimo valore disegnabile
        //troppo time-consuming ;(
        //scale=(signHeight/2)/(getMax(dataBefore,dataDuring,dataAfter)*1.5f);

        //di quanto avanzo ad ogni passo
        xStep=((float)signWidth)/(dataBefore[0].length+dataDuring[0].length+dataAfter[0].length);
        //colori
        paintX.setColor(Color.parseColor(color_palette[0]));
        paintX.setStrokeWidth(2.0f);

        paintY.setColor(Color.parseColor(color_palette[1]));
        paintY.setStrokeWidth(2.0f);

        paintZ.setColor(Color.parseColor(color_palette[2]));
        paintZ.setStrokeWidth(2.0f);
        //bitmap su cio disegnare
        bitmapGraficoAcc = Bitmap.createBitmap(signWidth,signHeight, Bitmap.Config.ARGB_8888);
        canvasGraficoAcc = new Canvas(bitmapGraficoAcc);
        //indice di avanzamento sull'asse x
        xPointer=0.0f;
        //float che conserve i valori delle y precedenti per colegarle a quelle nuove calcolate
        oldPointsY=new float[]{signHeight/2,signHeight/2,signHeight/2};


    }

    // ritorna il massimo tra tre array bidimensionali di float
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

    // ritorna il massimo tra tre array monodimensionali di float
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

    //ritorna il massimo di un array di float
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

    // Crea l'immagine in background
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


    /**
     * Disegna i tre valori sulla BItmap
     * @param newX il valore da disegnare dall'asse x dell'accelerometro
     * @param newY il valore da disegnare dall'asse y dell'accelerometro
     * @param newZ il valore da disegnare dall'asse z dell'accelerometro
     */
    private void drawValues(float newX, float newY, float newZ){
        //se non sono arrivato in fondo disegno
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

        }else{
            //in teoria non succede mai perchè i passi sono calcolati in base alla quantità di dati presente
            Log.d("FallBitmapCreator","Troppi Dati!");}
    }

    /**
     * Controlla che i valori non siano al di fuori dei bordi dell'immagine
     * @param i il float da controllare
     * @return il float originale o modificato se aveva valori troppo grandi o piccoli
     */
    private float checkValue(float i)
    {
        float temp=i;
        if(i<0) {
            temp = 0;
        }
        if (i>signHeight) {
            temp = signHeight;
        }
        if(i>0&i<signHeight)
            scale=4;
        else{scale=3.8f;}
        return temp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        //se l'imageview è ancora presente sullo schermo disegno la bitmap
        if (ivFallReference != null && bitmap != null) {
            final ImageView fallImage = ivFallReference.get();
            if (fallImage != null) {
                fallImage.setImageBitmap(bitmap);
            }
            else{Log.d(TAG,"Problema caricamento riferimento imageView");}
        }
    }


}

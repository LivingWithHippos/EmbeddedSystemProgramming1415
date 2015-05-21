package org.tbw.FemurShield.Controller;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.tbw.FemurShield.R;

import java.util.Observable;
import java.util.Observer;


public class UI3 extends Activity implements org.tbw.FemurShield.Observer.Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui3);
        paintX.setColor(Color.BLACK);
        paintX.setStrokeWidth(6.0f);

        paintY.setColor(Color.RED);
        paintY.setStrokeWidth(6.0f);

        paintZ.setColor(Color.GREEN);
        paintZ.setStrokeWidth(6.0f);

        paintCanc.setColor(Color.rgb(241,241,241));
        paintCanc.setStyle(Paint.Style.FILL);

        Paint line= new Paint();
        line.setColor(Color.MAGENTA);
        line.setStrokeWidth(3.0f);


        ImageView imageViewGraficoAcc= (ImageView)findViewById(R.id.graficoacc);
        bitmapGraficoAcc = Bitmap.createBitmap( getWindowManager()
                .getDefaultDisplay().getWidth(), getWindowManager()
                .getDefaultDisplay().getHeight()/2, Bitmap.Config.ARGB_8888);
        canvasGraficoAcc = new Canvas(bitmapGraficoAcc);

        canvasGraficoAcc.drawLine(0, distCentro, getWindowManager()
                .getDefaultDisplay().getWidth(), distCentro, line);
        ((ImageView)findViewById(R.id.graficoacc)).setImageBitmap(bitmapGraficoAcc);
        imageViewGraficoAcc.draw(canvasGraficoAcc);
        larghezzachart=getWindowManager().getDefaultDisplay().getWidth();
        Controller.getNotification().addObserver(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(org.tbw.FemurShield.Observer.Observable oggettoosservato, Object o) {
        //aggiornare la UI in base ai vallori ricevuti TODO
        if(o instanceof float[]){
            float[] arg=(float[])o;
            DrawGraphSliceAcc(arg[0], arg[1], arg[2]);
        }
        //TODO controllo che aggiornamtno ho
    }

    float distCentro=400.0f;
    float larghezzachart;

    float yIndexAcc= 0.0f;
    float xOldXAcc=distCentro;
    float xOldYAcc=distCentro;
    float xOldZAcc=distCentro;

    Canvas canvasGraficoAcc;
    Bitmap bitmapGraficoAcc;

    Paint paintX = new Paint();

    Paint paintY = new Paint();

    Paint paintZ = new Paint();

    Paint paintCanc = new Paint();

    public void DrawGraphSliceAcc(float newX, float newY, float newZ){
        EraseNextAcc(yIndexAcc+1.0f);
        DrawSegmentXAcc(yIndexAcc, xOldXAcc, yIndexAcc + 1.0f, newX + distCentro);
        xOldXAcc=newX+distCentro;
        DrawSegmentYAcc(yIndexAcc, xOldYAcc, yIndexAcc + 1.0f, newY + distCentro);
        xOldYAcc=newY+distCentro;
        DrawSegmentZAcc(yIndexAcc, xOldZAcc, yIndexAcc + 1.0f, newZ + distCentro);
        xOldZAcc=newZ+distCentro;


        if(yIndexAcc>=larghezzachart){
            yIndexAcc=0;
        }
        else{
            yIndexAcc+=1;
        }
    }

    private void EraseNextAcc(float v) {
        canvasGraficoAcc.drawRect(v, 0, v + 15.0f, 3000, paintCanc);
        ((ImageView)findViewById(R.id.graficoacc)).setImageBitmap(bitmapGraficoAcc);
    }

    private void DrawSegmentXAcc( float xStart, float yStart, float xStop, float yStop){
        canvasGraficoAcc.drawLine(xStart, yStart, xStop, yStop, paintX);
        ((ImageView)findViewById(R.id.graficoacc)).setImageBitmap(bitmapGraficoAcc);
    }

    private void DrawSegmentYAcc(float xStart, float yStart, float xStop, float yStop){
        canvasGraficoAcc.drawLine(xStart, yStart, xStop, yStop, paintY);
        ((ImageView)findViewById(R.id.graficoacc)).setImageBitmap(bitmapGraficoAcc);
    }

    private void DrawSegmentZAcc(float xStart, float yStart, float xStop, float yStop){
        canvasGraficoAcc.drawLine(xStart, yStart, xStop, yStop, paintZ);
        ((ImageView)findViewById(R.id.graficoacc)).setImageBitmap(bitmapGraficoAcc);
    }
}

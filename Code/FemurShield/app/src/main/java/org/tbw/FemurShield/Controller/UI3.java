package org.tbw.FemurShield.Controller;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.HashMap;


public class UI3 extends Activity implements org.tbw.FemurShield.Observer.Observer {
    private long duration=0;

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

        ((TextView)findViewById(R.id.session_name)).setText(SessionManager.getInstance().getActiveSession().getName());
        ((TextView)findViewById(R.id.session_date)).setText(SessionManager.getInstance().getActiveSession().getDataTime());
        //TODO timer nel controller che si stoppa e riparte a seconda degli eventi
        ((TextView)findViewById(R.id.num_fall)).setText("" + SessionManager.getInstance().getActiveSession().getFallsNumber());

        ((Chronometer)findViewById(R.id.chronometerui3)).setBase(Controller.getInstance().getActualChronoBase());
        if(Controller.getInstance().isRunning()){
            ((ImageView)findViewById(R.id.pausebtnui3)).setVisibility(ImageView.VISIBLE);
            ((ImageView)findViewById(R.id.stopbntui3)).setVisibility(ImageView.VISIBLE);
            ((Chronometer)findViewById(R.id.chronometerui3)).start();
        }
        else {
            ((ImageView) findViewById(R.id.startbtnui3)).setVisibility(ImageView.VISIBLE);

        }
    }

    @Override
    public void onResume(){
        super.onResume();

        if(Controller.getInstance().isRecording()) {
            if (Controller.getInstance().isRunning()) {
                ((ImageView)findViewById(R.id.pausebtnui3)).setVisibility(ImageView.VISIBLE);
                ((ImageView)findViewById(R.id.stopbntui3)).setVisibility(ImageView.VISIBLE);
                ((ImageView)findViewById(R.id.startbtnui3)).setVisibility(ImageView.INVISIBLE);
            }
            else{
                ((ImageView)findViewById(R.id.pausebtnui3)).setVisibility(ImageView.INVISIBLE);
                ((ImageView)findViewById(R.id.stopbntui3)).setVisibility(ImageView.VISIBLE);
                ((ImageView)findViewById(R.id.startbtnui3)).setVisibility(ImageView.VISIBLE);
            }
        }
        else{
            ((ImageView)findViewById(R.id.pausebtnui3)).setVisibility(ImageView.INVISIBLE);
            ((ImageView)findViewById(R.id.stopbntui3)).setVisibility(ImageView.INVISIBLE);
            ((ImageView)findViewById(R.id.startbtnui3)).setVisibility(ImageView.INVISIBLE);
        }
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
        if(o instanceof float[]){
            float[] arg=(float[])o;
            DrawGraphSliceAcc(arg[0]*6, arg[1]*6, arg[2]*6);
        }
        else{
            if(o instanceof Fall){
                ((TextView)findViewById(R.id.num_fall)).setText(""+SessionManager.getInstance().getActiveSession().getFallsNumber());
                aggiornaListaCadute();
            }
        }
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

    public void onPauseClick(View view){
        //TODO: business logic
        Controller.getInstance().PauseSession(this);
        ((Chronometer)findViewById(R.id.chronometerui3)).stop();
        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.pausebtnui3)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui3)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui3)).setVisibility(ImageView.VISIBLE);

    }

    public void onPlayClick(View view){
        //TODO: business logic
        Controller.getInstance().StartSession(this);
        ((Chronometer)findViewById(R.id.chronometerui3)).setBase(Controller.getInstance().getActualChronoBase());
        ((Chronometer)findViewById(R.id.chronometerui3)).start();
        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.pausebtnui3)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.stopbntui3)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui3)).setVisibility(ImageView.INVISIBLE);

    }

    public void onStopClick(View view){
        //TODO: business logic
        ((Chronometer)findViewById(R.id.chronometerui3)).stop();
        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.pausebtnui3)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui3)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.startbtnui3)).setVisibility(ImageView.INVISIBLE);
        Intent i = new Intent(this,UI2.class);
        i.putExtra(UI2.SESSION_DATA_STAMP, SessionManager.getInstance().getActiveSession().getDataTime());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Controller.getInstance().StopSession(this);
        startActivity(i);
    }

    public void aggiornaListaCadute(){
        ArrayList<Fall> falls =SessionManager.getInstance().getActiveSession().getFalls();

        //Questa è la lista che rappresenta la sorgente dei dati della listview
        //ogni elemento è una mappa(chiave->valore)

        ArrayList<HashMap<String, Object>> data=new ArrayList<>();


        for(int i=falls.size()-1;i>=0;i--){
            Fall s=falls.get(i);// per ogni sessione all'inteno della lista

            HashMap<String,Object> sessionMap=new HashMap<>();//creiamo una mappa di valori


            sessionMap.put("date", s.getData());
            if(s.isReported())
                sessionMap.put("state","Segnalata");
            else
                sessionMap.put("state","Non segnalata");

            data.add(sessionMap);  //aggiungiamo la mappa di valori alla sorgente dati
        }


        String[] from={"date","state"}; //dai valori contenuti in queste chiavi
        int[] to={R.id.data_ora_caduta_ui3,R.id.stato_segnalazione_ui3};//agli id delle view

        //costruzione dell adapter
        SimpleAdapter adapter=new SimpleAdapter(
                getApplicationContext(),
                data,//sorgente dati
                R.layout.falllistitemui3, //layout contenente gli id di "to"
                from,
                to);

        //utilizzo dell'adapter
        ((ListView)findViewById(R.id.listfallui3)).setAdapter(adapter);

        //TODO inviata non inviata mail
    }
}

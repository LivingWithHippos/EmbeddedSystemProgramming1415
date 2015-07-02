package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.Observer.Observable;
import org.tbw.FemurShield.R;

/**
 * Created by Marco on 26/06/2015.
 */
public class ActiveSessionFragment extends Fragment implements org.tbw.FemurShield.Observer.Observer{

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
    Paint line= new Paint();

    ImageView imageViewGraficoAcc;


    public ActiveSessionFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paintX.setColor(Color.BLACK);
        paintX.setStrokeWidth(6.0f);

        paintY.setColor(Color.RED);
        paintY.setStrokeWidth(6.0f);

        paintZ.setColor(Color.GREEN);
        paintZ.setStrokeWidth(6.0f);

        paintCanc.setColor(Color.rgb(241, 241, 241));
        paintCanc.setStyle(Paint.Style.FILL);

        Paint line= new Paint();
        line.setColor(Color.MAGENTA);
        line.setStrokeWidth(3.0f);

    }

    public static ActiveSessionFragment newIstance()
    {
        ActiveSessionFragment fragment = new ActiveSessionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_active_session, container,false);


        imageViewGraficoAcc= (ImageView)rootView.findViewById(R.id.graficoacc);
        bitmapGraficoAcc = Bitmap.createBitmap( getActivity().getWindowManager()
                .getDefaultDisplay().getWidth(), getActivity().getWindowManager()
                .getDefaultDisplay().getHeight()/2, Bitmap.Config.ARGB_8888);
        canvasGraficoAcc = new Canvas(bitmapGraficoAcc);

        canvasGraficoAcc.drawLine(0, distCentro, getActivity().getWindowManager()
                .getDefaultDisplay().getWidth(), distCentro, line);
        imageViewGraficoAcc.setImageBitmap(bitmapGraficoAcc);
        imageViewGraficoAcc.draw(canvasGraficoAcc);
        larghezzachart=getActivity().getWindowManager().getDefaultDisplay().getWidth();
        Controller.getNotification().addObserver(this);

        Chronometer chrono=(Chronometer) rootView.findViewById(R.id.chronometerui3);
        chrono.setBase(Controller.getInstance().getActualChronoBase());
        if(Controller.getInstance().isRunning()){
            chrono.start();
        }

        return rootView;
    }


    public void startChrono()
    {
        Chronometer chrono=(Chronometer) getView().findViewById(R.id.chronometerui3);
        chrono.setBase(Controller.getInstance().getActualChronoBase());
        chrono.start();
    }

    public void stopChrono()
    {
        Chronometer chrono=(Chronometer) getView().findViewById(R.id.chronometerui3);
        chrono.stop();
    }

    @Override
    public void update(Observable oggettoosservato, Object o) {
        if(o instanceof float[]){
            float[] arg=(float[])o;
            DrawGraphSliceAcc(arg[0]*6, arg[1]*6, arg[2]*6);
        }
    }

    public void DrawGraphSliceAcc(float newX, float newY, float newZ){
        EraseNextAcc(yIndexAcc+1.0f);
        DrawSegmentXAcc(yIndexAcc, xOldXAcc, yIndexAcc + 1.0f, newX + distCentro);
        xOldXAcc=newX+distCentro;
        DrawSegmentYAcc(yIndexAcc, xOldYAcc, yIndexAcc + 1.0f, newY + distCentro);
        xOldYAcc=newY+distCentro;
        DrawSegmentZAcc(yIndexAcc, xOldZAcc, yIndexAcc + 1.0f, newZ + distCentro);
        xOldZAcc=newZ+distCentro;
        imageViewGraficoAcc.invalidate();


        if(yIndexAcc>=larghezzachart){
            yIndexAcc=0;
        }
        else{
            yIndexAcc+=1;
        }
    }

    private void EraseNextAcc(float v) {
        canvasGraficoAcc.drawRect(v, 0, v + 15.0f, 3000, paintCanc);
    }

    private void DrawSegmentXAcc( float xStart, float yStart, float xStop, float yStop){
        canvasGraficoAcc.drawLine(xStart, yStart, xStop, yStop, paintX);
    }

    private void DrawSegmentYAcc(float xStart, float yStart, float xStop, float yStop){
        canvasGraficoAcc.drawLine(xStart, yStart, xStop, yStop, paintY);
    }

    private void DrawSegmentZAcc(float xStart, float yStart, float xStop, float yStop){
        canvasGraficoAcc.drawLine(xStart, yStart, xStop, yStop, paintZ);
    }
}

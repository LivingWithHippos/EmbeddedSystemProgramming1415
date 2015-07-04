package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Chronometer;
import android.widget.ImageView;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Observer.Observable;
import org.tbw.FemurShield.R;

/**
 * Created by Moro on 26/06/2015.
 */
public class ActiveSessionFragment extends Fragment implements org.tbw.FemurShield.Observer.Observer{

    private OnEmailSentListener emailCallback;
    private OnFallDetectedListener fallCallback;
    private static final String TAG="ActiveSessionFragment";

    int finalWidth;
    int finalHeight;
    int sizeCoefficient=2;

    float xIndexAcc = 0.0f;
    float yOldXAcc;
    float yOldYAcc;
    float yOldZAcc;

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
        setRetainInstance(false);

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_active_session, container,false);


        imageViewGraficoAcc= (ImageView)rootView.findViewById(R.id.graficoacc);
        ViewTreeObserver vto = imageViewGraficoAcc.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageViewGraficoAcc.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = imageViewGraficoAcc.getMeasuredHeight();
                finalWidth = imageViewGraficoAcc.getMeasuredWidth();
                yOldXAcc =finalHeight/2;
                yOldYAcc =finalHeight/2;
                yOldZAcc =finalHeight/2;

                bitmapGraficoAcc = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888);
                canvasGraficoAcc = new Canvas(bitmapGraficoAcc);

                canvasGraficoAcc.drawLine(0, finalHeight / 2, finalWidth, finalHeight / 2, line);
                imageViewGraficoAcc.setImageBitmap(bitmapGraficoAcc);
                imageViewGraficoAcc.draw(canvasGraficoAcc);
                attachObserver();

                return true;
            }
        });

        Chronometer chrono=(Chronometer) rootView.findViewById(R.id.chronometerui3);
        chrono.setBase(Controller.getInstance().getActualChronoBase());
        if(Controller.getInstance().isRunning()){
            chrono.start();
        }

        return rootView;
    }

    private void attachObserver()
    {
        Controller.getNotification().addObserver(this);
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
    public void update(Observable oggettoosservato, Object o)
    {
        if(o instanceof float[])
        {
            if(bitmapGraficoAcc!=null) {
                float[] arg = (float[]) o;
                DrawGraphSliceAcc(arg[0] * sizeCoefficient, arg[1] * sizeCoefficient, arg[2] * sizeCoefficient);
            }
        }
        else
            if(o instanceof Fall)
            {
                 fallCallback.onFallDetect();
             }
            else
                if(o instanceof EmailSentSegnalation){
                    emailCallback.onEmailSent();
            }
    }

    public void DrawGraphSliceAcc(float newX, float newY, float newZ){
        EraseNextAcc(xIndexAcc +1.0f);
        DrawSegmentXAcc(xIndexAcc, yOldXAcc, xIndexAcc + 1.0f, newX + (finalHeight/2));
        yOldXAcc =newX+(finalHeight/2);
        DrawSegmentYAcc(xIndexAcc, yOldYAcc, xIndexAcc + 1.0f, newY + (finalHeight/2));
        yOldYAcc =newY+(finalHeight/2);
        DrawSegmentZAcc(xIndexAcc, yOldZAcc, xIndexAcc + 1.0f, newZ + (finalHeight/2));
        yOldZAcc =newZ+(finalHeight/2);
        imageViewGraficoAcc.invalidate();


        if(xIndexAcc >=finalWidth){
            xIndexAcc =0;
        }
        else{
            xIndexAcc +=1;
        }
    }

    private void EraseNextAcc(float v) {
        //larghezza modifica
        canvasGraficoAcc.drawRect(v, 0, v + (sizeCoefficient*2), finalHeight, paintCanc);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            emailCallback = (OnEmailSentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEmailSentListener");
        }
        try {
            fallCallback = (OnFallDetectedListener) activity;
            Log.d(TAG,"onAttach partto");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFallDetectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        emailCallback = null;
        fallCallback=null;
    }

    public interface OnEmailSentListener
    {
        public void onEmailSent();
    }
    public interface OnFallDetectedListener
    {
        public void onFallDetect();
    }
}

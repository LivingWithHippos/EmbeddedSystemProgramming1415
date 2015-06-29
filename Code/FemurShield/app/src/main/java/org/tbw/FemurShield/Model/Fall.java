package org.tbw.FemurShield.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Moro on 30/04/15.
 * Edited by Vianello
 */
public class Fall {
    private static int i=0;
    private int id;
    public static final int FALL_LATITUDE=0;
    public static final int FALL_LONGITUDE=1;
    public static final int X_INDEX=0;
    public static final int Y_INDEX=1;
    public static final int Z_INDEX=2;
    public static final int TUMBLE_OR_MALAISE=0;
    public static final int IMPACT=1;

    private double[] position=new double[2];
    private String data;
    private boolean segnalato=false;
    private float[][] valuesBeforeFall= new float[3][];
    private float[][] valuesFall= new float[3][];
    private float[][] valuesAfterFall= new float[3][];

    public Fall(float[][] beforeValues, float[][] FallValues, float[][] afterValues,double lat,double longi)
    {
        Log.d("Fall", "Creating Fall Event");
        id=i++;

        valuesBeforeFall=beforeValues;
        valuesFall=FallValues;
        valuesAfterFall=afterValues;

        SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
        data=sdf.format(new Date());

        position[FALL_LATITUDE] = lat;
        position[FALL_LONGITUDE] = longi;
    }

    public Fall(float[][] beforeValues, float[][] FallValues, float[][] afterValues)
    {
        Log.d("Fall", "Creating Fall Event");
        id=i++;

        valuesBeforeFall=beforeValues;
        valuesFall=FallValues;
        valuesAfterFall=afterValues;

        SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
        data=sdf.format(new Date());
    }

    /**
     * da usare per il ripristino
     */
    Fall(float[][] beforeValues, float[][] FallValues, float[][] afterValues, int id,String data, double lat, double longi, boolean segnalato)
    {
        this.valuesAfterFall=afterValues;
        this.valuesFall=FallValues;
        this.valuesBeforeFall=beforeValues;
        this.id=id;
        this.data=data;
        this.position[this.FALL_LATITUDE]=lat;
        this.position[this.FALL_LONGITUDE]=longi;
        this.segnalato=segnalato;
    }

    public double[] getPosition(){
        return position;
    }

    public boolean isReported(){
        return segnalato;
    }

    public void setReported(){
        segnalato=true;
    }

    public float[][] getValuesBeforeFall(){
        return valuesBeforeFall;
    }

    public float[][] getValuesAfterFall(){
        return valuesAfterFall;
    }

    public float[][] getFallValues(){
        return valuesFall;
    }

    public double getPosition(int pos){
        return position[pos];
    }

    public int getId() { return id; }

    public String getData() { return data;}

    public void setPosition(double latitude, double longitude) {
        this.position[this.FALL_LATITUDE]=latitude;
        this.position[this.FALL_LONGITUDE]=longitude;
    }
}
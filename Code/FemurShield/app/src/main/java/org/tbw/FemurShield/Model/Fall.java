package org.tbw.FemurShield.Model;

import java.util.HashMap;
import java.util.Map;

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

    private double[] position=new double[2];

    private boolean segnalato=false;
    private float[][] valuesBeforeFall= new float[3][];
    private float[][] valuesFall= new float[3][];
    private float[][] valuesAfterFall= new float[3][];

    public Fall(float[][] beforeValues, float[][] FallValues, float[][] afterValues){

        id=i++;

        valuesBeforeFall=beforeValues;
        valuesFall=FallValues;
        valuesAfterFall=afterValues;

        position[FALL_LATITUDE]= 0.00;      //TODO: usare LOCATEMANAGER (vedi Criteria) per inizializzare la posizione
        position[FALL_LONGITUDE] = 0.00;    //TODO: usare LOCATEMANAGER (vedi Criteria) per inizializzare la posizione
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

    public float[][] GetValuesBeforeFall(){
        return valuesBeforeFall;
    }

    public float[][] GetValuesAfterFall(){
        return valuesAfterFall;
    }

    public float[][] GetFallValues(){
        return valuesFall;
    }

    public int getId() { return id; }
}

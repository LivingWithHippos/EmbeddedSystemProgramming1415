package Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moro on 30/04/15.
 */
public class Fall {

    public final int FALL_LATITUDE=0;
    public final int FALL_LONGITUDE=1;
    public final int X_INDEX=0;
    public final int Y_INDEX=1;
    public final int Z_INDEX=2;

    private double[] position=new double[2];

    private boolean segnalato=false;;
    private float[][] valuesBeforeFall= new float[3][];
    private float[][] valuesFall= new float[3][];
    private float[][] valuesAfterFall= new float[3][];

    public Fall(float[][] beforeValues, float[][] FallValues, float[][] afterValues){
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
}

package org.tbw.FemurShield.Model;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import org.tbw.FemurShield.Controller.LocationLocator;
import org.tbw.FemurShield.Controller.MultiEmailSender;

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
    public static final int TUMBLE_OR_MALAISE=0;
    public static final int IMPACT=1;

    private double[] position=new double[2];

    private boolean segnalato=false;
    private float[][] valuesBeforeFall= new float[3][];
    private float[][] valuesFall= new float[3][];
    private float[][] valuesAfterFall= new float[3][];

    private Context cont;

    public Fall(float[][] beforeValues, float[][] FallValues, float[][] afterValues, Context contx)
    {
        Log.d("Fall", "Creating Fall Event");
        id=i++;

        valuesBeforeFall=beforeValues;
        valuesFall=FallValues;
        valuesAfterFall=afterValues;

        cont = contx;

        setPostion();
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

    private void setPostion()//serve per inizializzare i valori di position usando locationmanager
    {
        LocationLocator.LocationResult locationResult = new LocationLocator.LocationResult(){
            @Override
            public void gotLocation(Location location)
            {
                position[FALL_LATITUDE] = location.getLatitude(); // leggo la latuditine e la metto in position
                position[FALL_LONGITUDE] = location.getLongitude(); // idem con la longitudine
                sendEmail();
            }
        };
        LocationLocator myLocation = new LocationLocator();
        myLocation.getLocation(cont, locationResult);
    }

    private void sendEmail()
    {

        Intent sender = new Intent(cont.getApplicationContext(),MultiEmailSender.class);
        sender.putExtra("appdirectory", cont.getFilesDir().toString()); // passo la cartella in cui c'è il file con gli indirizzi salvati
        sender.putExtra("latCaduta", position[FALL_LATITUDE]);
        sender.putExtra("lonCaduta", position[FALL_LONGITUDE]);
        sender.putExtra("idCaduta", getId());
        cont.startService(sender);
    }
}
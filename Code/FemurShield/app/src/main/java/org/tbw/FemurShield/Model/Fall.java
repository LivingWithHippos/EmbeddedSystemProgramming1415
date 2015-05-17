package org.tbw.FemurShield.Model;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

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
        Location loc;
        LocationManager lm = (LocationManager) cont.getSystemService(Context.LOCATION_SERVICE);

        LocationListener ll = new LocationListener() // non mi interessa implementare i metodi in quanto mi serve solo location
        {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        String p = "";

        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) //verifica se il GPS attivo e sceglie il provider
        {
            p = LocationManager.GPS_PROVIDER;
        }
        else if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) //idem con il network
        {
            p = LocationManager.NETWORK_PROVIDER;
        }

        lm.requestSingleUpdate(p, ll, null); // chiede una lettura dal listener
        loc = lm.getLastKnownLocation(p); //chiede la location della ultima lettura

        position[FALL_LATITUDE] = loc.getLatitude(); // leggo la latuditine e la metto in position
        position[FALL_LONGITUDE] = loc.getLongitude(); // idem con la longitudine

        lm.removeUpdates(ll);
    }
}
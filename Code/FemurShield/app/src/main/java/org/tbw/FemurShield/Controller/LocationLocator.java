package org.tbw.FemurShield.Controller;

/**
 * Created by lucavianello on 20/05/15.
 */

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationLocator
{
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_on = false;
    boolean network_on = false;

    public boolean getLocation(Context context, LocationResult result)
    {
        locationResult = result;

        // inizializza il location manager
        if(lm==null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // lancio exeption se non disponibili
        try
        {
            gps_on=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex){}

        try
        {
            network_on=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex){}

        //ritorna false se non sono disponibili i providers
        if(!gps_on && !network_on)
            return false;

        // richiede l'inizio acquisizione a chi è attivo, con precedenza al GPS
        if(gps_on)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
        if(network_on)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);

        // utlizzando il Timer andorid, fai partire il task dopo 20 sec, tempo per la rilevazione della posizione
        timer1=new Timer();
        timer1.schedule(new GetLastLocation(), 20000);
        return true;
    }

    // Inizializzazione Listener GPS
    LocationListener locationListenerGPS = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGPS);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    // semplice Task che tramite un timer temporale legge la posizione
    class GetLastLocation extends TimerTask
    {
        @Override
        public void run()
        {
            // rimuove possibili vecchie letture di un'altra sessione passata
            lm.removeUpdates(locationListenerGPS);
            lm.removeUpdates(locationListenerNetwork);

            //prende la ultima locazione se i porvider sono attivi
            Location net_loc=null, gps_loc=null;
            if(gps_on)
                gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(network_on)
                net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            //se presenti tutti e due, sceglie quello più recente
            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            // altrimenti prende quello che c'è
            if(gps_loc!=null){
                locationResult.gotLocation(gps_loc);
                return;
            }
            if(net_loc!=null){
                locationResult.gotLocation(net_loc);
                return;
            }

            // per sicurezza null
            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult
    {
        public abstract void gotLocation(Location location);
    }
}

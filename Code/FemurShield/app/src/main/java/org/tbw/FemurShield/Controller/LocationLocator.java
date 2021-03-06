package org.tbw.FemurShield.Controller;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe di support per recuperare la posizione geografica
 *
 * @author Luca Vianello
 */

public class LocationLocator {
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_on = false;
    boolean network_on = false;

    /**
     * Ritorna true se è stato possibile recuperare la posizione
     *
     * @param context il context dell'applicazione
     * @param result  la variabile su cui salvare le posizione
     * @return true se si è riusciti a recuperare la posizione, false altrimenti
     */
    public boolean getLocation(Context context, LocationResult result) {
        locationResult = result;

        // inizializza il location manager
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Controllo se gps o network non disponibili
        try {
            gps_on = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_on = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //ritorna false se non sono disponibili i providers
        if (!gps_on && !network_on)
            return false;

        // richiede l'inizio acquisizione a chi è attivo, con precedenza al GPS
        if (gps_on)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
        if (network_on)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);

        // utlizzando il Timer andorid, fai partire il task dopo 20 sec (tempo utile per la rilevazione della posizione)
        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 20000);
        return true;
    }

    // Inizializzazione Listener GPS. quando rileva un cambiamento, prende il risultato fermando il timer e rilascia il listener
    LocationListener locationListenerGPS = new LocationListener() {
        /**
         * Metodo lanciato alla rilevazione della posizione
         * @param location la variabile su cui salvare la posizione
         */
        @Override
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    // Inizializzazione Listener Network. quando rileva un cambiamento, prende il risultato fermando il timer e rilascia il listener
    LocationListener locationListenerNetwork = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGPS);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    /**
     * Classe che si attiva dopo un tempo deciso da un timer
     */
    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            // rilascia i listener dopo i 20 secondi
            lm.removeUpdates(locationListenerGPS);
            lm.removeUpdates(locationListenerNetwork);

            //prende la ultima locazione rilevata se i porvider sono attivi
            Location net_loc = null, gps_loc = null;
            if (gps_on)
                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_on)
                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            //se presenti tutti e due, sceglie quello più recente
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            // altrimenti prende quello che c'è
            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc);
                return;
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc);
                return;
            }

            // per sicurezza null
            locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
}

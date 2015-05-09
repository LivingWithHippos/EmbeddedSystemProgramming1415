package org.tbw.FemurShield.Controller;

/**
 * Created by Vianello on 09/05/15. in Editing
 */

import org.tbw.FemurShield.Model.Fall;

public class MultiEmailSender {

    public MultiEmailSender(){
    }

    public void sendEmail(Fall caduta){

        // recupero i dati da inserire nella mail da Fall
        double[] fallPosition = caduta.getPosition();
        double lat = fallPosition[caduta.FALL_LATITUDE];
        double lon = fallPosition[caduta.FALL_LONGITUDE];
        int num = caduta.getId();

        // trasformo i valori in string

        String latitudine = "pippo";
        String longitudine = "pluto";
        String numero = "paperino";

        // imposta come segnalata la caduta
        caduta.setReported();
    }

}

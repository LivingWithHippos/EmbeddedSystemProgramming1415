package org.tbw.FemurShield.Controller;

/**
 * Created by Vianello on 09/05/15. in Editing
 */

import org.tbw.FemurShield.Model.Fall;

public class MultiEmailSender {

    public MultiEmailSender(){
    }

    public void sendEmail(Fall caduta){

        // recupera i dati da inserire nella mail da Fall
        double[] fallPosition = caduta.getPosition();
        double lat = fallPosition[caduta.FALL_LATITUDE];
        double lon = fallPosition[caduta.FALL_LONGITUDE];
        int num = caduta.getId();
        String nome = "nomeUtente"; // è il nome di chi cade, magari inserire una opzione nelle settings per impostarlo

        // trasforma i valori in string
        String latitudine = ""+lat;
        String longitudine = ""+lon;
        String numero = ""+num;

        // apre un template della mail e lo mette in una stringa per ediatrlo (da finire)
        String Mail = "NUMERO DATA LATITUDINE LONGITUDINE";

        // inserisco i valori nei campi del testo (cioè parole in maiuscolo del template)
        Mail.replace("NOME","nome");
        Mail.replace("NUMERO","numero");
        Mail.replace("DATA","data");
        Mail.replace("LATITUDINE","latitudine");
        Mail.replace("LONGITUDINE","longitudine");

        //compone la mail con destinatari, oggetto e testo

        //invia le mail

        // imposta come segnalata la caduta
        caduta.setReported();
    }

}

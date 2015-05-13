package org.tbw.FemurShield.Controller;

/**
 * Created by Vianello on 09/05/15. in Editing
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.tbw.FemurShield.Model.Fall;

public class MultiEmailSender extends Service {

    private String[] adresses;

    @Override
    public void onCreate() 
    {
        adresses = null; // recupera gli indirizzi per la mail TODO
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  // per far partire il service e avvia metodo sendEmail TODO
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void sendEmail(Fall caduta){

        // recupera i dati da inserire nella mail da Fall
        double[] fallPosition = caduta.getPosition();
        double lat = fallPosition[caduta.FALL_LATITUDE];
        double lon = fallPosition[caduta.FALL_LONGITUDE];
        int num = caduta.getId();
        String nome = "nomeUtente"; // è il nome di chi cade, magari inserire una opzione nelle settings per impostarlo

        // crea il testo mail
        String Testo = "avvenuta caduta di "+nome+", numero: "+num+",in latiudine: "+lat+",e in longitudine: "+lon+".";

        //compone la mail con oggetto, destinatari in CCn e testo
        Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
        email.putExtra(Intent.EXTRA_SUBJECT, "(noreply) - FREMUR SHIELD Notification");
        email.putExtra(Intent.EXTRA_BCC, adresses);
        email.putExtra(Intent.EXTRA_TEXT, Testo);
        email.setType("message/rfc822");

        //invia le mail

        // imposta come segnalata la caduta
        caduta.setReported();
    }


}

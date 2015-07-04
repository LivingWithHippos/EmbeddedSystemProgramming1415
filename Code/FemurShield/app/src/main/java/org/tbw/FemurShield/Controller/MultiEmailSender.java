package org.tbw.FemurShield.Controller;

/**
 * Created by Vianello on 09/05/15.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

// service per invaire le mail di notifica
public class MultiEmailSender extends Service {

    private String[] addresses;
    String da;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent!=null)
        {
            // prende i dati dall'intent far partire il service e avvia metodo sendEmail, sennò blocca il service
            double la = intent.getDoubleExtra("latCaduta", 0.0);
            double lo = intent.getDoubleExtra("lonCaduta", 0.0);
            int i = intent.getIntExtra("idCaduta", 0);
            da = intent.getStringExtra("dataCaduta");
            String path = intent.getStringExtra("appdirectory");
            getAdresses(path);

            sendEmail(la, lo, i, da);
            return Service.START_STICKY;
        }
        else
        {
            stopSelf();
            return Service.START_NOT_STICKY;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // metodo per recuperare gli indirizzi mail per la mail
    private void getAdresses(String path)
    {
        HashMap<String, String> mails = null;
        try
        {
            File file = new File(path, "emails.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            mails = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            if (mails != null)
            {
                addresses = new String[mails.size()];
                int i = 0;
                for (HashMap.Entry<String, String> entry : mails.entrySet())
                    addresses[i++] = entry.getKey();

            }
        }
        catch (Exception e)
        {
            Log.e("FemurShield", "Errore Di lettura email: " + e.getMessage());
        }
    }

    // metodo che compone la mail e richiede l'invio di una mail ad una applicazione che mandi mail
    public void sendEmail(double lat, double lon, int num, String data)
    {

        // altre stringhe della mail
        String link = "https://www.google.it/maps/?z=18&q=";

        // crea il testo mail
        String testo = "Avvenuta caduta di un tuo amico in data: " + data + "\n\nnumero caduta: " + num + "\nlatiudine: " + lat + "\nlongitudine: " + lon + "\nLink Google Maps: " + link + lat + "," + lon;

        // se ci sono destinatari
        if (addresses != null)
        {
            //compone la mail con oggetto, destinatari in CCn e testo
            Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
            email.putExtra(Intent.EXTRA_SUBJECT, "(noreply) - FemurShield Notification");
            email.putExtra(Intent.EXTRA_BCC, addresses);
            email.putExtra(Intent.EXTRA_TEXT, testo);
            email.setType("message/rfc822");

            //avvia la activty per l'invio della mail
            Intent sendEmail = Intent.createChooser(email, "Scegli un Client E-Mail :");
            sendEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(sendEmail);

            // imposta come segnalata la caduta
            riporta();
        }
        else
        {
            Log.e("MultiMailSender", "indirizzi mancanti");
        }

    }

    // metodo per ricercare la fall che stiamo di cui stiamo mandando la mail e segnarla come riportata
    private void riporta()
    {
        ArrayList<Fall> falls=SessionManager.getInstance().getActiveSession().getFalls();
        falls.get(falls.size()-1).setReported();
        Controller.getNotification().NotifyEmailSent();
    }

}

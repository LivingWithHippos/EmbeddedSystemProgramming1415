package org.tbw.FemurShield.Controller;

/**
 * Created by Vianello on 09/05/15. in Editing
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.tbw.FemurShield.Model.Fall;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class MultiEmailSender extends Service {

    private String[] addresses;

    @Override
    public void onCreate()
    {
        addresses = null; // recupera gli indirizzi per la mail TODO
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("MultiMailSender", "Service Mail Partito");
        // per far partire il service e avvia metodo sendEmail
        double la = intent.getDoubleExtra("latCaduta", 0.0);
        double lo = intent.getDoubleExtra("lonCaduta", 0.0);
        int i = intent.getIntExtra("idCaduta", 0);
        String path = intent.getStringExtra("appdirectory");
        getAdresses(path);
        sendEmail(la, lo, i);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void getAdresses(String path)
    {
        HashMap<String,String> mails=null;
        try {
            File file=new File(path,"emails.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            mails = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            if(mails!=null) {
                addresses=new String[mails.size()];
                int i=0;
                for (HashMap.Entry<String, String> entry:mails.entrySet())
                    addresses[i++]=entry.getKey();

            }
        }catch (Exception e){
            Log.e("FemurShield", "Errore Di lettura email: " + e.getMessage());
        }
    }

    public void sendEmail(double lat, double lon, int id)
    {

        // recupera i dati da inserire nella mail

        double Lat = lat;
        double Lon = lon;
        int num = id;
        String nome = "nomeUtente"; // è il nome di chi cade, magari inserire una opzione nelle settings per impostarlo

        // crea il testo mail
        String Testo = "avvenuta caduta di "+nome+", numero: "+num+",in latiudine: "+Lat+",e in longitudine: "+Lon+".";

        if (addresses != null)
        {
            //compone la mail con oggetto, destinatari in CCn e testo
            Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
            email.putExtra(Intent.EXTRA_SUBJECT, "(noreply) - FREMUR SHIELD Notification");
            email.putExtra(Intent.EXTRA_BCC, addresses);
            email.putExtra(Intent.EXTRA_TEXT, Testo);
            email.setType("message/rfc822");

            //avvia la activty per l'invio della mail
            startActivity(Intent.createChooser(email, "Scegli un Client E-Mail :"));
        }
        else
        {
            Log.e("MultiMailSender", "indirizzi mancanti");
        }

        // imposta come segnalata la caduta
        // caduta.setReported(); TODO riporta email inviata su fall, da fare sapendo dove sono salavate
    }


}

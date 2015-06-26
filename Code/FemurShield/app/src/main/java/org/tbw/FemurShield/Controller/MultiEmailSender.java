package org.tbw.FemurShield.Controller;

/**
 * Created by Vianello on 09/05/15.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiEmailSender extends Service {

    private String[] addresses;
    String da;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null) {
            Log.d("MultiMailSender", "Service Mail Partito");
            // per far partire il service e avvia metodo sendEmail
            double la = intent.getDoubleExtra("latCaduta", 0.0);
            double lo = intent.getDoubleExtra("lonCaduta", 0.0);
            int i = intent.getIntExtra("idCaduta", 0);
            da = intent.getStringExtra("dataCaduta");
            String path = intent.getStringExtra("appdirectory");
            getAdresses(path);
            sendEmail(la, lo, i, da);
            return Service.START_STICKY;
        }else{
            Log.e("MultiEmailSender","Intent nullo, impossibile recuiperare la caduta");
            stopSelf();
            return Service.START_NOT_STICKY;}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getAdresses(String path) {
        HashMap<String, String> mails = null;
        try {
            File file = new File(path, "emails.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            mails = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            if (mails != null) {
                addresses = new String[mails.size()];
                int i = 0;
                for (HashMap.Entry<String, String> entry : mails.entrySet())
                    addresses[i++] = entry.getKey();

            }
        } catch (Exception e) {
            Log.e("FemurShield", "Errore Di lettura email: " + e.getMessage());
        }
    }

    public void sendEmail(double la, double lo, int id, String da) {

        // recupera i dati da inserire nella mail

        double lat = la;
        double lon = lo;
        int num = id;
        String data = da;
        String nome = "nomeUtente"; // è il nome di chi cade, magari inserire una opzione nelle settings per impostarlo
        String link = "https://www.google.it/maps/?z=18&q=";

        // crea il testo mail
        String testo = "Avvenuta caduta di " + nome + " in data: " + data + "\n\nnumero caduta: " + num + "\nlatiudine: " + lat + "\nlongitudine: " + lon + "\nLink Google Maps: " + link + lat + "," + lon;

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

    // metodo per ricercare la fall che stiamo di cui stiamo mandando la mail
    private boolean riporta()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
        try
        {
            long thisfalltime = sdf.parse(da).getTime();
            ArrayList <Session> sess = SessionManager.getInstance().getAllSessions();
            if(sess != null)
            {
                for(Session s: sess)
                {
                    long sessiontime =  sdf.parse(s.getDataTime()).getTime();
                    if(sessiontime>thisfalltime)
                    {
                        continue;
                    }
                    else
                    {
                        ArrayList<Fall> falls = s.getFalls();
                        for(Fall f:falls)
                        {
                            if(da.equalsIgnoreCase(f.getData()))
                            {
                                f.setReported();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}

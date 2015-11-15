package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.Observer.Observable;
import org.tbw.FemurShield.Observer.Observer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Moro on 02/05/15.
 */
public class Controller implements Observer {

    private static Controller instance;
    private static final String TAG = "Controller";
    Chronometer crono;
    Activity ac = null;

    private Controller() {
        instance = this;
    }

    public static Controller getInstance() {
        //permette l'implementazione del design pattern singleton
        if (instance != null)
            return instance;
        new Controller();
        getNotification().addObserver(instance);//si mette in osservazione della notifica della caduta (Design Pattern Observer)
        return instance;
    }

    public Session CreateSession() {
        //creo la sessione attiva nel caso in cui non esiste già altrimenti ritorno quella esistente
        ActiveSession active = SessionManager.getInstance().getActiveSession();
        if (active == null)
            return SessionManager.getInstance().createNewActiveSession();
        else
            return active;

    }

    long durata = 0;

    public void StartSession(Activity a) {
        ac = a;
        //chiede al session manager di avviare la sessione
        SessionManager.getInstance().StartSession();
        //avvio il cronometro per il calcolo della durata
        if (crono == null) {
            crono = new Chronometer(a.getBaseContext());
        }
        crono.setBase(SystemClock.elapsedRealtime() + durata);
        crono.start();
        //avvio il servizio che permette di stoppare la sessione se la durata di questa supera la durata impostata
        Intent i1 = new Intent(a, SessionStopper.class);
        a.startService(i1);
        //avvio il servizio che controlla se si sta verificando una caduta
        Intent i2 = new Intent(a, FallDetector.class);
        a.startService(i2);
    }

    public void PauseSession(Activity a) {
        //chiede al session manager di mettere in pausa la sessione
        SessionManager.getInstance().PauseSession();
        //metto in pausa il cronometro perchè la sessione è in pausa
        durata = crono.getBase() - SystemClock.elapsedRealtime();
        SessionManager.getInstance().getActiveSession().setDuration(-durata);
        crono.stop();
        //interrompo il servizio che permette di stoppare la sessione se la durata di questa supera la durata impostata
        Intent i1 = new Intent(a, SessionStopper.class);
        a.stopService(i1);
        //interrompo il servizio che controlla se si sta verificando una caduta
        Intent i2 = new Intent(a, FallDetector.class);
        a.stopService(i2);
    }

    public long getDurate() {
        if (crono != null)
            return -crono.getBase() + SystemClock.elapsedRealtime();
        return 0;
    }

    public void StopSession(Activity a) {
        //fermo il cronometro
        durata = crono.getBase() - SystemClock.elapsedRealtime();
        //chiedo al session manager di mettere terminare la sessione attiva e ne imposto la durata nel modello
        SessionManager.getInstance().StopSession((-durata));
        //azzero la durata e fermo il cronometro
        durata = 0;
        crono.stop();
        crono = null;
        //interrompo il servizio che permette di stoppare la sessione se la durata di questa supera la durata impostata
        Intent i1 = new Intent(a, SessionStopper.class);
        a.stopService(i1);
        //interrompo il servizio che controlla se si sta verificando una caduta
        Intent i2 = new Intent(a, FallDetector.class);
        a.stopService(i2);
        //salvo nel backup i dati della nuova sessione
        SaveAll();
    }

    /**
     * Mi dice in real time la durata della sessione attiva
     */
    public long getActualChronoBase() {
        if (isRunning())
            return crono.getBase();
        return SystemClock.elapsedRealtime() + Controller.getInstance().durata;
    }

    /**
     * Dice se la sessione attiva non è in pausa
     */
    public boolean isRunning() {
        return SessionManager.getInstance().isRunning();
    }

    /**
     * Dice se esiste una sessione attiva
     */
    public boolean isRecording() {
        return SessionManager.getInstance().getActiveSession() != null;
    }

    /**
     * ritorna l'oggetto osservabile che notifica i dati dell'accelerometro, quando avviene una caduta e quando la caduta viene segnalata
     */
    public static NotificationFall getNotification() {
        return NotificationFallImpl.getInstance();
    }

    @Override
    public void update(Observable oggettoosservato, Object o) {
        //se mi arriva un aggiornamento di tipo caduta la aggiungo alla sessione attiva
        if (o instanceof Fall) {
            //aggiungo la faduta alla sessione
            SessionManager.getInstance().getActiveSession().AddFall((Fall) o);
        }
    }

    // creato da Vianello
    public void sendEmail(Fall fall) {
        //invio le mail sfruttando MultiEmailSender
        Intent sender = new Intent(ac.getApplicationContext(), MultiEmailSender.class);
        sender.putExtra("appdirectory", ac.getFilesDir().toString()); // passo la cartella in cui c'è il file con gli indirizzi salvati
        sender.putExtra("latCaduta", fall.getPosition(Fall.FALL_LATITUDE));
        sender.putExtra("lonCaduta", fall.getPosition(Fall.FALL_LONGITUDE));
        sender.putExtra("idCaduta", fall.getId());
        sender.putExtra("dataCaduta", fall.getData());
        //avvio il servizio
        ac.startService(sender);
    }

    /**
     * Tale metodo permette il salvataggio del modello in un file di backup
     */
    public void SaveAll() {
        File file = new File(ac.getFilesDir().toString(), "backup.dat");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(SessionManager.getInstance().createBackup());
            outputStream.flush();
            outputStream.close();
            //Log.d(TAG, "Salvataggio sessioni avvenuto con successo!");
        } catch (Exception e) {
            Log.e(TAG, "Errore salvataggio file sessioni: " + e.getMessage());
        }
    }

    /**
     * Tale metodo permette il ripristino del modello backup salvato
     */
    public void RestoreAll() {
        HashMap<String, String> backup = null;
        try {
            File file = new File(ac.getFilesDir().toString(), "backup.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            backup = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            SessionManager.getInstance().applyBackup(backup);
            //Log.d(TAG, "Backup:" + backup.toString());
        } catch (IOException e) {
            Log.e(TAG, "Backup non trovato: " + e.getMessage());
        } catch (ClassNotFoundException e) {
        }
    }

    /*
    tale metodo viene invocato alla prima appertura dell'app  e permette il ripristino dei dati del backup
     */
    public void firstOpenEvent(Activity mainUI) {
        ac = mainUI;
        RestoreAll();
    }

    /*
    tale metodo elimina una sessione quando una activity scatena/richiede l'eliminazione di questa
     */
    public void deleteEvent(String data) {
        SessionManager.getInstance().deleteOldSession(data);
        SaveAll();
    }

    /*
        tale metodo rinomina una sessione quando una activity scatena/richiede di campiare il nome di questa
     */
    public void renameEvent(String data, String newname) {
        SessionManager.getInstance().renameSession(data, newname);
    }

    /*
    tale metodo serve per interrompere una sessione per esempio perchè il tempo è scaduto. essa interrompe anche i vari servizi
     */
    void InterruptSession() {
        //termino la sessione e i servizi
        Controller.getInstance().StopSession(ac);
        //avvio la nuova ui
        Intent i = new Intent(ac.getBaseContext(), UI1.class);
        ac.startActivity(i);
        //avverto l'utetne che la sessione è scaduta
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ac.getBaseContext(), "La sessione è stata stoppata come impostato.", Toast.LENGTH_LONG).show();
            }
        });
    }
}

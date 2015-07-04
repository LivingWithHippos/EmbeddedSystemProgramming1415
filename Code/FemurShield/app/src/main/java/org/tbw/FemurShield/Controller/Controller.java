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
    private static final String TAG="Controller";
    Chronometer crono;
    Activity ac=null;

    private Controller(){
        instance = this;
    }

    public static Controller getInstance(){
        if(instance!=null)
            return instance;
        new Controller();
        getNotification().addObserver(instance);
        return instance;
    }

    public Session CreateSession(){
        ActiveSession active=SessionManager.getInstance().getActiveSession();
        if(active==null)
            return SessionManager.getInstance().createNewActiveSession();
        else
            return null; //TODO: sollevare errore magari;

    }
    long durata=0;
    public void StartSession(Activity a){
        ac=a;
        SessionManager.getInstance().StartSession();
        if(crono==null) {
            crono = new Chronometer(a.getBaseContext());
        }
        crono.setBase(SystemClock.elapsedRealtime() + durata);
        crono.start();

        Intent i1 = new Intent(a,SessionStopper.class);
        a.startService(i1);

        Intent i2 = new Intent(a,FallDetector.class);
        a.startService(i2);
    }

    public void PauseSession(Activity a){
        SessionManager.getInstance().PauseSession();
        durata=crono.getBase()-SystemClock.elapsedRealtime();
        SessionManager.getInstance().getActiveSession().setDuration(-durata);
        crono.stop();
        Log.d("INTERRUPt", "segnato");
        Intent i1 = new Intent(a,SessionStopper.class);
        a.stopService(i1);

        Intent i2 = new Intent(a,FallDetector.class);
        a.stopService(i2);
    }

    public long getDurate(){
        if(crono!=null)
            return -crono.getBase()+SystemClock.elapsedRealtime();
        return 0;
    }

    public void StopSession(Activity a){
        durata=crono.getBase()-SystemClock.elapsedRealtime();
        SessionManager.getInstance().StopSession((-durata));
        durata=0;
        crono.stop();
        crono =null;
        Intent i1 = new Intent(a,SessionStopper.class);
        a.stopService(i1);

        Intent i2 = new Intent(a,FallDetector.class);
        a.stopService(i2);
        SaveAll();
    }

    public long getActualChronoBase(){
        if(isRunning())
            return crono.getBase();
        return SystemClock.elapsedRealtime()+Controller.getInstance().durata;
    }

    public boolean isRunning(){
        return SessionManager.getInstance().isRunning();
    }

    public boolean isRecording(){
        return SessionManager.getInstance().getActiveSession()!=null;
    }

    public static NotificationFall getNotification(){
        return NotificationFallImpl.getInstance();
    }

    @Override
    public void update(Observable oggettoosservato, Object o) {
        if(o instanceof Fall){

            //aggiungo la faduta alla sessione
            SessionManager.getInstance().getActiveSession().AddFall((Fall) o);
        }
    }

    public void sendEmail(Fall fall) {
    //invio le mail sfruttando MultiEmailSender
        Intent sender = new Intent(ac.getApplicationContext(),MultiEmailSender.class);
        sender.putExtra("appdirectory", ac.getFilesDir().toString()); // passo la cartella in cui c'è il file con gli indirizzi salvati
        sender.putExtra("latCaduta", fall.getPosition(Fall.FALL_LATITUDE));
        sender.putExtra("lonCaduta", fall.getPosition(Fall.FALL_LONGITUDE));
        sender.putExtra("idCaduta", fall.getId());
        sender.putExtra("dataCaduta", fall.getData());
        ac.startService(sender);
    }

    /**
     * Tale metodo permette il salvataggio del modello in un xml
     */
    public void SaveAll(){
        File file = new File(ac.getFilesDir().toString(), "backup.dat");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(SessionManager.getInstance().createBackup());
            outputStream.flush();
            outputStream.close();
            Log.d(TAG, "Salvataggio sessioni avvenuto con successo!");
        } catch (Exception e) {
            Log.e(TAG, "Errore salvataggio file sessioni: " + e.getMessage());
        }
    }

    /**
     * Tale metodo permette il ripristino del modello dall'XML salvato
     */
    public void RestoreAll(){
        HashMap<String, String> backup = null;
        try {
            File file = new File(ac.getFilesDir().toString(), "backup.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            backup = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            SessionManager.getInstance().applyBackup(backup);
            Log.d(TAG, "Backup:" + backup.toString());
        } catch (IOException e) {
            Log.e(TAG, "Backup non trovato: " + e.getMessage());
        } catch (ClassNotFoundException e) {
        }
    }

    public void firstOpenEvent(Activity mainUI) {
        ac=mainUI;
        RestoreAll();
    }

    public void deleteEvent(String data) {
        SessionManager.getInstance().deleteOldSession(data);
        SaveAll();
    }

    public void renameEvent(String data, String newname) {
        SessionManager.getInstance().renameSession(data,newname);
    }

    void InterruptSession() {
        Controller.getInstance().StopSession(ac);
        Intent i = new Intent(ac.getBaseContext(),UI1.class);
        ac.startActivity(i);
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ac.getBaseContext(),"La sessione è stata stoppata come impostato.",Toast.LENGTH_LONG).show();
            }
        });
    }
}

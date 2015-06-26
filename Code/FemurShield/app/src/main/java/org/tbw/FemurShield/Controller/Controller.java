package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Chronometer;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.Observer.Observable;
import org.tbw.FemurShield.Observer.Observer;

/**
 * Created by Moro on 02/05/15.
 */
public class Controller implements Observer {

    private static Controller instance;
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
        crono.setBase(SystemClock.elapsedRealtime()+durata);
        crono.start();
        Intent i = new Intent(a,FallDetector.class);
        a.startService(i);
    }

    public void PauseSession(Activity a){
        SessionManager.getInstance().PauseSession();
        durata=crono.getBase()-SystemClock.elapsedRealtime();
        SessionManager.getInstance().getActiveSession().setDuration(-durata);
        crono.stop();
        Intent i=new Intent(a, FallDetector.class);
        a.stopService(i);
    }

    public void StopSession(Activity a){
        durata=crono.getBase()-SystemClock.elapsedRealtime();
        SessionManager.getInstance().StopSession((-durata));
        durata=0;
        crono.stop();
        crono =null;
        Intent i=new Intent(a, FallDetector.class);
        a.stopService(i);
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
            //invio le mail sfruttando MultiEmailSender
            Intent sender = new Intent(ac.getApplicationContext(),MultiEmailSender.class);
            sender.putExtra("appdirectory", ac.getFilesDir().toString()); // passo la cartella in cui c'è il file con gli indirizzi salvati
            sender.putExtra("latCaduta", ((Fall)o).getPosition(Fall.FALL_LATITUDE));
            sender.putExtra("lonCaduta", ((Fall)o).getPosition(Fall.FALL_LONGITUDE));
            sender.putExtra("idCaduta", ((Fall)o).getId());
            sender.putExtra("dataCaduta", ((Fall)o).getData());
            ac.startService(sender);
            //aggiungo la faduta alla sessione
            SessionManager.getInstance().getActiveSession().AddFall((Fall) o);
        }
    }
}

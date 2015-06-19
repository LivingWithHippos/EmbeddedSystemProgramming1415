package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Intent;

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

    private Controller(){
    }

    public static Controller getInstance(){
        if(instance!=null)
            return instance;
        instance=new Controller();
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

    public void StartSession(Activity a){
        SessionManager.getInstance().StartSession();
        Intent i = new Intent(a,FallDetector.class);
        a.startService(i);
    }

    public void PauseSession(Activity a){
        SessionManager.getInstance().PauseSession();
        Intent i=new Intent(a, FallDetector.class);
        a.stopService(i);
    }

    public void StopSession(Activity a){
        SessionManager.getInstance().StopSession();
        Intent i=new Intent(a, FallDetector.class);
        a.stopService(i);
    }

    public boolean isRunning(){
        return SessionManager.getInstance().isRunning();
    }

    public boolean isRecording(){
        return SessionManager.getInstance().getActiveSession()==null;
    }

    public static NotificationFall getNotification(){
        return NotificationFallImpl.getInstance();
    }

    @Override
    public void update(Observable oggettoosservato, Object o) {

        //TODO
        if(o instanceof Fall)
            SessionManager.getInstance().getActiveSession().AddFall((Fall) o);
    }

    public void SaveAll(){

    }

    public void RestoreAll(){

    }
}

package org.tbw.FemurShield.Controller;

import android.app.Application;
import android.content.Intent;

import java.util.Observable;
import java.util.Observer;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;

/**
 * Created by Moro on 02/05/15.
 */
abstract class Controller {

    protected static Session CreateSession(){
        ActiveSession active=SessionManager.getInstance().getActiveSession();
        if(active==null)
            return SessionManager.getInstance().createNewActiveSession();
        else
            return null; //TODO: sollevare errore magari;

    }

    protected static void StartSession(/*Context c*/){ //avere il context ci permette di avere il contesto da passare all'intent del service
        SessionManager.getInstance().StartSession();
    }

    protected static void PauseSession(){
        SessionManager.getInstance().PauseSession();
    }

    protected static void StopSession(){
        SessionManager.getInstance().StopSession();
    }

    protected static void handleFallEvent(Fall fall){
        SessionManager.getInstance().getActiveSession().AddFall(fall);
    }
}

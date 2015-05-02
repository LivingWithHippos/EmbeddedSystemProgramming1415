package Controller;

import java.util.Observable;
import java.util.Observer;

import Model.ActiveSession;
import Model.SessionManager;

/**
 * Created by Moro on 02/05/15.
 */
public class Controller implements Observer{
    SessionManager SM;
    FallDetector detector;

    @Override
    public void update(Observable observable, Object data) {

    }

    public Controller(){
        SM=SessionManager.getInstance();
    }

    public void CreateSession(){
        ActiveSession active=SM.getActiveSession();
        if(active==null)
            SM.createNewActiveSession();
        else
            return; //TODO: errore;
    }

    public void StartSession(){
        SM.StartSession();
    }

    public void PauseSession(){
        SM.PauseSession();
    }

    public void StopSession(){
        SM.StopSession();
    }

    public FallDetector getDetector(){
        return detector;
    }
}

package org.tbw.FemurShield.Model;

import java.util.ArrayList;

/**
 * Created by Moro on 24/04/15.
 */
public class SessionManager {
    private static SessionManager instance; //identifica l'istanza unica di questa classe (singleton)
    private ActiveSessionImpl sessioneattiva=null;
    private static ArrayList<OldSessionImpl> sessionivecchie=new ArrayList<>();

    /**
     * costruttore o getter di sessionManager essendo esso un singleton
     * @return
     */
    public static SessionManager getInstance(){
        if(instance==null)
            return instance=new SessionManager();
        return instance;
    }

    /**
     * private con lo scopo di nascondere il costruttore per costringere ad utilizzare getInstance() (singleton)
     */
    private SessionManager(){}

    /**
     * creazione e restituzione di una nuova sessione attiva. (grazie a tale metodo sarà sempre unica ... singleton)
     * @return se è già presente una sessione attiva non permette di crearne un'altra e ritorna null
     */
    public ActiveSession createNewActiveSession(){
        if(sessioneattiva!=null)
            return null;
        return sessioneattiva=new ActiveSessionImpl();
    }

    /**
     * metodo get ritorna semplicemente la sessione attiva
     * @return sessione attiva o null se non è stata creata
     */
    public ActiveSession getActiveSession(){
        return sessioneattiva;
    }

    /**
     * metodo get ritorna semplicemente un array list con tutte le sessioni terminate.
     * @return array di interfacce alle sessioni vecchie (potrebbe esser vuoto)
     */
    public ArrayList<OldSession> getOldSessions(){
        ArrayList<OldSession> ret=new ArrayList<>();
        for(int i=0; i<sessionivecchie.size();i++)
            ret.add(sessionivecchie.get(i));
        return ret;
    }

    //TODO: eccezioni se non sono state create le sessioni o se non possono esser eseguite tali metodi
    /**
     * avvia la sessione attiva
     * @return true se è stata avviata con successo, false se non esiste ancora una sessione
     */
    public boolean StartSession(){
        if(sessioneattiva==null)
            return false;
        return sessioneattiva.Start();
    }

    /**
     * mette in pausa la sessione attiva
     * @return true se è stata messa in pausa con successo, false se non esiste ancora una sessione
     */
    public boolean PauseSession(){
        if(sessioneattiva==null)
            return false;
        return sessioneattiva.Pause();
    }

    /**
     * termina la sessione attiva
     * @return true se è stata terminata con successo, false se non esiste ancora una sessione
     */
    public boolean StopSession(){
        if(sessioneattiva==null)
            return false;
        sessioneattiva.Stop(); //TODO: maggiore controllo magari
        sessionivecchie.add(new OldSessionImpl(sessioneattiva)); //sfrutto con polimorfismo il costruttore OldSessionImpl(SessionImpl o)
        sessioneattiva=null; //cancello la sessione attiva
        return true;
    }
}

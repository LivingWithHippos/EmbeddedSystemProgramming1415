package org.tbw.FemurShield.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Moro on 24/04/15.
 */
public class SessionManager {
    private static SessionManager instance; //identifica l'istanza unica di questa classe (singleton)
    private ActiveSessionImpl sessioneattiva=null;
    private static HashMap<String,OldSessionImpl> sessionivecchie=new HashMap<>();
    private boolean running=false;

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
    public HashMap<String , OldSession> getOldSessionsById(){
        HashMap<String,OldSession> ret=new HashMap<>();
        for(int i=0; i<sessionivecchie.size();i++)
            ret.put(sessionivecchie.get(i).getId(), sessionivecchie.get(i));
        return ret;
    }


    /**
     * metodo get ritorna semplicemente un array list con tutte le sessioni terminate.
     * @return array di interfacce alle sessioni vecchie (potrebbe esser vuoto)
     */
    public ArrayList<OldSession> getOldSessions(){
        ArrayList<OldSession> ret=new ArrayList<>();
        Iterator it = sessionivecchie.entrySet().iterator();
        while (it.hasNext()) {
            OldSession session = ((HashMap.Entry<String,OldSession>)it.next()).getValue();
            ret.add(session);
        }
        return ret;
    }


    /**
     * metodo get ritorna una particolare sessione.
     * @return array di interfacce alle sessioni vecchie (potrebbe esser vuoto)
     */
    public Session getSession(String id){
        if(sessioneattiva.getId().equals(id))
            return sessioneattiva;
        return sessionivecchie.get(id);
    }

    /**
     * metodo che ritorna tutte le sessioni, vecchie e attiva
     * @return array di interfacce a tutte le sessioni (potrebbe esser vuoto)
     * */
    public HashMap<String, Session> getAllSessionsById()
    {
        HashMap<String,Session> ret=new HashMap<>();
        if(sessionivecchie!=null)
            for(int i=0; i<sessionivecchie.size();i++)
                ret.put(sessionivecchie.get(i).getId(), sessionivecchie.get(i));
        if(sessioneattiva!=null)
            ret.put(sessioneattiva.getId(),sessioneattiva);
        return ret;
    }

    /**
     * metodo che ritorna tutte le sessioni, vecchie e attiva
     * @return array di interfacce a tutte le sessioni (potrebbe esser vuoto)
     * */
    public ArrayList<Session> getAllSessions()
    {
        ArrayList<Session> ret=new ArrayList<>();
        Iterator it = sessionivecchie.entrySet().iterator();
        while (it.hasNext()) {
            Session pair = ((HashMap.Entry<String,Session>)it.next()).getValue();
            ret.add(pair);
        }

        if(sessioneattiva!=null)
            ret.add(sessioneattiva);
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
        running=true;
        return sessioneattiva.Start();
    }

    /**
     * mette in pausa la sessione attiva
     * @return true se è stata messa in pausa con successo, false se non esiste ancora una sessione
     */
    public boolean PauseSession(){
        if(sessioneattiva==null)
            return false;
        running=false;
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
        sessionivecchie.put(sessioneattiva.getId(),new OldSessionImpl(sessioneattiva)); //sfrutto con polimorfismo il costruttore OldSessionImpl(SessionImpl o)
        sessioneattiva=null; //cancello la sessione attiva
        running=false;
        return true;
    }

    public boolean isRunning() {
        return running;
    }
}

package org.tbw.FemurShield.Model;

import org.tbw.FemurShield.Controller.Controller;

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
     * metodo per eliminare una sessione vecchia
     * @param id id della sessione da eliminare
     */
    public void deleteOldSession(String id){
        sessionivecchie.remove(id);
        SignatureImpl.deleteSignature(id);
    }

    /**
     * metodo per rinominare una sessione
     * @param id id della sessione da rinominare
     * @param newname nuovo nome della sessione
     * @return la sessione modificata o null se non l'ha trovata
     */
    public Session renameSession(String id,String newname)
    {
        Session temp=null;
        if(sessioneattiva!=null&&sessioneattiva.getId().equalsIgnoreCase(id)) {
            sessioneattiva.setName(newname);
            Controller.getInstance().SaveAll();
            return sessioneattiva;
        }
        else
        if((temp=sessionivecchie.get(id))!=null) {
            temp.setName(newname);
            Controller.getInstance().SaveAll();
            return temp;
        }
        return null;
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
        if(sessioneattiva!=null&&sessioneattiva.getId().equals(id))
            return sessioneattiva;
        return sessionivecchie.get(id);
    }

    /**
     * metodo che ritorna tutte le sessioni, vecchie e attiva
     * @return hashmap di interfacce a tutte le sessioni (potrebbe esser vuoto)
     * */
    public HashMap<String, Session> getAllSessionsById()
    {
        HashMap<String,Session> ret=new HashMap<>();
        if(sessionivecchie!=null){
            for(Session s:sessionivecchie.values())
                ret.put(s.getId(),s);
            }
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

    /**
     * avvia la sessione attiva
     * @return true se è stata avviata con successo, false se non esiste ancora una sessione
     */
    public boolean StartSession(){
        if(sessioneattiva==null)
            return false;
        running=true;
        return true;
    }

    /**
     * mette in pausa la sessione attiva
     * @return true se è stata messa in pausa con successo, false se non esiste ancora una sessione
     */
    public boolean PauseSession(){
        if(sessioneattiva==null)
            return false;
        running=false;
        return true;
    }

    /**
     * termina la sessione attiva
     * @return true se è stata terminata con successo, false se non esiste ancora una sessione
     */
    public boolean StopSession(long duration){
        if(sessioneattiva==null)
            return false;

        OldSessionImpl nuovavecchia=new OldSessionImpl(sessioneattiva);
        nuovavecchia.setDuration(duration);
        sessionivecchie.put(sessioneattiva.getId(),nuovavecchia); //sfrutto con polimorfismo il costruttore OldSessionImpl(SessionImpl o)
        sessioneattiva=null; //cancello la sessione attiva
        running=false;
        return true;
    }

    public boolean isRunning() {
        return running;
    }

    /*
    creo un hashmap coni valori da salvare delle sessioni in modo da poterle ripristinare
     */
    public HashMap<String,String> createBackup(){
        HashMap<String,String> backup= new HashMap<>();
        SessionManager sm=SessionManager.getInstance();
        int numsess=sm.getOldSessions().size();
        backup.put("numsess", "" + numsess);
        //salvo tutte le sessioni
        for(int i=0;i<numsess;i++){
            OldSession old=sm.getOldSessions().get(i);
            //salvo il nome della sessione i
            backup.put("nome" + i, old.getName());
            //salvo la data della sessione i
            backup.put("data" + i, old.getDataTime());
            //salvo la durata della sessione i
            backup.put("durata" + i, ""+old.getDuration());
            //salvo il path dell'immagine della firma della sessione i

            ArrayList<Fall> falls=old.getFalls();
            int numfall=falls.size();
            backup.put("numfall" + i, ""+numfall);
            //salvo tutte le cadute della sessione i
            for(int j=0;j<numfall;j++){
                Fall fall=falls.get(j);
                //salvo le coordinate della caduta j della sessione i
                backup.put("latitude" + i+"/"+j, ""+fall.getPosition(Fall.FALL_LATITUDE));
                backup.put("longitude" + i+"/"+j, ""+fall.getPosition(Fall.FALL_LONGITUDE));
                //salvo la data della caduta j della sessione i
                backup.put("data" + i + "/" + j, "" + fall.getData());
                //salvo se la caduta della caduta j della sessione i è stata segnalata o no
                backup.put("segnalato" + i+"/"+j, ""+fall.isReported());
                //salvo l'id caduta della caduta j della sessione i
                backup.put("id" + i+"/"+j, ""+fall.getId());
                //salvo i valori prima della caduta j della sessione i
                float[][] before=fall.getValuesBeforeFall();
                //creo la stringa dei valori delle x prima della caduta j della sessione i e la aggiungo al backup
                String beforedatax="";
                for(int k=0;k<before[Fall.X_INDEX].length;k++){
                    if(k==0)
                        beforedatax+=before[Fall.X_INDEX][k];
                    else
                        beforedatax+="#"+before[Fall.X_INDEX][k];
                }
                backup.put("xvaluebefore" + i+"/"+j, beforedatax);
                //creo la stringa dei valori delle y prima della caduta j della sessione i e la aggiungo al backup
                String beforedatay="";
                for(int k=0;k<before[Fall.Y_INDEX].length;k++){
                    if(k==0)
                        beforedatay+=before[Fall.Y_INDEX][k];
                    else
                        beforedatay+="#"+before[Fall.Y_INDEX][k];
                }
                backup.put("yvaluebefore" + i+"/"+j, beforedatay);
                //creo la stringa dei valori delle z prima della caduta j della sessione i e la aggiungo al backup
                String beforedataz="";
                for(int k=0;k<before[Fall.Z_INDEX].length;k++){
                    if(k==0)
                        beforedataz+=before[Fall.Z_INDEX][k];
                    else
                        beforedataz+="#"+before[Fall.Z_INDEX][k];
                }
                backup.put("zvaluebefore" + i+"/"+j, beforedataz);

                //salvo i valori durante la caduta j della sessione i
                float[][] during=fall.getFallValues();
                //creo la stringa dei valori delle x durante la caduta j della sessione i e la aggiungo al backup
                String duringdatax="";
                for(int k=0;k<during[Fall.X_INDEX].length;k++){
                    if(k==0)
                        duringdatax+=during[Fall.X_INDEX][k];
                    else
                        duringdatax+="#"+during[Fall.X_INDEX][k];
                }
                backup.put("xvalueduring" + i+"/"+j, duringdatax);
                //creo la stringa dei valori delle y durante della caduta j della sessione i e la aggiungo al backup
                String duringdatay="";
                for(int k=0;k<during[Fall.Y_INDEX].length;k++){
                    if(k==0)
                        duringdatay+=during[Fall.Y_INDEX][k];
                    else
                        duringdatay+="#"+during[Fall.Y_INDEX][k];
                }
                backup.put("yvalueduring" + i+"/"+j, duringdatay);
                //creo la stringa dei valori delle z durante della caduta j della sessione i e la aggiungo al backup
                String duringdataz="";
                for(int k=0;k<during[Fall.Z_INDEX].length;k++){
                    if(k==0)
                        duringdataz+=during[Fall.Z_INDEX][k];
                    else
                        duringdataz+="#"+during[Fall.Z_INDEX][k];
                }
                backup.put("zvalueduring" + i+"/"+j, duringdataz);

                //salvo i valori dopo la caduta j della sessione i
                float[][] after=fall.getValuesAfterFall();
                //creo la stringa dei valori delle x dopo della caduta j della sessione i e la aggiungo al backup
                String afterdatax="";
                for(int k=0;k<after[Fall.X_INDEX].length;k++){
                    if(k==0)
                        afterdatax+=after[Fall.X_INDEX][k];
                    else
                        afterdatax+="#"+after[Fall.X_INDEX][k];
                }
                backup.put("xvalueafter" + i+"/"+j, afterdatax);
                //creo la stringa dei valori delle y dopo della caduta j della sessione i e la aggiungo al backup
                String afterdatay="";
                for(int k=0;k<after[Fall.Y_INDEX].length;k++){
                    if(k==0)
                        afterdatay+=after[Fall.Y_INDEX][k];
                    else
                        afterdatay+="#"+after[Fall.Y_INDEX][k];
                }
                backup.put("yvalueafter" + i+"/"+j, afterdatay);
                //creo la stringa dei valori delle z dopo della caduta j della sessione i e la aggiungo al backup
                String afterdataz="";
                for(int k=0;k<after[Fall.Z_INDEX].length;k++){
                    if(k==0)
                        afterdataz+=after[Fall.Z_INDEX][k];
                    else
                        afterdataz+="#"+after[Fall.Z_INDEX][k];
                }
                backup.put("zvalueafter" + i+"/"+j, afterdataz);
            }
        }
        return backup;
    }

    public void applyBackup(HashMap<String, String> backup) {
        int numsess=Integer.parseInt(backup.get("numsess"));
        for(int i=0;i<numsess;i++){
            //ottengo i valori salvati della sessione i
            String nome=backup.get("nome"+i);
            String data = backup.get("data" +i);
            long durata = Long.parseLong(backup.get("durata" + i));
            ArrayList<Fall> falls=new ArrayList<>();
            int numfall=Integer.parseInt(backup.get("numfall" + i));
            //ottengo i valori di tutte le cadute della sessione i
            for(int j=0;j<numfall;j++){
                //ottengo i vari valori della caduta j della sessione i
                double lat=Double.parseDouble(backup.get("latitude" + i + "/" + j));
                double longi=Double.parseDouble(backup.get("longitude" + i + "/" + j));
                String datafall= backup.get("data" + i + "/" + j);
                int id= Integer.parseInt(backup.get("id" + i + "/" + j));
                boolean segnalato =Boolean.parseBoolean(backup.get("segnalato" + i + "/" + j));

                float[][] before=new float[3][];
                //recupero i valori delle x prima della caduta
                String[] xvalbeforestring= backup.get("xvaluebefore" + i+"/"+j).split("#");
                float[] xvalbefore=new float[xvalbeforestring.length];
                for(int k=0;k<xvalbeforestring.length;k++)
                    xvalbefore[k]=Float.parseFloat(xvalbeforestring[k]);
                before[Fall.X_INDEX]=xvalbefore;
                //recupero i valori delle y prima della caduta
                String[] yvalbeforestring= backup.get("yvaluebefore" + i+"/"+j).split("#");
                float[] yvalbefore=new float[yvalbeforestring.length];
                for(int k=0;k<yvalbeforestring.length;k++)
                    yvalbefore[k]=Float.parseFloat(yvalbeforestring[k]);
                before[Fall.Y_INDEX]=yvalbefore;
                //recupero i valori delle z prima della caduta
                String[] zvalbeforestring= backup.get("zvaluebefore" + i+"/"+j).split("#");
                float[] zvalbefore=new float[zvalbeforestring.length];
                for(int k=0;k<zvalbeforestring.length;k++)
                    zvalbefore[k]=Float.parseFloat(zvalbeforestring[k]);
                before[Fall.Z_INDEX]=zvalbefore;

                float[][] during=new float[3][];
                //recupero i valori delle x durante la caduta
                String[] xvalduringstring= backup.get("xvalueduring" + i+"/"+j).split("#");
                float[] xvalduring=new float[xvalduringstring.length];
                for(int k=0;k<xvalduringstring.length;k++)
                    xvalduring[k]=Float.parseFloat(xvalduringstring[k]);
                during[Fall.X_INDEX]=xvalduring;
                //recupero i valori delle y durante la caduta
                String[] yvalduringstring= backup.get("yvalueduring" + i+"/"+j).split("#");
                float[] yvalduring=new float[yvalduringstring.length];
                for(int k=0;k<yvalduringstring.length;k++)
                    yvalduring[k]=Float.parseFloat(yvalduringstring[k]);
                during[Fall.Y_INDEX]=yvalduring;
                //recupero i valori delle z durante la caduta
                String[] zvalduringstring= backup.get("zvalueduring" + i+"/"+j).split("#");
                float[] zvalduring=new float[zvalduringstring.length];
                for(int k=0;k<zvalduringstring.length;k++)
                    zvalduring[k]=Float.parseFloat(zvalduringstring[k]);
                during[Fall.Z_INDEX]=zvalduring;

                float[][] after=new float[3][];
                //recupero i valori delle x dopo la caduta
                String xvalafterTemp=backup.get("xvalueafter" + i+"/"+j);
                if(xvalafterTemp.length()>1) {
                String[] xvalafterstring= xvalafterTemp.split("#");

                    float[] xvalafter = new float[xvalafterstring.length];
                        for (int k = 0; k < xvalafterstring.length; k++)
                            xvalafter[k] = Float.parseFloat(xvalafterstring[k]);
                    after[Fall.X_INDEX] = xvalafter;
                }
                //recupero i valori delle y dopo la caduta
                String yvalafterTemp=backup.get("yvalueafter" + i+"/"+j);
                if(yvalafterTemp.length()>1) {
                    String[] yvalafterstring = yvalafterTemp.split("#");
                    float[] yvalafter = new float[yvalafterstring.length];
                    for (int k = 0; k < yvalafterstring.length; k++)
                        yvalafter[k] = Float.parseFloat(yvalafterstring[k]);
                    after[Fall.Y_INDEX] = yvalafter;
                }
                //recupero i valori delle z dopo la caduta
                String zvalafterTemp=backup.get("zvalueafter" + i + "/" + j);
                if(zvalafterTemp.length()>1) {
                    String[] zvalafterstring = backup.get("zvalueafter" + i + "/" + j).split("#");
                    float[] zvalafter = new float[zvalafterstring.length];
                    for (int k = 0; k < zvalafterstring.length; k++)
                        zvalafter[k] = Float.parseFloat(zvalafterstring[k]);
                    after[Fall.Z_INDEX] = zvalafter;
                }
                //aggiungo la caduta alle cadute
                if(xvalafterTemp.length()>1&&yvalafterTemp.length()>1&&zvalafterTemp.length()>1)
                    falls.add(new Fall(before, during, after, id, datafall, lat, longi, segnalato));
            }
            sessionivecchie.put(data, new OldSessionImpl(new SessionImpl(nome, data, falls, durata)));
        }
    }
}

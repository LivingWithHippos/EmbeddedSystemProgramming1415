package org.tbw.FemurShield.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Moro on 30/04/15.
 */
public class SessionImpl implements Session{
    protected ArrayList<Fall> cadute;
    protected long secondduration=0;
    protected String name;
    protected String datetime;
    private String ID;

    public SessionImpl(){
        //iniszializzo l' array delle cadute
        cadute=new ArrayList<>();
        //salvo la data attuale (di creazione)
        SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
        datetime = sdf.format(new Date());
        // assegno un nome di default
        name="Sessione "+datetime;
        //assegno l'id univoco
        ID=datetime;
    }

    /*
    tale costruttore serve per il ripristino della sessione
     */
    public SessionImpl(String nome,String datetime, ArrayList<Fall> falls,long duration){
        this.name=nome;
        this.datetime=datetime;
        this.cadute=falls;
        this.secondduration=duration;
        this.ID=datetime;
    }

    /*
    permette l'ottenimento del nome della sessione
     */
    @Override
    public String getName() {
        return name;
    }
    /*
    imposta il nome della sessione
     */
    @Override
    public void setName(String sessionname) { name=sessionname;}

    /*
    permette l'ottenimento della durata della sessione
     */
    @Override
    public long getDuration() {
        return secondduration;
    }

    /*
    imposta la durata della sessione
     */
    @Override
    public void setDuration(long duration) { secondduration=duration;}

    /*
    permette l'ottenimento dell'id della sessione
     */
    @Override
    public String getId() {
        return ID;
    }

    /*
    permette l'ottenimento della data di creazione della sessione
     */
    @Override
    public String getDataTime() {
        return datetime;
    }

    /*
    permette l'ottenimento della cadute della sessione
     */
    @Override
    public ArrayList<Fall> getFalls(){
        return cadute;
    }

    /*
    permette l'ottenimento del numero di cadute attuali della sessione
     */
    @Override
    public int getFallsNumber() {
        if(cadute!=null)
            return cadute.size();
        return 0;
    }
}

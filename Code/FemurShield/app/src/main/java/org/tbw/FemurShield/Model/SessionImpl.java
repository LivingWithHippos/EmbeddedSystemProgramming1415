package org.tbw.FemurShield.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

/**
 * Created by Moro on 30/04/15.
 */
public class SessionImpl extends Observable implements Session{
    protected ArrayList<Fall> cadute;
    protected long secondduration=0;
    protected String name;
    protected String datetime;
    private String ID;

    public SessionImpl(){
        cadute=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
        datetime = sdf.format(new Date());
        name="Sessione "+datetime;
        ID=datetime;
    }

    public SessionImpl(String nome,String datetime, ArrayList<Fall> falls,long duration){
        this.name=nome;
        this.datetime=datetime;
        this.cadute=falls;
        this.secondduration=duration;
        this.ID=datetime;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String sessionname) { name=sessionname;}

    @Override
    public long getDuration() {
        return secondduration;
    }

    @Override
    public void setDuration(long duration) { secondduration=duration;}

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDataTime() {
        return datetime;
    }

    @Override
    public ArrayList<Fall> getFalls(){
        return cadute;
    }

    @Override
    public int getFallsNumber() {
        if(cadute!=null)
            return cadute.size();
        return 0;
    }
}

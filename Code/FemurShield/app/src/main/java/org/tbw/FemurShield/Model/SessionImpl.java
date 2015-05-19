package org.tbw.FemurShield.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

/**
 * Created by Moro on 30/04/15.
 */
public class SessionImpl extends Observable implements Session{
    protected ArrayList<Fall> cadute;
    protected int secondduration=0;
    protected SignatureImpl signature;
    protected String name;
    protected String datetime;
    private String ID;

    public SessionImpl(){
        cadute=new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
        datetime = sdf.format(new Date());
        name="Sessione "+datetime;
        ID=datetime;
        signature=new SignatureImpl(datetime,SignatureImpl.PEAKED_CIRCLE);
    }

    @Override
    public Signature getSignature(){
        return signature;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String sessionname) { name=sessionname;}

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

package org.tbw.FemurShield.Model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Moro on 30/04/15.
 */
public class SessionImpl implements Session{
    protected ArrayList<Fall> cadute;
    protected int secondduration=0;
    protected SignatureImpl signature;
    protected String name;
    protected String datetime;

    public SessionImpl(){
        cadute=new ArrayList<>();
        signature=new SignatureImpl();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
        datetime = sdf.format(new Date());
        name="Sessione"+datetime;
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
    public String getDataTime() {
        return datetime;
    }

    @Override
    public ArrayList<Fall> getFalls(){
        return cadute;
    }
}

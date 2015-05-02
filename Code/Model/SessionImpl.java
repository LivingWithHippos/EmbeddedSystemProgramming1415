package Model;

import java.util.ArrayList;

/**
 * Created by Moro on 30/04/15.
 */
public class SessionImpl {
    protected ArrayList<Fall> cadute;
    protected int secondduration=0;
    protected SignatureImpl signature;

    public SessionImpl(){
        cadute=new ArrayList<>();
        signature=new SignatureImpl();
    }

    public Signature getSignature(){
        return signature;
    }

    public ArrayList<Fall> getFalls(){
        return cadute;
    }
}

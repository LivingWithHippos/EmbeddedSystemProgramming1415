package org.tbw.FemurShield.Model;

import java.util.ArrayList;

/**
 * Created by Moro on 03/05/15.
 */
public interface Session {
    public Signature getSignature();
    public String getName();
    public String getDataTime();
    public ArrayList<Fall> getFalls();
}

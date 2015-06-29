package org.tbw.FemurShield.Model;

import java.util.ArrayList;

/**
 * Created by Moro on 03/05/15.
 */
public interface Session {
    public static final String datePattern="dd/MM/yyyy_HH:mm:ss";
    public String getName();
    public String getDataTime();
    public ArrayList<Fall> getFalls();
    public int getFallsNumber();
    public void setName(String sessionname);
    public String getId();
    public long getDuration();
    public void setDuration(long duration);
}

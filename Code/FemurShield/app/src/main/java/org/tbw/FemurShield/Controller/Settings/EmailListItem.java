package org.tbw.FemurShield.Controller.Settings;

/**
 * Created by Marco on 08/06/2015.
 */
public class EmailListItem {
    public final String name;
    public String address;

    public EmailListItem(String address,String name)
    {
        this.name=name;
        this.address=address;
    }

    public void setAddress(String newAddress)
    {
        address=newAddress;
    }
}
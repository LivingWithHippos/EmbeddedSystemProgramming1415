package org.tbw.FemurShield.View;

/**
 * Created by Marco on 07/05/2015.
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

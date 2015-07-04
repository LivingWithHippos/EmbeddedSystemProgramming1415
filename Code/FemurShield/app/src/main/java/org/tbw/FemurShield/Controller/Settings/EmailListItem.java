package org.tbw.FemurShield.Controller.Settings;

/**
 * Semplice classe per contenere un oggetto della lista contatti
 *
 * @author Marco Biasin
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
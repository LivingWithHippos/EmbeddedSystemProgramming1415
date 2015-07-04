package org.tbw.FemurShield.Controller.Settings;

import android.graphics.drawable.Drawable;

/**
 * Semplice classe per contenere un oggetto della lista impostazioni
 *
 * @author Marco Biasin
 */
public class SettingListItem {
    public final Drawable icon;
    public final String title;
    public String description;

    /**
     * Costruttore della classe
     * @param icon l'icona dell'opzione
     * @param title il nome dell'opzione
     * @param description la descrizione dell'opzione
     */
    public SettingListItem(Drawable icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    /**
     * Modifica la descrizione dell'oggetto
     * @param newDescription la nuova descrizione
     */
    public void setDescription(String newDescription)
    {
        description=newDescription;
    }

}
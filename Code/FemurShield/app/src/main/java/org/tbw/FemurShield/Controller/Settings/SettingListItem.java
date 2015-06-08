package org.tbw.FemurShield.Controller.Settings;

import android.graphics.drawable.Drawable;

/**
 * Created by Marco on 08/06/2015.
 */
public class SettingListItem {
    public final Drawable icon;
    public final String title;
    public String description;

    public SettingListItem(Drawable icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    public void setDescription(String newDescription)
    {
        description=newDescription;
    }

}
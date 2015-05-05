package org.tbw.FemurShield;

import android.graphics.drawable.Drawable;

/**
 * Created by Marco on 04/05/2015.
 * classe che gestisce i singoli elementi della UI 5, vedi settings_list_item.xml
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

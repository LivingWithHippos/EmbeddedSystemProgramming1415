package org.tbw.FemurShield.Controller;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Marco on 07/06/2015.
 */
public class SessionReminderReceiver extends BroadcastReceiver {

    public final static int ID=1911466196;
    public final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

            //TODO : cosa fare quando parte l'allarme

        Toast.makeText(context,"L'allarme funziona!",Toast.LENGTH_LONG).show();

        Intent sesRemIntent=new Intent(context,SessionReminderActivity.class);
        sesRemIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sesRemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sesRemIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(sesRemIntent);

    }

    public static void enableReceiver(Context context)
    {
        //attiva il receiver
        ComponentName receiver = new ComponentName(context, SessionReminderReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void disableReceiver(Context context)
    {
        //disattiva il receiver
        ComponentName receiver = new ComponentName(context, SessionReminderReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }



}
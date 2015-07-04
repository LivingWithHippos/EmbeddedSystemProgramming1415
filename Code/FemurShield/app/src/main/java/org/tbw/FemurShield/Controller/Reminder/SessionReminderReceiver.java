package org.tbw.FemurShield.Controller.Reminder;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * BroadcastReceiver che riceve dall'AlarmManager il fatto che è ora di lancaire il reminder della sessione.
 * Lancia {@link SessionReminderActivity}.
 *
 * @author Marco Biasin
 */
public class SessionReminderReceiver extends BroadcastReceiver {

    public final static int ID=1911466196;
    public final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

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
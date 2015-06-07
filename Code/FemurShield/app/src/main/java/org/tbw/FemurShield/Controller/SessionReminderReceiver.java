package org.tbw.FemurShield.Controller;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Marco on 07/06/2015.
 */
public class SessionReminderReceiver extends BroadcastReceiver {

    private static PreferencesEditor prefs;
    public final static int ID=1911466196;
    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {

            //TODO : cosa fare quando parte l'allarme
        Log.d("SessionReminderReceiver","allarme preso");

        // Acquire wakelock
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "SessionReminderReceiver");
        }
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
        Toast.makeText(context,"L'allarme funziona!",Toast.LENGTH_LONG).show();

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
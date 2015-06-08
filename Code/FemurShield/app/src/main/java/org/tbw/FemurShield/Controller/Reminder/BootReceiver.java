package org.tbw.FemurShield.Controller.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.tbw.FemurShield.Controller.PreferencesEditor;

/**
 * Created by Marco on 07/06/2015.
 * Classe che reimposta l'allarme
 * ad ogni riavvio del dispositivo
 * (gli allarmi vengono cancellati
 * ad ogni riavvio)
 */
public class BootReceiver extends BroadcastReceiver {

    private static PreferencesEditor prefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            setAlarm(context);
        }
    }

    public static void setAlarm(Context context) {
        //imposto l'ora del nuovo allarme
        Intent alarmService = new Intent(context, ReminderService.class);
        alarmService.setAction(ReminderService.CREATE);
        context.startService(alarmService);
     }
}

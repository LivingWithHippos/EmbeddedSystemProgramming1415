package org.tbw.FemurShield.Controller.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.tbw.FemurShield.Controller.PreferencesEditor;

/**
 * @author Marco Biasin
 * Classe che reimposta l'allarme
 * ad ogni riavvio del dispositivo
 * (gli allarmi vengono cancellati
 * ad ogni riavvio)
 */
public class BootReceiver extends BroadcastReceiver {

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

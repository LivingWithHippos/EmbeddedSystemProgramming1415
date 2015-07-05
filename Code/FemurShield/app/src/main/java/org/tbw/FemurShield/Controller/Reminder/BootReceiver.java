package org.tbw.FemurShield.Controller.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 *
 * Classe che reimposta l'allarme
 * ad ogni riavvio del dispositivo
 * (gli allarmi vengono cancellati
 * ad ogni riavvio)
 *
 * @author Marco Biasin
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            setAlarm(context);
        }
    }

    /**
     * Lancia il servizio che si occupa di far partire il reminder all'orario prestabilito
     * @param context il context dell'applicazione
     */
    public static void setAlarm(Context context) {
        Intent alarmService = new Intent(context, ReminderService.class);
        alarmService.setAction(ReminderService.CREATE);
        context.startService(alarmService);
     }
}

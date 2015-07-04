package org.tbw.FemurShield.Controller.Reminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.tbw.FemurShield.Controller.PreferencesEditor;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Marco Biasin
 * Servizio che si occupa di far partire all'ora impostata il reminder per iniziare la sessione
 */
public class ReminderService extends IntentService {

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";
    public static final String TAG = "Reminder_Service";

    private IntentFilter matcher;

    public ReminderService() {
        super(TAG);
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        String notificationId = intent.getStringExtra("notificationId");

        if (matcher.matchAction(action)) {
            execute(action, notificationId);
        }
    }

    private void execute(String action, String notificationId) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SessionReminderReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, SessionReminderReceiver.ID, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //prendo l'ora ed il minuto per l'allarme
        PreferencesEditor prefs = new PreferencesEditor(this.getBaseContext());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, prefs.getAlarmHour());
        calendar.set(Calendar.MINUTE, prefs.getAlarmMinute());
        long triggerAtMillis=calendar.getTimeInMillis();
        /*controllo che oggi non sia già passato altrimenti Android lo lancia subito*/
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());
        if(calendar.get(Calendar.DAY_OF_WEEK)==nowCalendar.get(Calendar.DAY_OF_WEEK)&&
                calendar.get(Calendar.HOUR_OF_DAY)<=nowCalendar.get(Calendar.HOUR_OF_DAY)&&
                calendar.get(Calendar.MINUTE)<=nowCalendar.get(Calendar.MINUTE))
        {
            triggerAtMillis=triggerAtMillis+(24*60*60*1000);
        }

        if (CREATE.equals(action)) {
            // siccome non ci importa che parta prima o dopo usiamo inexact, puo' partire anche un minuto dopo
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis,AlarmManager.INTERVAL_DAY, pi);
        } else if (CANCEL.equals(action)) {
            am.cancel(pi);
        }

    }


}
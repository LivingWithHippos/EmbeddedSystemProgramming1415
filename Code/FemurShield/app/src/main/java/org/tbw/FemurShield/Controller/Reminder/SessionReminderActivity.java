package org.tbw.FemurShield.Controller.Reminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import org.tbw.FemurShield.Controller.UI1;
import org.tbw.FemurShield.R;

/**
 * Activity vuota che si occupa di fornire un background a {@link SessionReminderDialog}
 * e ad illuminare lo schermo se spento per attirare l'attenzione.
 *
 * @author Marco Biasin
 */
public class SessionReminderActivity extends Activity implements SessionReminderDialog.OnSessionStartingListener, SessionReminderDialog.OnDialogClosedListener {

    public final String TAG = this.getClass().getSimpleName();
    private PowerManager.WakeLock mWakeLock;
    //secondi prima di spegnere lo schermo
    private static final int WAKELOCK_TIMEOUT = 30 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity non visibile
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_session_reminder);

        if (findViewById(R.id.fragment_container_settings) != null) {
            if (savedInstanceState != null) {
                return;
            }

            SessionReminderDialog srFragment = new SessionReminderDialog();
            srFragment.show(getFragmentManager(), "SessionReminderDialog");
        }

        // Flag per tenere lo schermo acceso
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // Acquisisco wakelock
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }


    }

    //chiudo l'activity se l'utente preme indietro
    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    /**
     * Gestisco il caso in cui l'utente prema su inizia sessione o annulla
     *
     * @param wantToStart se l'utente ha premuto inizia o annulla
     */
    @Override
    public void onSessionStarted(boolean wantToStart) {
        if (wantToStart) {
            Intent intent = new Intent(this, UI1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else {
            finish();
        }
    }

    // se l'utente preme al di fuori del dialog chiudo l'activity
    @Override
    public void onDialogClosed() {
        finish();
    }
}

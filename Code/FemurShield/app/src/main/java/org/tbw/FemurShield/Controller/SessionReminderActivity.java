package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 07/06/2015.
 */
public class SessionReminderActivity extends Activity implements SessionReminderDialog.OnSessionStartingListener {

    public final String TAG = this.getClass().getSimpleName();
    private PowerManager.WakeLock mWakeLock;
    //secondi prima di spegnere lo schermo
    private static final int WAKELOCK_TIMEOUT = 30 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_reminder);

        if (findViewById(R.id.fragment_container_settings) != null) {


            if (savedInstanceState != null) {
                return;
            }

            SessionReminderDialog srFragment= new SessionReminderDialog ();
            srFragment.show(getFragmentManager(), "SessionReminderDialog");
        }

        // Set the window to keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // Acquire wakelock
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }


    }

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

    @Override
    public void onSessionStarted(boolean wantToStart) {
        if(wantToStart) {
            Intent intent = new Intent(this, UI1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }else{finish();}
    }
}

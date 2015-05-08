package org.tbw.FemurShield.Controller;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.tbw.FemurShield.Model.Fall;

import java.util.Observable;

/**
 * Created by Moro on 02/05/15.
 */
public class FallDetector extends Service{

 /*   @Override
    protected void onHandleIntent(Intent intent) {
        //TODO: implementation of the fall detection algorithm

        float[][] beforefall=null;//TODO assign
        float[][] falldata=null;//TODO assign
        float[][] afterfall=null;//TODO assign
        //notifico il controller che è stata rilevata una caduta e la gestirà.
        controller.handleFallEvent(new Fall(beforefall,falldata,afterfall));

    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        while(true){
            Log.println(Log.ERROR,"dfefwefwef","gwegegwegwegewgwegwegwe");
            try{Thread.currentThread().sleep(500);}catch(Exception e){}
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package org.tbw.FemurShield.Controller;

import android.app.IntentService;
import android.content.Intent;

import org.tbw.FemurShield.Model.Fall;

import java.util.Observable;

/**
 * Created by Moro on 02/05/15.
 */
public class FallDetector extends IntentService /*Observable*/ {
    Controller controller=null;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FallDetector(String name, Controller c) {
        super(name);
        controller=c;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //TODO: implementation of the fall detection algorithm

        //notifico il controller che è stata rilevata una caduta e la gestirà.

        float[][] beforefall=null;//TODO assign
        float[][] falldata=null;//TODO assign
        float[][] afterfall=null;//TODO assign
        controller.handleFallEvent(new Fall(beforefall,falldata,afterfall));
    }
}

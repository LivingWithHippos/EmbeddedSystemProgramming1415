package org.tbw.FemurShield.Controller;

import android.util.Log;

import org.tbw.FemurShield.Model.Fall;

/**
 * Created by Moro on 19/05/15.
 */
class NotificationFallImpl extends org.tbw.FemurShield.Observer.Observable implements NotificationFall {
    private static NotificationFallImpl instance;

    private NotificationFallImpl(){

    }

    public static NotificationFallImpl getInstance(){
        if(instance!=null)
            return instance;
        return instance=new NotificationFallImpl();
    }

    @Override
    public void addObserver(org.tbw.FemurShield.Observer.Observer o) {
        this.attach(o);
    }

    public void NotifyAccData(float x, float y, float z){
        this.notifyObserver(new float[]{x, y, z});
    }

    public void NotifyFall(Fall f) {
        this.notifyObserver(f);
    }
}

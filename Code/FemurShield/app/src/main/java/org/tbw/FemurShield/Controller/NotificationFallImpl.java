package org.tbw.FemurShield.Controller;

import android.util.Log;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Observer.Observer;

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

    @Override
    public void deattach(org.tbw.FemurShield.Observer.Observer o) {
        super.deattach(o);
    }

    public void NotifyAccData(float x, float y, float z){
        float[] args= new float[3];
        args[0]=x;
        args[1]=y;
        args[2]=z;
        this.notifyObserver(args);
    }

    public void NotifyFall(Fall f) {
        this.notifyObserver(f);
    }
}

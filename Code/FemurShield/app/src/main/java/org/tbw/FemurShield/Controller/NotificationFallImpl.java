package org.tbw.FemurShield.Controller;

import org.tbw.FemurShield.Model.Fall;

/**
 * Created by Moro on 19/05/15.
 */
class NotificationFallImpl extends org.tbw.FemurShield.Observer.Observable implements NotificationFall {
    private static NotificationFallImpl instance;

    private NotificationFallImpl(){//nascondo il costruttore

    }

    public static NotificationFallImpl getInstance(){//implemento il DP singleton
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

    public void NotifyAccData(float x, float y, float z){ //emetto la notifica agli osservatori riguardo i dati dell'accelerometro
        float[] args= new float[3];
        args[0]=x;
        args[1]=y;
        args[2]=z;
        this.notifyObserver(args);
    }

    public void NotifyFall(Fall f) {//eetto la notifica agli osservatori riguardo i dati della caduta
        this.notifyObserver(f);
    }

    @Override
    public void NotifyEmailSent() {//emetto la notifica agli osservatori riguardo i dati della notifica di avvenuta segnalazione della caduta
        this.notifyObserver(new EmailSentSegnalation());
    }
}

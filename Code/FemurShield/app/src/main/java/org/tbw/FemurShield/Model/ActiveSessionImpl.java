package org.tbw.FemurShield.Model;

/**
 * Created by Moro on 24/04/15.
 */
class ActiveSessionImpl extends SessionImpl implements ActiveSession{
    private boolean activestate; //identifica lo stato di "attività" della sessione;

    protected boolean Start() {//TODO
        return false;
    }

    protected boolean Stop() {//TODO
        return false;
    }

    protected boolean Pause() {//TODO
        return false;
    }

    @Override
    public boolean AddFall(Fall fall) {
        //TODO
        return false;
    }
}

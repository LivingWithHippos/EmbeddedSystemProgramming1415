package org.tbw.FemurShield.Model;

/**
 * Created by Moro on 24/04/15.
 */
class ActiveSessionImpl extends SessionImpl implements ActiveSession{

    public ActiveSessionImpl(){
        super();
    }

    //TODO: dovranno modificare la durata della sessione ... fare differenza ora attuale ora partenza sessione...e modificare activestate
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

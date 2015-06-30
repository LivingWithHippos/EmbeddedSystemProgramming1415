package org.tbw.FemurShield.Model;

/**
 * Created by Moro on 24/04/15.
 */
class ActiveSessionImpl extends SessionImpl implements ActiveSession{

    public ActiveSessionImpl(){
        super();
        SignatureImpl si=SignatureImpl.getInstance(getDataTime());
        si.startDrawing();
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
        cadute.add(fall);
        super.notifyObservers();
        return false;
    }

}

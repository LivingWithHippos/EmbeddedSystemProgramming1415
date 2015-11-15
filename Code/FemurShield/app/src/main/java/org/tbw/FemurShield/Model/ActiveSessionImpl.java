package org.tbw.FemurShield.Model;

/**
 * Created by Moro on 24/04/15.
 */
class ActiveSessionImpl extends SessionImpl implements ActiveSession {

    public ActiveSessionImpl() {
        super();
        SignatureImpl si = SignatureImpl.getInstance(getDataTime());
        si.startDrawing();
    }

    @Override
    public boolean AddFall(Fall fall) {
        if (fall != null) {
            //aggiungo la caduta alla sessione
            cadute.add(fall);
            return true;
        } else
            return false;
    }

}

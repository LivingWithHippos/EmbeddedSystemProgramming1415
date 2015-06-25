package org.tbw.FemurShield.Model;

/**
 * Created by Moro on 24/04/15.
 */
class OldSessionImpl extends SessionImpl implements OldSession {

    public OldSessionImpl(SessionImpl o){
        super(o.name,o.datetime,o.cadute,o.secondduration,o.signature);
    }
}

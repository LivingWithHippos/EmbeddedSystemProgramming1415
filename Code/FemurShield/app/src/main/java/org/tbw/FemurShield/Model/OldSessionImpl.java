package org.tbw.FemurShield.Model;

/**
 * Created by Moro on 24/04/15.
 */
class OldSessionImpl extends SessionImpl implements OldSession {
    //costruttore di una vecchia sessione (una vecchia sessione è una ex sessione attiva con tipo OldSessionImpl)
    public OldSessionImpl(SessionImpl o) {
        super(o.name, o.datetime, o.cadute, o.secondduration);
    }
}

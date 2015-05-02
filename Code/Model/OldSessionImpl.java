package Model;

/**
 * Created by Moro on 24/04/15.
 */
class OldSessionImpl extends SessionImpl implements OldSession {

    public OldSessionImpl(SessionImpl o){
        //TODO: controllare se si può fare estrazione del sottoggetto e assegnazione di esso con i principi della PAO... non ricordo più
        super.cadute=o.cadute;
        super.secondduration=o.secondduration;
        super.signature=o.signature;
        //così è pericoloso perchè se aggiungo a SessionImpl un campo rischiodi non inizializzarlo...in C++ si fa nella lista di inizializzazione : SessionImpl(o)
        //TODO: end
    }
}

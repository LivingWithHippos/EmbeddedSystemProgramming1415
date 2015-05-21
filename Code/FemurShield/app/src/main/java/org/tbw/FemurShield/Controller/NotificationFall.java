package org.tbw.FemurShield.Controller;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Observer.Observer;


/**
 * Created by Moro on 19/05/15.
 */
public interface NotificationFall {
    void addObserver(Observer o );
    void NotifyAccData(float x, float y, float z);
    void NotifyFall(Fall f);
}

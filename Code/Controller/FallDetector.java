package Controller;

import java.util.Observable;

/**
 * Created by Moro on 02/05/15.
 */
public class FallDetector extends Observable {


    //quando viene rilevato dall'algoritmo basterà fare notifyObservers(new Fall(args);)


    @Override
    public void notifyObservers(Object data) {
        super.notifyObservers(data);
    }
}

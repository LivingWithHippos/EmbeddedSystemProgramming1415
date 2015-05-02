package View;

import android.app.Activity;

import java.util.Observable;
import java.util.Observer;

import Controller.Controller;

/**
 * Created by Moro on 02/05/15.
 */
public class ActivityObserver extends Activity implements Observer{
    static protected Controller controller;

    static{ // viene eseguito la prima volta al caricamento della classe e basta
        controller = new Controller(); //tutte le Activity che specializzano tale classe hanno il riferimento al controller
    }

    @Override
    public void update(Observable observable, Object data) {
        //implementare l'aggiornamnto....controllare anche i tipi di data e observable
    }
}

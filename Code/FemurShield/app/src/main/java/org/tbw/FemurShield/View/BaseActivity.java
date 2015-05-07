package org.tbw.FemurShield.View;

import android.app.Activity;

import org.tbw.FemurShield.Controller.Controller;

/**
 * Created by Moro on 02/05/15.
 */
public class BaseActivity extends Activity{
    static protected Controller controller;

    static{ // viene eseguito la prima volta al caricamento della classe e basta
        controller = new Controller(); //tutte le Activity che specializzano tale classe hanno il riferimento al controller
    }
}

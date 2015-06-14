package org.tbw.FemurShield.Controller;

import java.util.Random;

/**
 * Created by Marco on 14/06/2015.
 */
public class ColorsPicker {
    private static ColorsPicker instance = null;
    private static String[][] palettes;
    private ColorsPicker(String[][] palettes) {
        this.palettes=palettes;

    }

    public static ColorsPicker getInstance(String[][] palettes) {
        if(instance == null) {
            instance = new ColorsPicker(palettes);
        }
        return instance;
    }

    public static ColorsPicker getInstance() {
        //trick, se e' nulla deve ritornare null per permettere di controllare
        return instance;
    }


    public static String[] pickRandomColors()
    {
        //TODO: tenere aggiornati i cambiamenti
        Random random=new Random();
        int i=random.nextInt(5);
        return palettes[i];
    }
}

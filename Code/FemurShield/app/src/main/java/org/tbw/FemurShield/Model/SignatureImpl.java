package org.tbw.FemurShield.Model;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Moro on 30/04/15.
 */
public class SignatureImpl implements Signature{
    //TODO:Campi privati
    protected Calendar calendar;
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");

    public SignatureImpl(String date){//TODO: creazione immagine

        try {
            calendar=Calendar.getInstance();
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {e.printStackTrace();}
    }

    @Override
    public Bitmap toBitmap() {
        //TODO;
        return null;
    }
}

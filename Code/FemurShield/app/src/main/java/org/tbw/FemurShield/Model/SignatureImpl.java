package org.tbw.FemurShield.Model;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Moro on 30/04/15.
 */
public class SignatureImpl implements Signature{
    //TODO:Campi privati
    protected Session session;
    protected Calendar calendar;
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");

    public SignatureImpl(Session session){//TODO: creazione immagine
        this.session=session;

        try {
            calendar=Calendar.getInstance();
            calendar.setTime(sdf.parse(session.getDataTime()));
        } catch (ParseException e) {e.printStackTrace();}
    }

    @Override
    public Bitmap toBitmap() {
        //TODO;
        return null;
    }
}

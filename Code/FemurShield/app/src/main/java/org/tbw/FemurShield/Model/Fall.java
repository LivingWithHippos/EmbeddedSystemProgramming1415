package org.tbw.FemurShield.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Moro on 30/04/15.
 * Edited by Vianello
 */
public class Fall {
    private static int i = 0;
    private int id;
    public static final int FALL_LATITUDE = 0;//rappresenta la posizione della latitudine nell'array della posizione di una caduta
    public static final int FALL_LONGITUDE = 1;//rappresenta la posizione della longitudine nell'array della posizione di una caduta
    public static final int X_INDEX = 0;//rappresenta la posizione dei valori delle x nell'array dei valori di una caduta
    public static final int Y_INDEX = 1;//rappresenta la posizione dei valori delle y nell'array dei valori di una caduta
    public static final int Z_INDEX = 2;//rappresenta la posizione dei valori delle z nell'array dei valori di una caduta
    private double[] position = new double[2];//rappresenta l'array della posizione della caduta
    private String data;//rappresenta la data nella quale è avvenuta la caduta
    private boolean segnalato = false;//indica se la caduta è stata segnalata via email o no
    private float[][] valuesBeforeFall = new float[3][]; //rappresenta i valori prima della caduta (500ms di dati)
    private float[][] valuesFall = new float[3][];//rappresenta i valori della caduta
    private float[][] valuesAfterFall = new float[3][];//rappresenta i valori dopo la caduta (500ms di dati)

    public Fall(float[][] beforeValues, float[][] FallValues, float[][] afterValues) {
        id = i++;
        valuesBeforeFall = beforeValues;
        valuesFall = FallValues;
        valuesAfterFall = afterValues;
        //imposto la data attuale per la caduta (avenuta in contemporanea con la creazione)
        SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
        data = sdf.format(new Date());
    }

    /*
    costruttore necessrio per il ripristione della caduta
     */
    Fall(float[][] beforeValues, float[][] FallValues, float[][] afterValues, int id, String data, double lat, double longi, boolean segnalato) {
        this.valuesAfterFall = afterValues;
        this.valuesFall = FallValues;
        this.valuesBeforeFall = beforeValues;
        this.id = id;
        this.data = data;
        this.position[this.FALL_LATITUDE] = lat;
        this.position[this.FALL_LONGITUDE] = longi;
        this.segnalato = segnalato;
    }

    /*
    permette l'ottenimento della posizione della caduta
     */
    public double[] getPosition() {
        return position;
    }

    /*
    permette l'ottenimento della latidudine o longitudine della caduta
     */
    public double getPosition(int pos) {
        return position[pos];
    }

    /*
    permette di sapere se la caduta è stata notificata via email
     */
    public boolean isReported() {
        return segnalato;
    }

    /*
    permete di salvare nella caduta che la caduta è stata segnalata
     */
    public void setReported() {
        segnalato = true;
    }

    /*
    permette l'ottenimento dei valori precedenti alla caduta (500ms)
     */
    public float[][] getValuesBeforeFall() {
        return valuesBeforeFall;
    }

    /*
        permette l'ottenimento dei valori successivi alla caduta (500ms)
         */
    public float[][] getValuesAfterFall() {
        return valuesAfterFall;
    }

    /*
        permette l'ottenimento dei valori della caduta (500ms)
         */
    public float[][] getFallValues() {
        return valuesFall;
    }

    /*
    permette l'ottenimento dell'id della caduta
     */
    public int getId() {
        return id;
    }

    /*
    permette l'ottenimetno della data della caduta
     */
    public String getData() {
        return data;
    }

    /*
    permette di impostare le posizioni della caduta
     */
    public void setPosition(double latitude, double longitude) {
        this.position[this.FALL_LATITUDE] = latitude;
        this.position[this.FALL_LONGITUDE] = longitude;
    }
}
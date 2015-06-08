package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marco on 12/05/2015.
 * Classe che gestisce il salvataggio ed il caricamento delle impostazioni in tutta l'applicazione
 */
public class PreferencesEditor {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String appPath;
    private Activity activity;
    private Context context;

    public PreferencesEditor(Activity a)
    {
        appPath=a.getFilesDir().toString();
        preferences= PreferenceManager.getDefaultSharedPreferences(a);
        editor=preferences.edit();
    }

    public PreferencesEditor(Context c)
    {
        appPath=c.getFilesDir().toString();
        preferences= PreferenceManager.getDefaultSharedPreferences(c);
        editor=preferences.edit();
    }


    /**
     * @return l'ora a cui e' stato impostato l'allarme o il valore di defualt 9
     */
    public int getAlarmHour()
    {
        return preferences.getInt("alarm_hour", 9);
    }

    /**
     * @param newHour l'ora in cui far partire l'allarme giornaliero
     */
    public void setAlarmHour(int newHour)
    {
        editor.putInt("alarm_hour",newHour);
        editor.commit();
    }
    /**
     * @return il minuto a cui e' stato impostato l'allarme o il valore di defualt 00
     */
    public int getAlarmMinute() { return preferences.getInt("alarm_minute",0); }

    /**
     * @param newMinute il minuto in cui far partire l'allarme giornaliero
     */
    public void setAlarmMinute(int newMinute)
    {
        editor.putInt("alarm_minute",newMinute);
        editor.commit();
    }
    /**
     * @return la durata massima della sessione o il valore di default, 12
     */
    public int getSessionDuration()
    {
        return preferences.getInt("session_duration",12);
    }

    /**
     * @param newDuration la durata massima della sessione
     */
    public void setSessionDuration(int newDuration)
    {
        editor.putInt("session_duration",newDuration);
        editor.commit();
    }

    /**
     * @return la percentuale di sampling rete deciso per i sensori
     */
    public int getSamplingRate()
    {
        return preferences.getInt("sample_rate",50);
    }

    /**
     * @param newRate la percentuale di sampling rate con cui utilizzare il sensore
     */
    public void setSamplingRate(int newRate)
    {
        editor.putInt("sample_rate",newRate);
        editor.commit();
    }

    /**
     * @return il numero di contatti inseriti o il valore di default 0
     */
    public int getEmailContactsNumber()
    {
        return preferences.getInt("email_contacts",0);
    }

    /**
     * Usare il metodo addOneContactNumber per aggiungere un contatto solo
     * @param newNumber il nuovo numero di contatti inseriti
     */
    public void setEmailContactsNumber(int newNumber)
    {
        editor.putInt("email_contacts",newNumber);
        editor.commit();
    }

    /**
     * Aggiunge uno al contatore di contatti
     */
    public void addOneContactNumber()
    {
        editor.putInt("email_contacts",getEmailContactsNumber()+1);
        editor.commit();
    }

    /**
     * Vedi metodo getEmailAddresses() se solo gli indirizzi sono di interesse
     * @return un hashmap con nome ed indirizzo email di tutti i contatti, l'email e' la chiave
     */
    public HashMap<String,String> getEmail()
    {
        HashMap<String,String> mails=null;
        try {
            File file=new File(appPath,"emails.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            mails = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.e("FemurShield","Errore Di lettura email: "+e.getMessage());
        }

        return mails;
    }

    /**
     * @return un array di stringhe che contiene esclusivamente gli indirizzi dei contatti
     */
    public String[] getEmailAddresses()
    {
        HashMap<String,String> mails=null;
        String[] addresses=null;
        try {
            File file=new File(appPath,"emails.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            mails = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            if(mails!=null) {
                addresses=new String[mails.size()];
                int i=0;
                for (HashMap.Entry<String, String> entry:mails.entrySet())
                    addresses[i++]=entry.getKey();

            }
        }catch (Exception e){
            Log.e("FemurShield","Errore Di lettura email: "+e.getMessage());
        }

        return addresses;
    }

    /**
     * elimina il file contenente i contatti
     * @return true se il file e' stato cancellato, false altrimenti
     */
    public boolean cleanEmailFile()
    {
        boolean isDeleted=false;
        try{

            File file=new File(appPath,"emails.dat");
            isDeleted=file.delete();
            setEmailContactsNumber(0);
        }catch (Exception e){
            Log.e("FemurShield","Errore Di cancellazione email: "+e.getMessage());}
        return isDeleted;
    }

    /**
     *
     * @param nome il nome del contatto da aggiungere
     * @param indirizzo l'indirizzo del contatto da aggiungere
     * @return true se il contatto e' stato inserito correttamente, false altrimenti
     */
    public boolean addEmail(String nome,String indirizzo)
    {
        HashMap<String,String> oldEmail=getEmail();
        HashMap<String,String> newEmail;
        boolean result=true;
        if(oldEmail!=null)
        {
            newEmail=new HashMap<>(oldEmail.size()+1);
            newEmail.putAll(oldEmail);
        }
        else
            newEmail=new HashMap<>(1);


        newEmail.put(indirizzo,nome);
        File file=new File(appPath,"emails.dat");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(newEmail);
            outputStream.flush();
            outputStream.close();
            Log.d("FemurShield","File email Scritto correttamente");
        }catch (Exception e){Log.e("FemurShield","Errore aggiunta contatto email : "+e.getMessage());result=false;}
        return result;
    }



}
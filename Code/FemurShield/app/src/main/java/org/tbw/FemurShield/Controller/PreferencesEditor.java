package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
 */
public class PreferencesEditor {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Activity activity;

    public PreferencesEditor(Activity a)
    {activity=a;}

    public int getAlarmHour()
    {
        preferences= activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getInt("alarm_hour", 9);
    }
    public int getAlarmMinute()
    {
        preferences= activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getInt("alarm_minute",0);
    }
    public int getSessionDuration()
    {
        preferences= activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getInt("session_duration",12);
    }
    public int getSamplingRate()
    {
        preferences= activity.getPreferences(Context.MODE_PRIVATE);
        return preferences.getInt("sample_rate",50);
    }
    public HashMap<String,String> getEmail()
    {
        HashMap<String,String> mails=null;
        try {
            File file=new File(activity.getFilesDir(),"emails.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            mails = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.e("FemurShield","Errore Di lettura email: "+e.getMessage());
        }

        return mails;
    }
    public String[] getEmailAddresses()
    {
        HashMap<String,String> mails=null;
        String[] addresses=null;
        try {
            File file=new File(activity.getFilesDir(),"emails.dat");
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



    public boolean cleanEmailFile()
    {
        try{

            File file=new File(activity.getFilesDir(),"emails.dat");
            return file.delete();
        }catch (Exception e){
            Log.e("FemurShield","Errore Di cancellazione email: "+e.getMessage());}
        return false;
    }

    //ritorna true se viene salvata correttamente
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
        File file=new File(activity.getFilesDir(),"emails.dat");
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
package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.OldSession;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void onDestroy() {
        super.onDestroy();
        SaveAll();
    }

    /**
     * Tale metodo permette il salvataggio del modello in un xml
     */
    public void SaveAll(){
        File file = new File(getFilesDir().toString(), "backup.dat");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(createBackup());
            outputStream.flush();
            outputStream.close();
            Log.d("FemurShield", "Salvataggio avvenuto con successo!");
        } catch (Exception e) {
            Log.e("FemurShield", "Errore salvataggio file : " + e.getMessage());
        }
    }

    public HashMap<String,String> createBackup(){
        HashMap<String,String> backup= new HashMap<>();
        SessionManager sm=SessionManager.getInstance();
        int numsess=sm.getOldSessions().size();
        backup.put("numsess", ""+numsess);
        //salvo tutte le sessioni
        for(int i=0;i<numsess;i++){
            OldSession old=sm.getOldSessions().get(i);
            //salvo il nome della sessione i
            backup.put("nome" + i, old.getName());
            //salvo la data della sessione i
            backup.put("data" + i, old.getDataTime());
            //salvo la durata della sessione i
            backup.put("durata" + i, ""+old.getDuration());
            //salvo il path dell'immagine della firma della sessione i
            //TODO backup.put("pathimg" + i, old.getName());

            ArrayList<Fall> falls=old.getFalls();
            int numfall=falls.size();
            backup.put("numfall" + i, ""+numfall);
            //salvo tutte le cadute della sessione i
            for(int j=0;j<numfall;j++){
                Fall fall=falls.get(j);
                //salvo le coordinate della caduta j della sessione i
                backup.put("latitude" + i+"/"+j, ""+fall.getPosition(Fall.FALL_LATITUDE));
                backup.put("longitude" + i+"/"+j, ""+fall.getPosition(Fall.FALL_LONGITUDE));
                //salvo la data della caduta j della sessione i
                backup.put("data" + i + "/" + j, "" + fall.getData());
                //salvo se la caduta della caduta j della sessione i è stata segnalata o no
                backup.put("segnalato" + i+"/"+j, ""+fall.isReported());
                //salvo i valori prima della caduta j della sessione i
                float[][] before=fall.getValuesBeforeFall();
                //creo la stringa dei valori delle x prima della caduta j della sessione i e la aggiungo al backup
                String beforedatax="";
                for(int k=0;k<before[Fall.X_INDEX].length;k++){
                    if(k==0)
                        beforedatax+=before[Fall.X_INDEX][k];
                    else
                        beforedatax+="#"+before[Fall.X_INDEX][k];
                }
                backup.put("xvaluebefore" + i+"/"+j, beforedatax);
                //creo la stringa dei valori delle y prima della caduta j della sessione i e la aggiungo al backup
                String beforedatay="";
                for(int k=0;k<before[Fall.Y_INDEX].length;k++){
                    if(k==0)
                        beforedatay+=before[Fall.Y_INDEX][k];
                    else
                        beforedatay+="#"+before[Fall.Y_INDEX][k];
                }
                backup.put("yvaluebefore" + i+"/"+j, beforedatay);
                //creo la stringa dei valori delle z prima della caduta j della sessione i e la aggiungo al backup
                String beforedataz="";
                for(int k=0;k<before[Fall.Z_INDEX].length;k++){
                    if(k==0)
                        beforedataz+=before[Fall.Z_INDEX][k];
                    else
                        beforedataz+="#"+before[Fall.Z_INDEX][k];
                }
                backup.put("zvaluebefore" + i+"/"+j, beforedataz);

                //salvo i valori durante la caduta j della sessione i
                float[][] during=fall.getFallValues();
                //creo la stringa dei valori delle x durante la caduta j della sessione i e la aggiungo al backup
                String duringdatax="";
                for(int k=0;k<during[Fall.X_INDEX].length;k++){
                    if(k==0)
                        duringdatax+=during[Fall.X_INDEX][k];
                    else
                        duringdatax+="#"+during[Fall.X_INDEX][k];
                }
                backup.put("xvalueduring" + i+"/"+j, duringdatax);
                //creo la stringa dei valori delle y durante della caduta j della sessione i e la aggiungo al backup
                String duringdatay="";
                for(int k=0;k<during[Fall.Y_INDEX].length;k++){
                    if(k==0)
                        duringdatay+=during[Fall.Y_INDEX][k];
                    else
                        duringdatay+="#"+during[Fall.Y_INDEX][k];
                }
                backup.put("yvalueduring" + i+"/"+j, duringdatay);
                //creo la stringa dei valori delle z durante della caduta j della sessione i e la aggiungo al backup
                String duringdataz="";
                for(int k=0;k<during[Fall.Z_INDEX].length;k++){
                    if(k==0)
                        duringdataz+=during[Fall.Z_INDEX][k];
                    else
                        duringdataz+="#"+during[Fall.Z_INDEX][k];
                }
                backup.put("zvalueduring" + i+"/"+j, duringdataz);

                //salvo i valori dopo la caduta j della sessione i
                float[][] after=fall.getValuesAfterFall();
                //creo la stringa dei valori delle x dopo della caduta j della sessione i e la aggiungo al backup
                String afterdatax="";
                for(int k=0;k<after[Fall.X_INDEX].length;k++){
                    if(k==0)
                        afterdatax+=after[Fall.X_INDEX][k];
                    else
                        afterdatax+="#"+after[Fall.X_INDEX][k];
                }
                backup.put("xvalueafter" + i+"/"+j, afterdatax);
                //creo la stringa dei valori delle y dopo della caduta j della sessione i e la aggiungo al backup
                String afterdatay="";
                for(int k=0;k<after[Fall.Y_INDEX].length;k++){
                    if(k==0)
                        afterdatay+=after[Fall.Y_INDEX][k];
                    else
                        afterdatay+="#"+after[Fall.Y_INDEX][k];
                }
                backup.put("yvalueafter" + i+"/"+j, afterdatay);
                //creo la stringa dei valori delle z dopo della caduta j della sessione i e la aggiungo al backup
                String afterdataz="";
                for(int k=0;k<after[Fall.Z_INDEX].length;k++){
                    if(k==0)
                        afterdataz+=after[Fall.Z_INDEX][k];
                    else
                        afterdataz+="#"+after[Fall.Z_INDEX][k];
                }
                backup.put("zvalueafter" + i+"/"+j, afterdataz);
            }
        }
        return backup;
    }

    /**
     * Tale metodo permette il ripristino del modello dall'XML salvato
     */
    public void RestoreAll(){
        Toast.makeText(getBaseContext(),"Ripristino",Toast.LENGTH_SHORT).show();

        HashMap<String,String> backup=null;
        try {
            File file=new File(getFilesDir().toString(), "backup.dat");
            FileInputStream fileInputStream = new FileInputStream(file);

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            backup = (HashMap) objectInputStream.readObject();
            objectInputStream.close();

            Log.e("Backup", "Backup:" + backup.toString());
        }catch (Exception e){
            Log.e("FemurShield","Backup non trovato: "+e.getMessage());
        }
    }
}

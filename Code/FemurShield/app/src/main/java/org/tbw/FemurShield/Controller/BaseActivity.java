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
    private static int i=0;
    private int numCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numCreate=i++;
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
            outputStream.writeObject(SessionManager.getInstance().createBackup());
            outputStream.flush();
            outputStream.close();
            Log.d("FemurShield", "Salvataggio avvenuto con successo!");
        } catch (Exception e) {
            Log.e("FemurShield", "Errore salvataggio file : " + e.getMessage());
        }
    }

    /**
     * Tale metodo permette il ripristino del modello dall'XML salvato
     */
    public void RestoreAll(){
        if(numCreate==0) {//ripristino solo la prima volta che apro l'app

            HashMap<String, String> backup = null;
            try {
                File file = new File(getFilesDir().toString(), "backup.dat");
                FileInputStream fileInputStream = new FileInputStream(file);

                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                backup = (HashMap) objectInputStream.readObject();
                objectInputStream.close();
                SessionManager.getInstance().applyBackup(backup);
                Log.e("Backup", "Backup:" + backup.toString());
            } catch (IOException e) {
                Log.e("FemurShield", "Backup non trovato: " + e.getMessage());
            } catch (ClassNotFoundException e) {
            }
        }
    }
}

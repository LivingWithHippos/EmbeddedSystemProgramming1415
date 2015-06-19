package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import org.tbw.FemurShield.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void onDestroy() {
        super.onDestroy();
        SaveAll();
        finish();
    }

    /**
     * Tale metodo permette il salvataggio del modello in un xml
     */
    public void SaveAll(){
        Toast.makeText(getBaseContext(),"Salvo",Toast.LENGTH_SHORT).show();
        String FILENAME = "SessionsData";
        String string = "hello world!";

        FileOutputStream fos = null;


        try {
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e){}
    }

    /**
     * Tale metodo permette il ripristino del modello dall'XML salvato
     */
    public void RestoreAll(){
        Toast.makeText(getBaseContext(),"Ripristino",Toast.LENGTH_SHORT).show();
    }
}

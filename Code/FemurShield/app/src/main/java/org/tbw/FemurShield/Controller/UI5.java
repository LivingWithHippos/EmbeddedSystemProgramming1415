package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import org.tbw.FemurShield.R;
import java.util.HashSet;
import java.util.Set;

/*
* UI5 e' l'activity che gestisce le impostazioni, contiene il fragment fragment_settings
* e gestisce il callback dei vari fragment che rappresentano le voci del menu impostazioni
* TODO: gestire la modalit� landscape e tablet, gestire i vari stati (onPause(), etc), gestire lista email destinatari
* */
public class UI5 extends Activity implements SettingsFragment.OnFragmentInteractionListener,TimePickerFragment.OnAlarmChangedListener,DurationFragment.OnDurationChangedListener,EmailFragment.OnEmailItemClickedListener,EmailFragment.OnAddEmailButtonClickListener,AddEmailDialog.OnUserInsertedListener{

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui5);

        // controlla che il layout in uso abbia il posto per il fragment
        if (findViewById(R.id.fragment_container_settings) != null) {

            /*Se stiamo tornando indietro non abbiamo bisogno di ricaricarlo
            * rischiamo di ritrovarci con un secondo fragment sovrapposto*/
            if (savedInstanceState != null) {
                return;
            }

            // Creo il mio fragment principale
            SettingsFragment settFragment = new SettingsFragment();

            // passo gli eventuali argomenti al fragment
            //settFragment .setArguments(getIntent().getExtras());

            // carico il gestore di fragment e mostro il fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container_settings, settFragment ,"mSettingsFragment");
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
    * @param s La voce del menu selezionata.
    * Questa funzione attivata tramite callback identifica l'elemento selezionato
    * nel menu principale delle impostazioni e lancia l'interfaccia corrispondente
    */
    @Override
    public void onVoiceSelected(SettingListItem s) {
        if(s.title.equalsIgnoreCase(getString(R.string.title_alarm)))
        {
            TimePickerFragment timepick= new TimePickerFragment();
            timepick.show(getFragmentManager(), "TimePicker");
        }
        if(s.title.equalsIgnoreCase(getString(R.string.title_sample_rate)))
        {
            SliderFragment slider=new SliderFragment();
            slider.show(getFragmentManager(),"SampleRatePicker");
        }
        if(s.title.equalsIgnoreCase(getString(R.string.title_session_duration)))
        {
            DurationFragment duration=new DurationFragment();
            duration.show(getFragmentManager(),"DurationPicker");
        }
        if(s.title.equalsIgnoreCase(getString(R.string.title_email_recipient)))
        {
            EmailFragment emailFragment=new EmailFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // rimpiazza il fragment attuale e lo aggiunge allo stack in modo che premendo indietro ricompaia
            transaction.replace(R.id.fragment_container_settings, emailFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }

    }

    /**
     * @param newDuration indica la nuova durata massima della sessione scelta.
     * Questa � l'implementazione dell'interfaccia di callback dichirata nel DurationFragment
     */
    @Override
    public void onDurationChanged(int newDuration) {

        prefs=getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("session_duration", newDuration);
        editor.commit();
    }

    /**
    *  @param hourOfDay indica l'ora selezionata nel Time Picker per l'allarme
    *  @param minute indica il minuto selezionata nel Time Picker per l'allarme
    *  Implementazione dell'interfaccia di callback dichiarata in TimePickerFragment
    */
    @Override
    public void OnAlarmChanged(int hourOfDay, int minute) {
        // salva l'orario selezionato
        prefs=getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("alarm_hour", hourOfDay);
        editor.putInt("alarm_minute", minute);
        editor.commit();
        //aggiorna l'orario mostrato nella descrizione
        SettingsFragment alarmFrag = (SettingsFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        if(alarmFrag!=null)
            alarmFrag.updateAlarmTime(hourOfDay,minute);
        // TODO: impostare la sveglia con l'orario salvato
    }

    @Override
    public void onEmailItemClicked(EmailListItem e) {
        // TODO aggiungere rinomina e cancella email
        Log.d("FemureShield","Cliccato su "+e.name+" "+e.address);

    }

    @Override
    public void onAddEmailButtonClick() {
        AddEmailFragment emailFragment=new AddEmailFragment();
        emailFragment.show(getFragmentManager(),"Add Contact Dialog");
    }

    @Override
    public void onUserInserted(EditText nome, EditText indirizzo) {

        if(nome!=null&indirizzo!=null)
        {
            String n=nome.getText().toString().trim();
            String i=indirizzo.getText().toString().trim();
            String regex="[\\w]+@[\\w]+\\.(com|it|net)";
            if(i.matches(regex))
            {
                PreferencesEditor prefs=new PreferencesEditor(this);
                prefs.addEmail(n, i);
                //aggiorna la lista email 
                EmailFragment ef=(EmailFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
                ef.addAndUpdateContact(n, i);
            }
            else{Log.d("FemurShield","Sintassi email errata");}
        }
    }
}
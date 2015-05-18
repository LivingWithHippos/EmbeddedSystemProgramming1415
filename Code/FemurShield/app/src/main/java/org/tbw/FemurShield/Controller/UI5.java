package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.tbw.FemurShield.Model.SignatureImpl;
import org.tbw.FemurShield.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/*
* UI5 e' l'activity che gestisce le impostazioni, contiene il fragment fragment_settings
* e gestisce il callback dei vari fragment che rappresentano le voci del menu impostazioni
* TODO: gestire la modalita tablet, gestire i vari stati (onPause(), etc)
* */
public class UI5 extends Activity implements SettingsFragment.OnFragmentInteractionListener,TimePickerFragment.OnAlarmChangedListener,DurationFragment.OnDurationChangedListener,EmailFragment.OnEmailItemClickedListener,EmailFragment.OnAddEmailButtonClickListener,AddContactFragment.OnUserInsertedListener,SampleRatePickerFragment.OnSamplingRateChangedListener{

    private PreferencesEditor prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui5);


        if (findViewById(R.id.fragment_container_settings) != null) {

            prefs=new PreferencesEditor(this);

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

        switch(id)
        {
            case R.id.action_all_sessions:return true;
            case R.id.action_active_session: return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
    * @param s La voce del menu selezionata.
    * Questa funzione attivata tramite callback identifica l'elemento selezionato
    * nel menu principale delle impostazioni e lancia l'interfaccia corrispondente
    */
    @Override
    public void onOptionSelected(SettingListItem s) {
        if(s.title.equalsIgnoreCase(getString(R.string.title_alarm)))
        {
            TimePickerFragment timepick= new TimePickerFragment();
            timepick.show(getFragmentManager(), "TimePicker");
        }
        if(s.title.equalsIgnoreCase(getString(R.string.title_sample_rate)))
        {
            SampleRatePickerFragment slider=new SampleRatePickerFragment();
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
        if(s.title.equalsIgnoreCase("Test Signature"))
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AppDialogTheme);
            final LayoutInflater inflater = LayoutInflater.from(this);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss");
            String datetime = sdf.format(new Date());
            SignatureImpl signature=new SignatureImpl(datetime,SignatureImpl.PEAKED_CIRCLE);
            Bitmap bitmap=signature.toBitmap();
            //LinearLayOut Setup
            LinearLayout linearLayout= new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            //ImageView Setup
            ImageView imageView = new ImageView(this);
            //setting image resource
            imageView.setImageBitmap(bitmap);
            //setting image position
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            //adding view to layout
            linearLayout.addView(imageView);
            //make visible to program
            alert.setView(linearLayout);
            alert.create();
            alert.show();
        }

    }

    /**
     * @param newDuration indica la nuova durata massima della sessione scelta.
     * Questa � l'implementazione dell'interfaccia di callback dichirata nel DurationFragment
     */
    @Override
    public void onDurationChanged(int newDuration) {

        prefs.setSessionDuration(newDuration);
        SettingsFragment settingsFragment = (SettingsFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        if(settingsFragment !=null)
            settingsFragment.updateSessionDuration(newDuration);

    }

    /**
    *  @param hourOfDay indica l'ora selezionata nel Time Picker per l'allarme
    *  @param minute indica il minuto selezionata nel Time Picker per l'allarme
    *  Implementazione dell'interfaccia di callback dichiarata in TimePickerFragment
    */
    @Override
    public void OnAlarmChanged(int hourOfDay, int minute) {

        //aggiorna l'orario mostrato nella descrizione
        SettingsFragment alarmFrag = (SettingsFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        if(alarmFrag!=null)
            alarmFrag.updateAlarmTime(hourOfDay, minute);
        // TODO: impostare la sveglia con l'orario salvato
    }

    @Override
    public void onEmailItemClicked(EmailListItem e) {
        // TODO aggiungere rinomina e cancella email
        Log.d("FemureShield","Cliccato su "+e.name+" "+e.address);

    }

    @Override
    public void onAddEmailButtonClick() {
        AddContactFragment emailFragment=new AddContactFragment();
        emailFragment.show(getFragmentManager(),"Add Contact Dialog");
    }

    @Override
    public void onUserInserted(EditText nome, EditText indirizzo) {

        if(nome!=null&indirizzo!=null)
        {
            String n=nome.getText().toString().trim();
            String i=indirizzo.getText().toString().trim();
            String regex="[\\w_.-]+@[\\w.-]{5,30}";
            if(i.matches(regex))
            {
                prefs.addEmail(n, i);
                //aggiorna la lista email 
                EmailFragment ef=(EmailFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
                ef.addAndUpdateContact(n, i);
                prefs.addOneContactNumber();

            }
            else{
                Log.d("FemurShield","Sintassi email errata");
                Toast.makeText(getApplicationContext(), getString(R.string.wrong_email_message),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSamplingRateChanged(int newRate) {
        SettingsFragment settingsFragment=(SettingsFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        settingsFragment.updateSamplingRate(newRate);
    }
}

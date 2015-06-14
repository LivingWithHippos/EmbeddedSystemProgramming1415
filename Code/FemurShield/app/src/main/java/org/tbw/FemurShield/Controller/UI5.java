package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.tbw.FemurShield.Controller.Reminder.BootReceiver;
import org.tbw.FemurShield.Controller.Reminder.ReminderService;
import org.tbw.FemurShield.Controller.Settings.AddContactFragment;
import org.tbw.FemurShield.Controller.Settings.ContactOptionsDialog;
import org.tbw.FemurShield.Controller.Settings.DurationFragment;
import org.tbw.FemurShield.Controller.Settings.EmailFragment;
import org.tbw.FemurShield.Controller.Settings.EmailListItem;
import org.tbw.FemurShield.Controller.Settings.SampleRatePickerFragment;
import org.tbw.FemurShield.Controller.Settings.SettingListItem;
import org.tbw.FemurShield.Controller.Settings.SettingsFragment;
import org.tbw.FemurShield.Controller.Settings.TimePickerFragment;
import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

/**
* UI5 e' l'activity che gestisce le impostazioni, contiene il fragment fragment_settings
* e gestisce il callback dei vari fragment che rappresentano le voci del menu impostazioni
* */
public class UI5 extends Activity implements TimePickerFragment.OnAlarmChangedListener,DurationFragment.OnDurationChangedListener,EmailFragment.OnEmailItemClickedListener,EmailFragment.OnAddEmailButtonClickListener,AddContactFragment.OnUserInsertedListener,SampleRatePickerFragment.OnSamplingRateChangedListener,EmailFragment.OnClearEmailClickListener,EmailFragment.OnContactClickListener,ContactOptionsDialog.OnContactOptionsClickListener,AddContactFragment.OnUserToUpdateInsertedListener{

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

        switch(item.getItemId())
        {
            case R.id.action_all_sessions:
                Intent ui1=new Intent(this,UI1.class);
                startActivity(ui1);
                return true;
            case R.id.action_active_session:
                if(SessionManager.getInstance().getActiveSession()!=null)
                {
                    Intent ui3=new Intent(this,UI3.class);
                    startActivity(ui3);
                }
                else
                    Toast.makeText(this,getString(R.string.no_active_session),Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

        Intent alarmService = new Intent(this, ReminderService.class);
        alarmService.setAction(ReminderService.CREATE);
        startService(alarmService);
        //attiva il broadcast receiver per il boot
        //che riattiverà la sveglia ad ogni riavvio
        //nel manifest è impostato su false in modo che non parta fino
        //all'attivazione della sveglia per risparmiare risorse ed evitare errori
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onAddEmailButtonClick() {
        AddContactFragment emailFragment=new AddContactFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean(AddContactFragment.CONTACT_MODE,AddContactFragment.MODE_NEW_USER);
        emailFragment.setArguments(bundle);
        emailFragment.show(getFragmentManager(), "Add Contact Dialog");
    }

    @Override
    public boolean onUserInserted(EditText nome, EditText indirizzo) {
        boolean result=true;
        if(nome!=null&indirizzo!=null)
        {
            String n=nome.getText().toString().trim();
            String i=indirizzo.getText().toString().trim();
            Log.d("UI5","nome :"+n);
            Log.d("UI5","indirizzo :"+i);

            String regex="[\\w_.-]+@[\\w.-]{4,30}";
            if(i.matches(regex))
            {
                result=prefs.addEmail(i,n);
                Log.d("UI5","ho modificato : "+result);

                if(result) {
                    //aggiorna la lista email
                    EmailFragment ef = (EmailFragment) getFragmentManager().findFragmentById(R.id.fragment_container_settings);
                    ef.updateList();
                }
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.wrong_email_message),Toast.LENGTH_SHORT).show();

            }
            else{
                Log.d("UI5","Sintassi email errata");
                result=false;
                Toast.makeText(getApplicationContext(), getString(R.string.wrong_email_message),Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }

    @Override
    public boolean onUserToUpdateInserted(EditText nome, EditText indirizzo, String oldEmail) {

        boolean result=false;
        result = prefs.deleteContact(oldEmail);
        if(result)
            result=onUserInserted(nome,indirizzo);

        return result;
    }

    @Override
    public void onSamplingRateChanged(int newRate) {
        SettingsFragment settingsFragment=(SettingsFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        settingsFragment.updateSamplingRate(newRate);
    }

    @Override
    public void onClearEmail() {
        prefs.cleanEmailFile();
        EmailFragment ef=(EmailFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        ef.clearList();

    }

    @Override
    public void onContactLongClick(String emailAddress,String name) {
        ContactOptionsDialog dialog=new ContactOptionsDialog();
        Bundle bundle=new Bundle();
        bundle.putString(ContactOptionsDialog.SELECTED_MAIL, emailAddress);
        bundle.putString(ContactOptionsDialog.SELECTED_NAME,name);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "Contact Options Menu");
    }

    @Override
    public void onContactOptionClick(String emailAddress, String name, int type) {
        switch (type)
        {
            case ContactOptionsDialog.DELETE_CONTACT:
                if(prefs.deleteContact(emailAddress))
                {
                    EmailFragment ef=(EmailFragment)getFragmentManager().findFragmentById(R.id.fragment_container_settings);
                    ef.updateList();
                }else
                {
                    Toast.makeText(this, getString(R.string.contact_not_deleted), Toast.LENGTH_LONG).show();
                }
                break;
            case ContactOptionsDialog.EDIT_CONTACT:
                AddContactFragment emailFragment=new AddContactFragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean(AddContactFragment.CONTACT_MODE,AddContactFragment.MODE_EDIT_USER);
                bundle.putString(AddContactFragment.OLD_EMAIL, emailAddress);
                bundle.putString(AddContactFragment.OLD_NAME,name);
                emailFragment.setArguments(bundle);
                emailFragment.show(getFragmentManager(),"Add Contact Dialog");
                break;
        }
    }


}

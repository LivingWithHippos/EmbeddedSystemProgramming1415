package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import org.tbw.FemurShield.Controller.Settings.SampleRatePickerFragment;
import org.tbw.FemurShield.Controller.Settings.SettingListItem;
import org.tbw.FemurShield.Controller.Settings.SettingsFragment;
import org.tbw.FemurShield.Controller.Settings.TimePickerFragment;
import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

/**
 * UI5 e' l'activity che gestisce le impostazioni, contiene il fragment fragment_settings
 * e gestisce il callback dei vari fragment che rappresentano le voci del menu impostazioni {@link SettingsFragment}:
 * {@link SampleRatePickerFragment}, {@link DurationFragment}, {@link EmailFragment}, {@link TimePickerFragment}
 *
 * @author Marco Biasin
 */
public class UI5 extends Activity implements SettingsFragment.OnOptionSelectedListener, TimePickerFragment.OnAlarmChangedListener, DurationFragment.OnDurationChangedListener, EmailFragment.OnAddEmailButtonClickListener, AddContactFragment.OnUserInsertedListener, SampleRatePickerFragment.OnSamplingRateChangedListener, EmailFragment.OnClearEmailClickListener, EmailFragment.OnContactClickListener, ContactOptionsDialog.OnContactOptionsClickListener, AddContactFragment.OnUserToUpdateInsertedListener {

    private PreferencesEditor prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui5);


        if (findViewById(R.id.fragment_container_settings) != null) {

            prefs = new PreferencesEditor(this);

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

            fragmentTransaction.add(R.id.fragment_container_settings, settFragment, "mSettingsFragment");
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui5, menu);
        return true;
    }

    /**
     * Gestisce il menu in alto a destra.
     *
     * @param item la voce del menu selezionata
     * @return true se l'evento e' stato consumato, altrimenti viene passato al menu "superiore"
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_all_sessions:
                Intent ui1 = new Intent(this, UI1.class);
                startActivity(ui1);
                return true;
            case R.id.action_active_session:
                if (SessionManager.getInstance().getActiveSession() != null) {
                    Intent ui3 = new Intent(this, UI3.class);
                    startActivity(ui3);
                } else
                    Toast.makeText(this, getString(R.string.no_active_session), Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Salva la nuova durata massima della sessione e aggiorna la vista nel menu {@link SettingsFragment}
     *
     * @param newDuration indica la nuova durata massima della sessione scelta.
     *                    Questa e' l'implementazione dell'interfaccia di callback dichirata nel DurationFragment
     */
    @Override
    public void onDurationChanged(int newDuration) {

        prefs.setSessionDuration(newDuration);
        SettingsFragment settingsFragment = (SettingsFragment) getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        if (settingsFragment != null)
            settingsFragment.updateSessionDuration(newDuration);

    }

    /**
     * Metodo che gestisce il nuovo orario dell'allarme selezionato in {@link TimePickerFragment}.
     * Fa partire il servizio di sveglia {@link ReminderService}
     *
     * @param hourOfDay indica l'ora selezionata nel Time Picker per l'allarme
     * @param minute    indica il minuto selezionata nel Time Picker per l'allarme
     *                  Implementazione dell'interfaccia di callback dichiarata in TimePickerFragment
     */
    @Override
    public void OnAlarmChanged(int hourOfDay, int minute) {

        //aggiorna l'orario mostrato nella descrizione
        SettingsFragment alarmFrag = (SettingsFragment) getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        if (alarmFrag != null)
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

    /**
     * Metodo lanciato alla pressione del pulsante "+" {@link } nella schermata dei contatti {@link EmailFragment}.
     * Fa comparire un dialog {@link AddContactFragment} che permette di inserirne uno nuovo.
     */
    @Override
    public void onAddEmailButtonClick() {
        AddContactFragment emailFragment = new AddContactFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(AddContactFragment.CONTACT_MODE, AddContactFragment.MODE_NEW_USER);
        emailFragment.setArguments(bundle);
        emailFragment.show(getFragmentManager(), "Add Contact Dialog");
    }

    /**
     * Metodo lanciato all'inserimento di un nuovo contatto per l'email, {@link UI5#onAddEmailButtonClick()}
     * salva i dati se corretti.
     *
     * @param nome      EditText in cui e stato inserito il nome
     * @param indirizzo EditText in cui e stato inserita l email
     * @return true se viene salvato (file ok e email non gia' presente) o false altrimenti
     */
    @Override
    public boolean onUserInserted(EditText nome, EditText indirizzo) {
        boolean result = true;
        if (nome != null & indirizzo != null) {
            String n = nome.getText().toString().trim();
            String i = indirizzo.getText().toString().trim();

            String regex = "[\\w_.-]+@[\\w.-]{4,30}";
            if (i.matches(regex)) {
                result = prefs.addEmail(i, n);

                if (result) {
                    //aggiorna la lista email
                    EmailFragment ef = (EmailFragment) getFragmentManager().findFragmentById(R.id.fragment_container_settings);
                    ef.updateList();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.already_added_email_message), Toast.LENGTH_SHORT).show();

            } else {
                result = false;
                Toast.makeText(getApplicationContext(), getString(R.string.wrong_email_message), Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }

    /**
     * Metodo lanciato dopo aver premuto modifica sul menu a comparsa di un contatto  {@link ContactOptionsDialog}
     * e aver premuto ok dopo averlo modificato nel Dialog {@link AddContactFragment}
     *
     * @param nome      l'EditText contenente il nuovo nome
     * @param indirizzo l'EditText contenente il nuovo indirizzo
     * @param oldEmail  la vecchia email, usata come chiave per riconoscere che contatto rimpiazzare
     * @return true se viene rimpiazzato correttamente
     */
    @Override
    public boolean onUserToUpdateInserted(EditText nome, EditText indirizzo, String oldEmail) {

        boolean result = false;
        result = prefs.deleteContact(oldEmail);
        if (result)
            result = onUserInserted(nome, indirizzo);

        return result;
    }

    /**
     * Salva la frequenza di campionamento scelta del sensore tramite il Dialog {@link SampleRatePickerFragment} e aggiorna la vista nel menu {@link SettingsFragment}
     *
     * @param newRate il nuovo valore da salvare
     */
    @Override
    public void onSamplingRateChanged(int newRate) {
        SettingsFragment settingsFragment = (SettingsFragment) getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        settingsFragment.updateSamplingRate(newRate);
    }

    /**
     * Metodo chiamato alla pressione del tasto "Elimina Contatti"
     * nel menu della {@link UI5}.
     * Elimina tutti i contatti e aggiorna la lista contatti svuotandola.
     */
    @Override
    public void onClearEmail() {
        prefs.cleanEmailFile();
        EmailFragment ef = (EmailFragment) getFragmentManager().findFragmentById(R.id.fragment_container_settings);
        ef.clearList();
    }

    /**
     * Metodo chiamato alla pressione prolungata su un contatto, permette di modificarlo o eliminarlo.
     * Mostra il Dialog {@link ContactOptionsDialog}
     *
     * @param emailAddress l'indirizzo del contatto selezionato
     * @param name         il nome del contatto selezionato
     */
    @Override
    public void onContactLongClick(String emailAddress, String name) {
        ContactOptionsDialog dialog = new ContactOptionsDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ContactOptionsDialog.SELECTED_MAIL, emailAddress);
        bundle.putString(ContactOptionsDialog.SELECTED_NAME, name);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "Contact Options Menu");
    }

    /**
     * Metodo che rileva la pressione su "modifica" o "elimina"
     * nel menu a comparsa dei contatti {@link ContactOptionsDialog}
     *
     * @param emailAddress l'indirizzo del contatto selezionato
     * @param name         il nome del contatto selezionato
     * @param type         se si tratta di eliminazione ({@link ContactOptionsDialog#DELETE_CONTACT})o modifica ({@link ContactOptionsDialog#EDIT_CONTACT}) del contatto.
     */
    @Override
    public void onContactOptionClick(String emailAddress, String name, int type) {
        switch (type) {
            case ContactOptionsDialog.DELETE_CONTACT:
                if (prefs.deleteContact(emailAddress)) {
                    EmailFragment ef = (EmailFragment) getFragmentManager().findFragmentById(R.id.fragment_container_settings);
                    ef.updateList();
                } else {
                    Toast.makeText(this, getString(R.string.contact_not_deleted), Toast.LENGTH_LONG).show();
                }
                break;
            case ContactOptionsDialog.EDIT_CONTACT:
                AddContactFragment emailFragment = new AddContactFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(AddContactFragment.CONTACT_MODE, AddContactFragment.MODE_EDIT_USER);
                bundle.putString(AddContactFragment.OLD_EMAIL, emailAddress);
                bundle.putString(AddContactFragment.OLD_NAME, name);
                emailFragment.setArguments(bundle);
                emailFragment.show(getFragmentManager(), "Add Contact Dialog");
                break;
        }
    }

    /**
     * Metodo che gestisce le voci scelte nella lista opzioni su {@link SettingsFragment}
     *
     * @param s l'opzione scelta dalla lista
     */
    @Override
    public void onOptionSelected(SettingListItem s) {

        if (s.title.equalsIgnoreCase(getString(R.string.title_alarm))) {
            TimePickerFragment timepick = new TimePickerFragment();
            timepick.show(getFragmentManager(), "TimePicker");
        }
        if (s.title.equalsIgnoreCase(getString(R.string.title_sample_rate))) {
            SampleRatePickerFragment slider = new SampleRatePickerFragment();
            slider.show(getFragmentManager(), "SampleRatePicker");
        }
        if (s.title.equalsIgnoreCase(getString(R.string.title_session_duration))) {
            DurationFragment duration = new DurationFragment();
            duration.show(getFragmentManager(), "DurationPicker");
        }
        if (s.title.equalsIgnoreCase(getString(R.string.title_email_recipient))) {
            EmailFragment emailFragment = EmailFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // rimpiazza il fragment attuale e lo aggiunge allo stack in modo che premendo indietro ricompaia
            transaction.replace(R.id.fragment_container_settings, emailFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
}

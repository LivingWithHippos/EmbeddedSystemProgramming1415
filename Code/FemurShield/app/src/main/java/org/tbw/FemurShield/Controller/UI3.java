package org.tbw.FemurShield.Controller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

/**
 * La UI3 e' l'acivity che gestisce la sessione attiva attraverso i comandi di {@link SessionCommandsFragment},
 * mostra l'andamento dell'accelerometro su grafico tramite {@link ActiveSessionFragment} ed i dettagli della sessione con {@link SessionDetailsFragment}
 */
public class UI3 extends Activity implements ActiveSessionFragment.OnFallDetectedListener, ActiveSessionFragment.OnEmailSentListener, EditSessionNameFragment.OnSessionNameInsertedListener, FallFragment.OnFallClickListener, SessionCommandsFragment.OnCommandUpdatedListener {


    public final static String ACTIVE_SESSION_FRAGMENT_TAG = "mActiveSessionFragment";
    public final static String FALLS_LIST_FRAGMENT_TAG = "mFallsListFragment";
    public final static String SESSION_DETAILS_FRAGMENT_TAG = "mSessionDetailsFragment";
    public final static String SESSION_COMMANDS_FRAGMENT_TAG = "mSessionCommandsFragment";

    private String thisData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui3);
        PreferencesEditor pe = new PreferencesEditor(this);
        if (pe.getEmailContactsNumber() < 1)
            Toast.makeText(this, getString(R.string.no_contacts_warning), Toast.LENGTH_SHORT).show();
        checkGPS();

        if (findViewById(R.id.ui3rootLayout) != null) {

            if (SessionManager.getInstance().getActiveSession() != null) {
                thisData = SessionManager.getInstance().getActiveSession().getDataTime();

                SessionDetailsFragment sdf = SessionDetailsFragment.newIstance(thisData, SessionDetailsFragment.UI_3_MODE);
                ActiveSessionFragment asf = ActiveSessionFragment.newIstance();
                SessionCommandsFragment scf = SessionCommandsFragment.newInstance(SessionCommandsFragment.MODE_SMALL);
                FallFragment ff = FallFragment.newInstance(thisData);

                getFragmentManager().beginTransaction().replace(R.id.sessionDetailLayout, sdf, SESSION_DETAILS_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.sessionGraphLayout, asf, ACTIVE_SESSION_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.fallListLayout, ff, FALLS_LIST_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.commandLayout, scf, SESSION_COMMANDS_FRAGMENT_TAG).commit();


            } else {
                finish();
            }
        }
    }

    /**
     * Controlla che sia acceso il GPS e manda messaggio d'errore in caso contrario
     */
    private void checkGPS()
    {
        try
        {
            LocationManager lm;
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_on=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!gps_on)
                Toast.makeText(this,getString(R.string.gps_off),Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, UI5.class);
                startActivity(intent);
                return true;
            case R.id.action_all_sessions:
                Intent ui1 = new Intent(this, UI1.class);
                startActivity(ui1);
                return true;
            case R.id.rename_session:
                EditSessionNameFragment sessionRenameFragment = new EditSessionNameFragment();
                Bundle bundle = new Bundle();
                bundle.putString(EditSessionNameFragment.SESSION_DATA, SessionManager.getInstance().getActiveSession().getId());
                sessionRenameFragment.setArguments(bundle);
                sessionRenameFragment.show(getFragmentManager(), "Edit Session Name Dialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo di implementazione dell'interfaccia {@link org.tbw.FemurShield.Controller.FallFragment.OnFallClickListener}
     * Mostra i dettagli della caduta su cui si e' premuto tramite interfaccia {@link UI4}
     *
     * @param sessionID l'ID della sessione a cui appartiene la caduta
     * @param fallID    l'ID della caduta da mostrare
     */
    @Override
    public void onFallClick(String sessionID, String fallID) {
        Intent fallDetails = new Intent(this, UI4.class);
        fallDetails.putExtra(UI4.ID_SESSION, sessionID);
        fallDetails.putExtra(UI4.ID_FALL, fallID);
        startActivity(fallDetails);
    }

    /**
     * Chiamato quando un bottone di comando viene premuto
     *
     * @param buttonPressed il bottone che ha causato l'aggiornamento,
     *                      vedi {@value SessionCommandsFragment#BUTTON_STOP}, {@value SessionCommandsFragment#BUTTON_PLAY}, {@value SessionCommandsFragment#BUTTON_PAUSE}, {@value SessionCommandsFragment#BUTTON_REC}
     */
    @Override
    public void aggiornaLista(int buttonPressed) {
        switch (buttonPressed) {
            case SessionCommandsFragment.BUTTON_PAUSE:
                ((ActiveSessionFragment) getFragmentManager().findFragmentByTag(ACTIVE_SESSION_FRAGMENT_TAG)).stopChrono();
                break;
            case SessionCommandsFragment.BUTTON_PLAY:
                ((ActiveSessionFragment) getFragmentManager().findFragmentByTag(ACTIVE_SESSION_FRAGMENT_TAG)).startChrono();
                break;
            case SessionCommandsFragment.BUTTON_STOP:
                ((ActiveSessionFragment) getFragmentManager().findFragmentByTag(ACTIVE_SESSION_FRAGMENT_TAG)).stopChrono();
                Intent i = new Intent(this, UI2.class);
                i.putExtra(UI2.SESSION_DATA_STAMP, thisData);
                startActivity(i);
                finish();
                break;
        }
    }

    /**
     * Metodo di implementazione dell'interfaccia {@link org.tbw.FemurShield.Controller.EditSessionNameFragment.OnSessionNameInsertedListener}
     * modifica il nome della sessione e aggiorna la lista
     *
     * @param nome      il nome da salvare
     * @param sessionID l'ID della sessione da rinominare
     */
    @Override
    public void onSessionNameInserted(String nome, String sessionID) {
        Controller.getInstance().renameEvent(sessionID, nome);
        SessionDetailsFragment sdf = (SessionDetailsFragment) getFragmentManager().findFragmentByTag(SESSION_DETAILS_FRAGMENT_TAG);
        sdf.updateNameView(nome);
    }

    /**
     * Metodo di implementazione dell'interfaccia {@link org.tbw.FemurShield.Controller.ActiveSessionFragment.OnEmailSentListener}
     * Aggiorna la conferma visuale che una caduta e' stata notificata dopo la sua notifica
     */
    @Override
    public void onEmailSent() {
        FallFragment ff = (FallFragment) getFragmentManager().findFragmentByTag(FALLS_LIST_FRAGMENT_TAG);
        if (ff != null)
            ff.startlist();
    }

    /**
     * Aggiorna la lista cadute dopo la rilevazione di una caduta
     */
    @Override
    public void onFallDetect() {
        FallFragment ff = (FallFragment) getFragmentManager().findFragmentByTag(FALLS_LIST_FRAGMENT_TAG);
        Log.d("UI3", "aggiorno fall");
        if (ff != null)
            ff.startlist();
    }
}


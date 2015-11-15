package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

/**
 * UI2 e' l'activity che mostra i dettagli di una sessione gia' conclusa ed una lista delle sue cadute
 * tramite i fragment {@link SessionDetailsFragment} e {@link FallFragment}.
 *
 * @author Luca Vianello
 */
public class UI2 extends Activity implements FallFragment.OnFallClickListener, EditSessionNameFragment.OnSessionNameInsertedListener {

    public final static String SESSION_DATA_STAMP = "sessiondatastamp";
    public final static String SESSION_DETAILS_FRAGMENT_TAG = "sessionDetails";
    private String thisData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui2);

        if (findViewById(R.id.ui2rootLayout) != null) {
            /*Se stiamo tornando indietro non abbiamo bisogno di ricaricarlo
            * rischiamo di ritrovarci con un secondo fragment sovrapposto*/
            if (savedInstanceState != null) {
                return;
            }

            thisData = getIntent().getExtras().getString(SESSION_DATA_STAMP);
            SessionDetailsFragment sdf = SessionDetailsFragment.newIstance(thisData, SessionDetailsFragment.UI_2_MODE);
            FallFragment ff = FallFragment.newInstance(thisData);

            getFragmentManager().beginTransaction().replace(R.id.sessionDetailsUI2, sdf, SESSION_DETAILS_FRAGMENT_TAG).commit();
            getFragmentManager().beginTransaction().replace(R.id.fallsListUI2, ff, "FallDetails").commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui2, menu);
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
            case R.id.action_active_session:
                if (SessionManager.getInstance().getActiveSession() != null) {
                    Intent ui3 = new Intent(this, UI3.class);
                    startActivity(ui3);
                } else
                    Toast.makeText(this, getString(R.string.no_active_session), Toast.LENGTH_LONG).show();
                return true;
            case R.id.rename_session:
                EditSessionNameFragment sessionRenameFragment = new EditSessionNameFragment();
                Bundle bundle = new Bundle();
                bundle.putString(EditSessionNameFragment.SESSION_DATA, thisData);
                sessionRenameFragment.setArguments(bundle);
                sessionRenameFragment.show(getFragmentManager(), "Edit Session Name Dialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish(); // chiude la UI2 quando premi il tasto back e ritorna alla chiamante (UI1)
    }

    /**
     * Metodo di implementazione dell'interfaccia {@link org.tbw.FemurShield.Controller.FallFragment.OnFallClickListener}, solo nel caso della UI per tablet
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
     * Metodo di implementazione dell'interfaccia {@link org.tbw.FemurShield.Controller.EditSessionNameFragment.OnSessionNameInsertedListener}
     * modifica il nome della sessione e aggiorna la view
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
}

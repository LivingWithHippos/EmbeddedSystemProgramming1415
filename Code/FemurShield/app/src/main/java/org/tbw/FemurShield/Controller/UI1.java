package org.tbw.FemurShield.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

/**
 * UI5 e' l'activity che gestisce tutte le sessioni, attive e non, mostrandole in una lista {@link FallFragment}
 * e permettendone la modifica e la visualizzazione, oltre a permettere la gestione della
 * registrazione di una nuova sessione tramite comandi {@link SessionCommandsFragment}
 */
public class UI1 extends Activity implements FallFragment.OnFallClickListener, SessionsListFragment.OnSessionClickListener, SessionCommandsFragment.OnCommandUpdatedListener, SessionOptionDialog.OnSessionOptionsClickListener, EditSessionNameFragment.OnSessionNameInsertedListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 13333337;
    private static int i = 0;
    private final static String COMMAND_FRAGMENT_TAG = "mSessionCommandsFragment";
    private final static String SESSIONS_FRAGMENT_TAG = "mSessionListFragment";
    public final static String SESSION_EMPTY = "empty_session";
    public final static String SESSION_DETAILS_FRAGMENT_TAG = "sessionDetailsUI1";
    public final static String FALLS_LIST_FRAGMENT_TAG = "fallsListUI1";
    private static String lastChosenSession = SESSION_EMPTY;
    private static final boolean SHOW_FRAGMENT = true, HIDE_FRAGMENT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui1);
        //avverto il controller che è avvenuta la prima appertura dell'activity e dovrà quindi fare il restore dei dati
        if (i++ == 0) {
            Controller.getInstance().firstOpenEvent(this);
        }

        if (findViewById(R.id.ui1rootLayout) != null) {

            /*Se stiamo tornando indietro non abbiamo bisogno di ricaricarlo
            * rischiamo di ritrovarci con un secondo fragment sovrapposto*/
            if (savedInstanceState != null) {
                return;
            }

            // carico il gestore di fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //se siamo nel caso display grande aggiungo altri fragment
            if (findViewById(R.id.fallsListUI1) != null & findViewById(R.id.sessionDetailsUI1) != null) {
                SessionDetailsFragment sdf = SessionDetailsFragment.newIstance(lastChosenSession, SessionDetailsFragment.UI_2_MODE);
                FallFragment ff = FallFragment.newInstance(lastChosenSession);

                fragmentTransaction.replace(R.id.sessionDetailsUI1, sdf, SESSION_DETAILS_FRAGMENT_TAG);
                fragmentTransaction.replace(R.id.fallsListUI1, ff, FALLS_LIST_FRAGMENT_TAG);
                if (lastChosenSession.equalsIgnoreCase(SESSION_EMPTY)) {
                    fragmentTransaction.hide(sdf);
                    fragmentTransaction.hide(ff);
                }

            }

            SessionsListFragment sFragment = SessionsListFragment.newInstance();
            SessionCommandsFragment cFragment = SessionCommandsFragment.newInstance(SessionCommandsFragment.MODE_BIG);

            fragmentTransaction.replace(R.id.listaSessioniUI1, sFragment, SESSIONS_FRAGMENT_TAG);
            fragmentTransaction.replace(R.id.comandiSessioniUI1, cFragment, COMMAND_FRAGMENT_TAG);

            fragmentTransaction.commit();
        }

        instantiateColors();
        if (SignatureLoaderTask.spaceAvailable() < 3)
            Toast.makeText(this, getString(R.string.low_memory_warning), Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                );
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // Se la richiesta è cancellata l'array sarà vuoto
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permessi accordati
                } else {
                    Toast.makeText(this.getBaseContext(), getString(R.string.gps_needed), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Gestisce il menu in alto a destra.
     *
     * @param item la voce del menu selezionata
     * @return true se l'evento e' stato consumato, altrimenti viene passato al menu "superiore"
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_active_session:
                if (SessionManager.getInstance().getActiveSession() != null) {
                    Intent ui3 = new Intent(this, UI3.class);
                    startActivity(ui3);
                } else
                    Toast.makeText(this, getString(R.string.no_active_session), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionsListFragment slf = (SessionsListFragment) getFragmentManager().findFragmentByTag(SESSIONS_FRAGMENT_TAG);
        //se siamo in modalita' tablet carico l'ultima sessione su cui si era premuto
        if (!lastChosenSession.equalsIgnoreCase(SESSION_EMPTY))
            loadUI2fragments(lastChosenSession);
        //aggiorno la lista sessioni
        slf.aggiornaLista();
    }

    /**
     * Forza un aggiornamento della lista delle sessioni
     *
     * @param buttonPressed il bottone che ha causato l'aggiornamento, ma nella UI1 il suo unico effetto e' l'aggiornamento della lista, quindi rimane inutilizzato
     */
    public void aggiornaLista(int buttonPressed) {
        SessionsListFragment fragment = (SessionsListFragment) getFragmentManager().findFragmentById(R.id.listaSessioniUI1);
        fragment.aggiornaLista();
    }

    /**
     * apre la finestra delle impostazioni {@link UI5}
     */
    public void openSettings() {
        Intent intent = new Intent(this, UI5.class);
        startActivity(intent);
    }

    /**
     * Inizializza i colori per il grafico create da {@link FallBitmapCreator} associando alla classe {@link ColorsPicker} il file arrays.xml
     */
    private void instantiateColors() {

        ColorsPicker colorsPicker = ColorsPicker.getInstance(new String[][]{
                getResources().getStringArray(R.array.random_palette_1),
                getResources().getStringArray(R.array.random_palette_2),
                getResources().getStringArray(R.array.random_palette_3),
                getResources().getStringArray(R.array.random_palette_4),
                getResources().getStringArray(R.array.random_palette_5),
                getResources().getStringArray(R.array.random_palette_6),
                getResources().getStringArray(R.array.random_palette_7)
        });

    }

    /**
     * Metodo di implementazione dell'interfaccia {@link org.tbw.FemurShield.Controller.SessionsListFragment.OnSessionClickListener}
     * richiamato dal callback nella classe {@link SessionsListFragment} in seguito alla pressione breve su di una sessione.
     * Lancia la {@link UI3} o la {@link UI2} a seconda che venga premuto su una sessione non attiva  o no
     *
     * @param sessionID l'ID della sessione su cui si e' premuto
     */
    @Override
    public void onSessionClick(String sessionID) {
        if (SessionManager.getInstance().getActiveSession() != null && SessionManager.getInstance().getActiveSession().getId().equals(sessionID)) {
            //lancio la UI che gestisce le sessioni attive
            Intent i = new Intent(getBaseContext(), UI3.class);
            startActivity(i);
        } else {
            //modalita' schermo grande, carico i fragment sulla destra invece di una nuova UI
            if (findViewById(R.id.fallsListUI1) != null & findViewById(R.id.sessionDetailsUI1) != null) {
                loadUI2fragments(sessionID);
            } else {
                //modalta' schermo piccolo, lancio una nuova activity
                Intent i = new Intent(getBaseContext(), UI2.class);
                i.putExtra(UI2.SESSION_DATA_STAMP, sessionID);
                startActivity(i);
            }
        }
    }

    /**
     * Inizializza i fragment (gia' presenti ma invisibili) {@link SessionDetailsFragment} e {@link FallFragment} che contengono i dettagli di una sessione e le sue cadute. Usato solo in caso di schermo grande.
     *
     * @param sessionID l'ID della sessione da rappresentare
     */
    private void loadUI2fragments(String sessionID) {
        if (findViewById(R.id.fallsListUI1) != null & findViewById(R.id.sessionDetailsUI1) != null) {
            SessionDetailsFragment sdf = (SessionDetailsFragment) getFragmentManager().findFragmentByTag(SESSION_DETAILS_FRAGMENT_TAG);
            FallFragment ff = (FallFragment) getFragmentManager().findFragmentByTag(FALLS_LIST_FRAGMENT_TAG);
            lastChosenSession = sessionID;
            if (sessionID.equalsIgnoreCase(SESSION_EMPTY)) {
                showHideFragment(sdf, HIDE_FRAGMENT);
                showHideFragment(ff, HIDE_FRAGMENT);
            } else {
                sdf.setSession(sessionID);
                sdf.startDetails();
                ff.setSession(sessionID);
                ff.startlist();
                showHideFragment(sdf, SHOW_FRAGMENT);
                showHideFragment(ff, SHOW_FRAGMENT);
            }
        }
    }

    /**
     * Metodo per nascondere o mostrare un fragment
     *
     * @param fragment   il frammento da nascondere
     * @param hideOrShow true se il fragment e' da mostrare, false altrimenti, vedi {@value #SHOW_FRAGMENT} e {@value #HIDE_FRAGMENT}
     */
    private void showHideFragment(Fragment fragment, boolean hideOrShow) {

        if (fragment != null) {
            if (hideOrShow == SHOW_FRAGMENT) {
                if (fragment.isHidden()) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.show(fragment);
                    fragmentTransaction.commit();
                }
            } else {
                if (!fragment.isHidden()) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(fragment);
                    fragmentTransaction.commit();
                }
            }

        }


    }

    /**
     * Metodo di implementazione di dell'interfaccia {@link org.tbw.FemurShield.Controller.SessionsListFragment.OnSessionClickListener}
     * richiamato dal callback nella classe {@link SessionsListFragment} in seguito alla pressione lunga su di una sessione.
     * Permette la scelta tra l'eliminazione o la modifica di una sessione tramite {@link SessionOptionDialog}
     *
     * @param sessionID l'ID della sessione da mostrare
     */
    @Override
    public void onSessionLongClick(String sessionID) {
        SessionOptionDialog dialog = new SessionOptionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SessionOptionDialog.SELECTED_DATA, sessionID);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "Contact Options Menu");
    }

    /**
     * Metodo di implementazione dell'interfaccia {@link org.tbw.FemurShield.Controller.SessionOptionDialog.OnSessionOptionsClickListener}
     * Richiamato dalla pressione di modifica o elimina nelle opzioni di una sessione
     *
     * @param sessionID l'ID della sessione da modificare
     * @param type      indica se la sessione e' da modificare o eliminare, vedi {@value SessionOptionDialog#DELETE_SESSION} e {@value SessionOptionDialog#RENAME_SESSION}
     */
    @Override
    public void onSessionOptionClick(String sessionID, int type) {
        if (type == SessionOptionDialog.DELETE_SESSION) {
            if (SessionManager.getInstance().isRunning() &&
                    SessionManager.getInstance().getActiveSession().getDataTime().equalsIgnoreCase(sessionID)) {
                //se la sessione da eliminare e' quella attiva simulo il click sul tasto stop
                SessionCommandsFragment scf = (SessionCommandsFragment) getFragmentManager().findFragmentByTag(COMMAND_FRAGMENT_TAG);
                scf.onStopClick();
            }
            //procedo in ogni caso ad eliminarla ed aggiornare la lista sessioni
            Controller.getInstance().deleteEvent(sessionID);
            SessionsListFragment slf = (SessionsListFragment) getFragmentManager().findFragmentByTag(SESSIONS_FRAGMENT_TAG);
            slf.aggiornaLista();
            if (lastChosenSession.equalsIgnoreCase(sessionID))
                loadUI2fragments(SESSION_EMPTY);
        } else {
            if (type == SessionOptionDialog.RENAME_SESSION) {
                //mostro il dialog di rinomina della sessione
                EditSessionNameFragment sessionRenameFragment = new EditSessionNameFragment();
                Bundle bundle = new Bundle();
                bundle.putString(EditSessionNameFragment.SESSION_DATA, sessionID);
                sessionRenameFragment.setArguments(bundle);
                sessionRenameFragment.show(getFragmentManager(), "Edit Session Name Dialog");
            }
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
        SessionsListFragment slf = (SessionsListFragment) getFragmentManager().findFragmentByTag(SESSIONS_FRAGMENT_TAG);
        slf.aggiornaLista();
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
}

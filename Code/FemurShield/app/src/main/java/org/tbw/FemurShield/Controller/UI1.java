package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;


public class UI1 extends Activity implements SessionsListFragment.OnSessionClickListener,SessionCommandsFragment.OnCommandUpdatedListener,SessionOptionDialog.OnSessionOptionsClickListener, EditSessionNameFragment.OnUserInsertedListener {

    private static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui1);
        //avverto il controller che è avvenuta la prima appertura dell'activity e dovrà quindi fare il restore dei dati
        if(i++==0){
            Controller.getInstance().firstOpenEvent(this);
        }

        if (findViewById(R.id.ui1rootLayout) != null) {

            /*Se stiamo tornando indietro non abbiamo bisogno di ricaricarlo
            * rischiamo di ritrovarci con un secondo fragment sovrapposto*/
            if (savedInstanceState != null) {
                return;
            }

            SessionsListFragment sFragment=SessionsListFragment.newInstance();
            SessionCommandsFragment cFragment=SessionCommandsFragment.newInstance(SessionCommandsFragment.MODE_BIG);

            // carico il gestore di fragment e mostro il fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.listaSessioniUI1, sFragment, "mSessionsListFragment");
            fragmentTransaction.add(R.id.comandiSessioniUI1, cFragment, "mSessionCommandsFragment");
            fragmentTransaction.commit();
        }

        instantiateColors();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionsListFragment slf=(SessionsListFragment)getFragmentManager().findFragmentByTag("mSessionsListFragment");
        slf.aggiornaLista();
    }

    public void aggiornaLista(int buttonPressed){
        SessionsListFragment fragment = (SessionsListFragment) getFragmentManager().findFragmentById(R.id.listaSessioniUI1);
        fragment.aggiornaLista();
    }

    public void openSettings()
    {
        //lancio senza opzioni perchè la ui impostazioni non ne richiede
        Intent intent=new Intent(this,UI5.class);
        startActivity(intent);
    }

    private void instantiateColors()
    {

        ColorsPicker colorsPicker=ColorsPicker.getInstance(new String[][]{
                getResources().getStringArray(R.array.random_palette_1),
                getResources().getStringArray(R.array.random_palette_2),
                getResources().getStringArray(R.array.random_palette_3),
                getResources().getStringArray(R.array.random_palette_4),
                getResources().getStringArray(R.array.random_palette_5),
                getResources().getStringArray(R.array.random_palette_6),
                getResources().getStringArray(R.array.random_palette_7)
        });

    }

    @Override
    public void onSessionClick(String sessionID) {
        if(SessionManager.getInstance().getActiveSession()!=null && SessionManager.getInstance().getActiveSession().getId().equals(sessionID)){
            Intent i = new Intent(getBaseContext(), UI3Neo.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(getBaseContext(), UI2.class);
            i.putExtra(UI2.SESSION_DATA_STAMP, sessionID);
            startActivity(i);
        }
    }

    @Override
    public void onSessionLongClick(String data) {
        SessionOptionDialog dialog = new SessionOptionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SessionOptionDialog.SELECTED_DATA, data);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "Contact Options Menu");
    }

    @Override
    public void onActiveSessionLongClick(String data) {
        EditSessionNameFragment sessionRenameFragment = new EditSessionNameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EditSessionNameFragment.SESSION_DATA, data);
        sessionRenameFragment.setArguments(bundle);
        sessionRenameFragment.show(getFragmentManager(), "Edit Session Name Dialog");
    }

    @Override
    public void onSessionOptionClick(String data, int type) {
        if(type==SessionOptionDialog.DELETE_SESSION){
            Controller.getInstance().deleteEvent(data);
            SessionsListFragment slf=(SessionsListFragment)getFragmentManager().findFragmentByTag("mSessionsListFragment");
            slf.aggiornaLista();
        }
        else {
            if (type == SessionOptionDialog.RENAME_SESSION) {
                EditSessionNameFragment sessionRenameFragment = new EditSessionNameFragment();
                Bundle bundle = new Bundle();
                bundle.putString(EditSessionNameFragment.SESSION_DATA, data);
                sessionRenameFragment.setArguments(bundle);
                sessionRenameFragment.show(getFragmentManager(), "Edit Session Name Dialog");
            }
        }
    }

    @Override
    public void onUserInserted(String nome,String data) {
        Controller.getInstance().renameEvent(data, nome);
        SessionsListFragment slf = (SessionsListFragment) getFragmentManager().findFragmentByTag("mSessionsListFragment");
        slf.aggiornaLista();
    }
}

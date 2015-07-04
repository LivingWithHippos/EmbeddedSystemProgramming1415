package org.tbw.FemurShield.Controller;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

/**
 * Created by Marco on 27/06/2015.
 */

public class UI3 extends Activity implements ActiveSessionFragment.OnFallDetectedListener,EditSessionNameFragment.OnSessionNameInsertedListener,FallFragment.OnFallClickListener,SessionCommandsFragment.OnCommandUpdatedListener{


    public final static String ACTIVE_SESSION_FRAGMENT_TAG = "mActiveSessionFragment";
    public final static String FALLS_LIST_FRAGMENT_TAG = "mFallsListFragment";
    public final static String SESSION_DETAILS_FRAGMENT_TAG = "mSessionDetailsFragment";
    public final static String SESSION_COMMANDS_FRAGMENT_TAG = "mSessionCommandsFragment";

    private String thisData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui3);
        if (findViewById(R.id.ui3rootLayout) != null) {

            int orientation = this.getResources().getConfiguration().orientation;
            thisData = SessionManager.getInstance().getActiveSession().getDataTime();

            SessionDetailsFragment sdf = SessionDetailsFragment.newIstance(thisData, SessionDetailsFragment.UI_3_MODE);
            ActiveSessionFragment asf = ActiveSessionFragment.newIstance();
            SessionCommandsFragment scf = SessionCommandsFragment.newInstance(SessionCommandsFragment.MODE_SMALL);
            FallFragment ff = FallFragment.newInstance(thisData);


            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                getFragmentManager().beginTransaction().replace(R.id.sessionDetailLayout, sdf, SESSION_DETAILS_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.sessionGraphLayout, asf, ACTIVE_SESSION_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.fallListLayout, ff, FALLS_LIST_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.commandLayout, scf, SESSION_COMMANDS_FRAGMENT_TAG).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.sessionDetailLayout, sdf, SESSION_DETAILS_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.fallListLayout, ff, FALLS_LIST_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.sessionGraphLayout, asf, ACTIVE_SESSION_FRAGMENT_TAG).commit();
                getFragmentManager().beginTransaction().replace(R.id.commandLayout, scf, SESSION_COMMANDS_FRAGMENT_TAG).commit();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui4, menu);
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


    @Override
    public void onFallClick(String sessionID, String fallID) {
        Intent fallDetails=new Intent(this,UI4.class);
        fallDetails.putExtra(UI4.ID_SESSION, sessionID);
        fallDetails.putExtra(UI4.ID_FALL, fallID);
        startActivity(fallDetails);
    }

    @Override
    public void aggiornaLista(int buttonPressed) {
        switch (buttonPressed) {
            case SessionCommandsFragment.BUTTON_PAUSE:
                ((ActiveSessionFragment)getFragmentManager().findFragmentByTag(ACTIVE_SESSION_FRAGMENT_TAG)).stopChrono();
                break;
            case SessionCommandsFragment.BUTTON_PLAY:
                ((ActiveSessionFragment)getFragmentManager().findFragmentByTag(ACTIVE_SESSION_FRAGMENT_TAG)).startChrono();
                break;
            case SessionCommandsFragment.BUTTON_STOP:
                ((ActiveSessionFragment)getFragmentManager().findFragmentByTag(ACTIVE_SESSION_FRAGMENT_TAG)).stopChrono();
                Intent i = new Intent(this, UI2.class);
                i.putExtra(UI2.SESSION_DATA_STAMP, thisData);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    public void onSessionNameInserted(String nuovonome, String data) {
        Controller.getInstance().renameEvent(data, nuovonome);
        //TODO aggiornare view dopo che il nome e' cambiato
    }

    @Override
    public void onFallDetected() {
        FallFragment ff=(FallFragment)getFragmentManager().findFragmentByTag(FALLS_LIST_FRAGMENT_TAG);
        if(ff!=null)
            ff.startlist();
    }
}


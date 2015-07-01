package org.tbw.FemurShield.Controller;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.Observer.Observable;
import org.tbw.FemurShield.Observer.Observer;
import org.tbw.FemurShield.R;

/**
 * Created by Marco on 27/06/2015.
 */

public class UI3Neo extends Activity implements EditSessionNameFragment.OnSessionNameInsertedListener,FallFragment.OnFallClickListener,SessionCommandsFragment.OnCommandUpdatedListener,Observer{

    private String thisData;
    private int layoutID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui3neo);

        int orientation=this.getResources().getConfiguration().orientation;
        thisData= SessionManager.getInstance().getActiveSession().getDataTime();

        SessionDetailsFragment sdf = SessionDetailsFragment.newIstance(thisData,SessionDetailsFragment.UI_3_MODE);
        ActiveSessionFragment asf= ActiveSessionFragment.newIstance();
        SessionCommandsFragment scf=SessionCommandsFragment.newInstance(SessionCommandsFragment.MODE_SMALL);
        FallFragment ff = FallFragment.newInstance(thisData);

        LinearLayout fragContainer = (LinearLayout) findViewById(R.id.ui3rootLayout);

        LinearLayout containerLayout=new LinearLayout(this);
        containerLayout.setId(View.generateViewId());
        LinearLayout firstLayout = new LinearLayout(this);
        firstLayout.setOrientation(LinearLayout.VERTICAL);
        firstLayout.setId(View.generateViewId());
        LinearLayout secondLayout = new LinearLayout(this);
        secondLayout.setOrientation(LinearLayout.VERTICAL);
        secondLayout.setId(View.generateViewId());

        if(orientation== Configuration.ORIENTATION_PORTRAIT)
        {
            containerLayout.setOrientation(LinearLayout.VERTICAL);
            containerLayout.addView(firstLayout);
            containerLayout.addView(secondLayout);
            getFragmentManager().beginTransaction().add(firstLayout.getId(),sdf, "sessionDetails").commit();
            getFragmentManager().beginTransaction().add(firstLayout.getId(),asf, "activeSessionGraph").commit();
            getFragmentManager().beginTransaction().add(secondLayout.getId(), scf, "sessionCommand").commit();
            getFragmentManager().beginTransaction().add(secondLayout.getId(), ff, "fallsList_ui3").commit();
        }
        else
        {
            containerLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams cllp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            containerLayout.setLayoutParams(cllp);
            containerLayout.setWeightSum(1);
            LinearLayout.LayoutParams fllp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.5f);
            firstLayout.setLayoutParams(fllp);
            LinearLayout.LayoutParams sllp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.5f);
            secondLayout.setLayoutParams(sllp);
            containerLayout.addView(firstLayout);
            containerLayout.addView(secondLayout);
            getFragmentManager().beginTransaction().add(firstLayout.getId(),sdf, "sessionDetails").commit();
            getFragmentManager().beginTransaction().add(firstLayout.getId(), ff, "fallsList_ui3").commit();
            getFragmentManager().beginTransaction().add(secondLayout.getId(),asf, "activeSessionGraph").commit();
            getFragmentManager().beginTransaction().add(secondLayout.getId(), scf, "sessionCommand").commit();
        }

        fragContainer.addView(containerLayout);
        
        Controller.getNotification().addObserver(this);
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

    public void aggiornaLista(){
        //do nothing
    }

    @Override
    public void aggiornaLista(int buttonPressed) {
        switch (buttonPressed) {
            case SessionCommandsFragment.BUTTON_PAUSE:
                ((ActiveSessionFragment)getFragmentManager().findFragmentByTag("activeSessionGraph")).stopChrono();
                break;
            case SessionCommandsFragment.BUTTON_PLAY:
                ((ActiveSessionFragment)getFragmentManager().findFragmentByTag("activeSessionGraph")).startChrono();
                break;
            case SessionCommandsFragment.BUTTON_STOP:
                ((ActiveSessionFragment)getFragmentManager().findFragmentByTag("activeSessionGraph")).stopChrono();
                Intent i = new Intent(this, UI2.class);
                i.putExtra(UI2.SESSION_DATA_STAMP, thisData);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    public void update(Observable oggettoosservato, Object o) {
        if(o instanceof Fall)
        {
            FallFragment ff=((FallFragment) getFragmentManager().findFragmentByTag("fallsList_ui3"));
                    if(ff!=null)
                        ff.startlist();
        }
    }

    @Override
    public void onSessionNameInserted(String nuovonome, String data) {
        Controller.getInstance().renameEvent(data, nuovonome);
        //TODO aggiornare view dopo che il nome è cambiato
    }
}


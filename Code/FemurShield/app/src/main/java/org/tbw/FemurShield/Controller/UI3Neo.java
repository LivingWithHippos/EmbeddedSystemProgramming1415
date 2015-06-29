package org.tbw.FemurShield.Controller;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.Observer.Observable;
import org.tbw.FemurShield.Observer.Observer;
import org.tbw.FemurShield.R;

/**
 * Created by Marco on 27/06/2015.
 */

public class UI3Neo extends BaseActivity implements FallFragment.OnFallClickListener,SessionCommandsFragment.OnCommandUpdatedListener,Observer{

    private String thisData;
    private int layoutID;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui3neo);

        LinearLayout fragContainer = (LinearLayout) findViewById(R.id.ui3rootLayout);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        layoutID=View.generateViewId();
        ll.setId(layoutID);

        thisData= SessionManager.getInstance().getActiveSession().getDataTime();

        SessionDetailsFragment sdf = SessionDetailsFragment.newIstance(thisData,SessionDetailsFragment.UI_3_MODE);
        ActiveSessionFragment asf= ActiveSessionFragment.newIstance();
        SessionCommandsFragment scf=SessionCommandsFragment.newInstance(SessionCommandsFragment.MODE_SMALL);
        FallFragment ff = FallFragment.newInstance(thisData);

        getFragmentManager().beginTransaction().add(ll.getId(),sdf, "sessionDetails").commit();
        getFragmentManager().beginTransaction().add(ll.getId(),asf, "activeSessionGraph").commit();
        getFragmentManager().beginTransaction().add(ll.getId(),scf, "sessionCommand").commit();
        getFragmentManager().beginTransaction().add(ll.getId(), ff, "fallsList_ui3").commit();

        fragContainer.addView(ll);
        
        Controller.getNotification().addObserver(this);
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
}


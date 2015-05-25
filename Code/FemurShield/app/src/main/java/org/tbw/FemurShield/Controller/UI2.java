package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.R;


public class UI2 extends Activity {

    private Session thisSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui2);
        String thisData = savedInstanceState.getString("sessiondatastamp");
        FragmentManager fm = getFragmentManager();
        SessionDetailsFragment sdf = (SessionDetailsFragment)fm.findFragmentById(R.id.SessionDetailsFragment);
        FallFragment ff = (FallFragment)fm.findFragmentById(R.id.FallFragment);
        sdf.setSession(thisData);
        ff.setSession(thisData);

        /*
        FallFragment ff = FallFragment.newInstance();
        SessionDetailsFragment sdf = SessionDetailsFragment.newIstance();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(ff, "cadutalista");
        ft.add(sdf, "dettaglicaduta");
        ft.commit();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        finish(); // chiude la UI2 quando premi il tasto back e ritorna alla chiamante (UI1)
    }
}

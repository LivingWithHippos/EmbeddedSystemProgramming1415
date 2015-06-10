package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.R;


public class UI2 extends Activity {

    private Session thisSession;
    public final static String SESSION_DATA_STAMP = "sessiondatastamp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui2);


        LinearLayout fragContainer = (LinearLayout) findViewById(R.id.ui2rootLayout);

        LinearLayout ll = new LinearLayout(this);
        int orientation=this.getResources().getConfiguration().orientation;
        ll.setOrientation(orientation== Configuration.ORIENTATION_PORTRAIT?LinearLayout.VERTICAL:LinearLayout.HORIZONTAL);

        ll.setId(View.generateViewId());


        String thisData = getIntent().getExtras().getString(SESSION_DATA_STAMP);
        SessionDetailsFragment sdf = SessionDetailsFragment.newIstance(thisData);
        FallFragment ff = FallFragment.newInstance(thisData);

        getFragmentManager().beginTransaction().add(ll.getId(),sdf, "sessionDetails").commit();
        getFragmentManager().beginTransaction().add(ll.getId(), ff, "FallDetails").commit();

        fragContainer.addView(ll);

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

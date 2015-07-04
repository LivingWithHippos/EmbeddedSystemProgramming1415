package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

import java.util.Random;

/**
 * La UI4 rappresenta i dettagli di una caduta specifica sul Fragment {@link FallDetailsFragment},
 * incluso un  grafico create tramite {@link FallBitmapCreator}
 * che mostra i dati rilevati dall'accelerometro.
 */
public class UI4 extends Activity {

    public static final String ID_FALL = "ID_CADUTA";
    public static final String ID_SESSION = "ID_SESSIONE";
    public static final String COLOR_PALETTE ="COLOR_PALETTE" ;
    private String fallID, sessionID;
    private Fall fall;
    private Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui4);
        rand=new Random();
        //reupero l'ID della sessione e dell caduta
        sessionID = getIntent().getExtras().getString(ID_SESSION);
        fallID = getIntent().getExtras().getString(ID_FALL);
        //creo il fragment
        FallDetailsFragment fdf = FallDetailsFragment.newInstance(sessionID, fallID,ColorsPicker.pickRandomColors());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.ui4rootLayout, fdf, "mFallDetailsFragment");
        fragmentTransaction.commit();

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
                if (SessionManager.getInstance().getActiveSession() != null) {
                    Intent ui3 = new Intent(this, UI3.class);
                    startActivity(ui3);
                } else
                    Toast.makeText(this, getString(R.string.no_active_session), Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

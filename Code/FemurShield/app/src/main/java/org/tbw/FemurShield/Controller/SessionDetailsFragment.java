package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;

import java.util.ArrayList;

/**
 * Created by Vianello on 21/05/15.
 * mostra detagli: nome sessione - data - durata
 * e sotto la signature della sessione
 *
 * usata nella UI2
 */
public class SessionDetailsFragment extends Fragment
{
    private Session session;

    public SessionDetailsFragment()
    {
    }

    public static SessionDetailsFragment newIstance()
    {
        SessionDetailsFragment fragment = new SessionDetailsFragment();
        return fragment;
    }

    @Override
    public void onAttach (Activity activity)
    {
        // TODO dalla UI2 prende i dati della sessione selezionata da UI1,
        // TODO che mostra tutte le sessioni, passare tramite intent?
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        startDetails();
    }

    public void setSession(String date)
    {
        ArrayList<Session> s = SessionManager.getInstance().getAllSessions();
        if(s!= null)
        {
            for(Session sex : s)
            {
                if(sex.getDataTime().equalsIgnoreCase(date))
                {
                    session = sex;
                    break;
                }
            }
        }
    }

    private void startDetails()
    {
        if(session!=null)
        {
            //TODO inizializzare i valori con findwibyid ecc...
        }
    }
}

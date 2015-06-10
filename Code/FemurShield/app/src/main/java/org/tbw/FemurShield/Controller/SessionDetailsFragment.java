package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Vianello on 21/05/15.
 * mostra detagli:
 * nome sessione - data e ora
 * durata
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String thisData = savedInstanceState.getString(UI2.SESSION_DATA_STAMP);
        setSession(thisData);
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
            TextView tvNome = (TextView) getView().findViewById(R.id.tvSessionName); // è sia nome che data
            TextView tvDurata = (TextView) getView().findViewById(R.id.tvSessionDuration);
            ImageView ivGrafico = (ImageView) getView().findViewById(R.id.ivGraficoSessione);

            tvNome.setText(session.getName());
            tvDurata.setText("durata session");
            ivGrafico.setImageBitmap(session.getSignature().toBitmap());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_details, container,false);
        return rootView;
    }
}

package org.tbw.FemurShield.Controller;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

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

    public static SessionDetailsFragment newIstance(String datatime)
    {
        SessionDetailsFragment fragment = new SessionDetailsFragment();
        Bundle b = new Bundle();
        b.putString(UI2.SESSION_DATA_STAMP, datatime);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String thisData = getArguments().getString(UI2.SESSION_DATA_STAMP);
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
            long timeElapsed=session.getDuration();
            int hours = (int) (timeElapsed / 3600000);
            String h=((""+hours).length()==1)?"0"+hours: ""+hours;
            int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
            String m=((""+minutes).length()==1)?"0"+minutes: ""+minutes;
            int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;
            String se=((""+seconds).length()==1)?"0"+seconds: ""+seconds;
            String durata=""+h+":"+m+":"+se;
            tvDurata.setText(durata);
            ivGrafico.setImageBitmap(session.getSignature().toBitmap());

            getView().invalidate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_details, container,false);
        return rootView;
    }
}

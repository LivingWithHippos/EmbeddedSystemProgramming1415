package org.tbw.FemurShield.Controller;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
    private boolean ui_mode;
    private String thisData;

    public final static String UI_MODE="signature";
    public final static boolean UI_2_MODE=true;
    public final static boolean UI_3_MODE=false;

    private ImageView ivGrafico;
    private TextView tvNome;

    public SessionDetailsFragment()
    {
    }

    public static SessionDetailsFragment newIstance(String datatime,boolean ui_mode)
    {
        SessionDetailsFragment fragment = new SessionDetailsFragment();
        Bundle b = new Bundle();
        b.putString(UI2.SESSION_DATA_STAMP, datatime);
        b.putBoolean(UI_MODE, ui_mode);
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
        thisData = getArguments().getString(UI2.SESSION_DATA_STAMP);
        ui_mode=getArguments().getBoolean(UI_MODE);
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
            tvNome = (TextView) getView().findViewById(R.id.tvSessionName); // è sia nome che data
            TextView tvDurata = (TextView) getView().findViewById(R.id.tvSessionDuration);
            ivGrafico = (ImageView) getView().findViewById(R.id.ivGraficoSessione);

            tvNome.setText(session.getName());
            if(ui_mode==UI_2_MODE)
            {
                long timeElapsed=session.getDuration();
                int hours = (int) (timeElapsed / 3600000);
                String h=((""+hours).length()==1)?"0"+hours: ""+hours;
                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
                String m=((""+minutes).length()==1)?"0"+minutes: ""+minutes;
                int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;
                String se=((""+seconds).length()==1)?"0"+seconds: ""+seconds;
                String durata=""+h+":"+m+":"+se;
                tvDurata.setText(durata);
                Bitmap temp;
                if((temp=BitmapCache.getInstance().getBitmapFromMemCache(thisData))!=null)
                    ivGrafico.setImageBitmap(temp);
                else
                    loadSessionBitmap(R.id.ivGraficoSessione);
            }
            else {
                tvDurata.setVisibility(View.GONE);
                ivGrafico.setVisibility(View.GONE);
            }
            getView().invalidate();
        }
    }

    public void loadSessionBitmap(int resId) {
        SignatureLoaderTask slt = new SignatureLoaderTask(ivGrafico,thisData);
        slt.execute(resId);
    }

    public void updateNameView(String nome)
    {
        if(tvNome!=null) {
            tvNome.setText(nome);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_session_details, container,false);
        return rootView;
    }
}

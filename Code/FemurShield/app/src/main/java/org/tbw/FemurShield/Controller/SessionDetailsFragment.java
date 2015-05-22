package org.tbw.FemurShield.Controller;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vianello on 21/05/15.
 * mostra detagli: nome sessione - data - durata
 * e sotto la signature della sessione
 *
 * usata nella UI2
 */
public class SessionDetailsFragment extends Fragment
{
    public SessionDetailsFragment()
    {
    }

    public static SessionDetailsFragment newIstance()
    {
        SessionDetailsFragment fragment = new SessionDetailsFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        startDetails();
    }

    private void startDetails()
    {
        //TODO metodo che inizializza i dettagli della sessione
    }

    private static class ViewHolder
    {
        TextView tvName;
        TextView tvdate;
        TextView tvDuration;
        ImageView ivGraphic; // lo so è una signature, ho messo così che non da errore
    }
}

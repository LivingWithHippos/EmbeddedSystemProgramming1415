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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment che mostra i dettagli: nome sessione, data e ora, durata e la signature della sessione, usata nella {@link UI2}
 *
 * @author Luca Vianello
 */

public class SessionDetailsFragment extends Fragment {
    private static final String TAG = "SessionDetailsFragment";
    private Session session;
    private boolean ui_mode;
    private String thisData;

    public final static String UI_MODE = "signature";
    public final static boolean UI_2_MODE = true;
    public final static boolean UI_3_MODE = false;

    private String datePattern = "dd/MM/yyyy";
    private String hourPattern = "HH:mm:ss";
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat hourFormat;
    private SimpleDateFormat sdf;

    private ImageView ivGrafico;
    private TextView tvNome;
    private TextView tvData;
    private TextView tvOra;
    private TextView tvDurata;

    public SessionDetailsFragment() {
    }

    /**
     * Metodo per ottenere una nuova istanza del Fragment
     *
     * @param datatime il timestamp della sessione
     * @param ui_mode  se il fragment deve essere mostrato nella UI 2 o 3
     * @return una nuova istanza del Fragment
     */
    public static SessionDetailsFragment newIstance(String datatime, boolean ui_mode) {
        SessionDetailsFragment fragment = new SessionDetailsFragment();
        Bundle b = new Bundle();
        b.putString(UI2.SESSION_DATA_STAMP, datatime);
        b.putBoolean(UI_MODE, ui_mode);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateFormat = new SimpleDateFormat(datePattern);
        hourFormat = new SimpleDateFormat(hourPattern);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        thisData = getArguments().getString(UI2.SESSION_DATA_STAMP);
        ui_mode = getArguments().getBoolean(UI_MODE);
        if (!thisData.equalsIgnoreCase(UI1.SESSION_EMPTY)) {
            setSession(thisData);
            startDetails();
        }
    }

    /**
     * Imposta la sessione da rappresentare
     *
     * @param date l'ID della sessione da recuperare
     */
    public void setSession(String date) {
        ArrayList<Session> s = SessionManager.getInstance().getAllSessions();
        if (s != null) {
            for (Session sex : s) {
                if (sex.getDataTime().equalsIgnoreCase(date)) {
                    session = sex;
                    thisData = date;
                    break;
                }
            }
        }
        if (session == null)
            Log.e(TAG, getString(R.string.no_session_found));
    }

    /**
     * Inizializza il testo e le immagini del fragment
     */
    public void startDetails() {
        if (session != null) {
            tvNome = (TextView) getView().findViewById(R.id.tvSessionName);
            tvData = (TextView) getView().findViewById(R.id.tvSessionDate);
            tvOra = (TextView) getView().findViewById(R.id.tvSessionTime);
            tvDurata = (TextView) getView().findViewById(R.id.tvSessionDuration);
            ivGrafico = (ImageView) getView().findViewById(R.id.ivGraficoSessione);
            if (!thisData.equalsIgnoreCase(UI1.SESSION_EMPTY)) {

                tvNome.setVisibility(View.VISIBLE);
                tvData.setVisibility(View.VISIBLE);
                tvOra.setVisibility(View.VISIBLE);
                tvDurata.setVisibility(View.VISIBLE);
                ivGrafico.setVisibility(View.VISIBLE);

                tvNome.setText(session.getName());

                try {
                    sdf = new SimpleDateFormat(Session.datePattern);
                    Date timestamp = sdf.parse(session.getDataTime());
                    tvData.setText("Iniziata il " + dateFormat.format(timestamp) + " ");
                    tvOra.setText("alle ore " + hourFormat.format(timestamp));
                } catch (ParseException e) {
                    Log.e(TAG, "Errore recupero data sessione ");
                }

                if (ui_mode == UI_2_MODE) {
                    long timeElapsed = session.getDuration();
                    int hours = (int) (timeElapsed / 3600000);
                    String h = (("" + hours).length() == 1) ? "0" + hours : "" + hours;
                    int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
                    String m = (("" + minutes).length() == 1) ? "0" + minutes : "" + minutes;
                    int seconds = (int) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;
                    String se = (("" + seconds).length() == 1) ? "0" + seconds : "" + seconds;
                    String durata = "" + h + ":" + m + ":" + se;
                    tvDurata.setText("Durata Sessione: " + durata);

                    Bitmap temp;
                    if ((temp = BitmapCache.getInstance().getBitmapFromMemCache(thisData)) != null)
                        ivGrafico.setImageBitmap(temp);
                    else
                        loadSessionBitmap(R.id.ivGraficoSessione);
                } else {
                    tvDurata.setVisibility(View.GONE);
                    ivGrafico.setVisibility(View.GONE);
                }
            } else {
                tvNome.setVisibility(View.INVISIBLE);
                tvData.setVisibility(View.INVISIBLE);
                tvOra.setVisibility(View.INVISIBLE);
                tvDurata.setVisibility(View.INVISIBLE);
                ivGrafico.setVisibility(View.INVISIBLE);
            }
            getView().invalidate();
        }
    }

    /**
     * Carica dalla cache o dal disco l'immagine della sessione
     */
    public void loadSessionBitmap(int resId) {
        SignatureLoaderTask slt = new SignatureLoaderTask(ivGrafico, thisData);
        slt.execute(resId);
    }

    /**
     * Metodo che aggiorna il nome della sessione da mostrare
     *
     * @param nome il nuovo nome
     */
    public void updateNameView(String nome) {
        if (tvNome != null) {
            tvNome.setText(nome);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_session_details, container, false);
        return rootView;
    }
}

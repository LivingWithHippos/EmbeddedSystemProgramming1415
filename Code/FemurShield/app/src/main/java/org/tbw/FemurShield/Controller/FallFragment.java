package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment che contiene la lista di cadute di una sessione
 *
 * @author Luca Vianello
 */
public class FallFragment extends ListFragment {
    private List<FallListItem> fItems;
    private FallListAdapter fAdapter;
    private ArrayList<Fall> falls;
    private Session session;

    private OnFallClickListener mListener;

    public FallFragment() {
    }

    /**
     * Metodo per creare una nuova istanza del Fragment
     * @param datatime la sessione da cui prendere le cadute
     * @return una nuova istanza della classe
     */
    public static FallFragment newInstance(String datatime) {
        FallFragment fragment = new FallFragment();
        Bundle b = new Bundle();
        b.putString(UI2.SESSION_DATA_STAMP, datatime);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String thisData = getArguments().getString(UI2.SESSION_DATA_STAMP);
        if (!thisData.equalsIgnoreCase(UI1.SESSION_EMPTY)) {
            setSession(thisData);
            startlist();
        }
    }

    /**
     * Gestisce il click su una caduta presente nella lista
     * @param position la posizione nella lista dell'elemento premuto
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        FallListItem fall = (FallListItem) l.getItemAtPosition(position);
        if (isAdded())
            mListener.onFallClick(session.getDataTime(), fall.date);
    }

    /**
     * Imposta la sessione da rappresentare
     * @param date l'ID della sessione da recuperare
     */
    public void setSession(String date) {
        ArrayList<Session> s = SessionManager.getInstance().getAllSessions();
        if (s != null) {
            for (Session sex : s) {
                if (sex.getDataTime().equalsIgnoreCase(date)) {
                    session = sex;
                    return;
                }
            }
        }
        if(session==null)
            Log.e("FallFragment", getString(R.string.no_session_found));
    }

    /**
     * Inizializza la lista di cadute
     */
    public void startlist() {
        if (session != null) {
            fItems = new ArrayList<>();
            falls = session.getFalls();
            int fallIndex = 1;
            if (session.getFallsNumber() > 0) {
                for (int i = 0; i < session.getFallsNumber(); i++) {
                    String n = "#." + (fallIndex++);
                    String d = falls.get(i).getData();
                    boolean s = falls.get(i).isReported();

                    fItems.add(new FallListItem(n, d, s));
                }
                fAdapter = new FallListAdapter(getActivity(), fItems);
                setListAdapter(fAdapter);
            } else {
                if (fAdapter != null)
                    fAdapter.clear();
            }
        } else {
            if (fAdapter != null)
                fAdapter.clear();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fall, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFallClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnFallClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFallClickListener {
        /**
         * Metodo che indica all'activity che è stata premuta una caduta
         * @param sessionID l'ID della sessione
         * @param fallID l'ID della caduta
         */
        public void onFallClick(String sessionID, String fallID);
    }
}

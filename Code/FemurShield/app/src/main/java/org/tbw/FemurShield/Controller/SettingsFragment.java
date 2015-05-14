package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.ListFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.tbw.FemurShield.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe Fragment che implementa l'interfaccia
 * {@link SettingsFragment.OnFragmentInteractionListener}
 * che gestisce l'interazione con le voci della lista.
 */
public class SettingsFragment extends ListFragment {

    private List<SettingListItem> mItems;
    private PreferencesEditor prefs;
    private OnFragmentInteractionListener mListener;
    private SettingListAdapter mAdapter;



    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = new ArrayList<SettingListItem>();
        //mi serve per ottenere le icone
        Resources resources = getResources();
        //TODO: impostare icone di dimensioni diverse a seconda della dimensione dello schermo?
        // prefs serve a caricare le impostazioni salvate
        prefs=new PreferencesEditor(getActivity());
        //aggiungo le voci alla lista
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.frequency),
                getString(R.string.title_sample_rate),
                getString(R.string.description_sample_rate)));
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.duration),
                getString(R.string.title_session_duration),
                getString(R.string.description_session_duration)));
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.email),
                getString(R.string.title_email_recipient),
                getString(R.string.description_email_recipient)));
        int hour=prefs.getAlarmHour();
        int minute=prefs.getAlarmMinute();
        //per stampare i numeri nel formato 05:07 invece di 5:7
        DecimalFormat formatter=new DecimalFormat("00");
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.alarm),
                getString(R.string.title_alarm),
                "Sveglia impostata per le "+formatter.format(hour)+":"+formatter.format(minute)));
        //imposto l'adapter
        mAdapter=new SettingListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // tolgo i divisori tra le voci
        getListView().setDivider(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // recupera l'elemento cliccato
        SettingListItem item = mItems.get(position);

        if(mListener!=null&item!=null)
            mListener.onVoiceSelected(item);
    }

    public void updateAlarmTime(int hour,int minute)
    {
        for(SettingListItem s:mItems)
        {
            if(s.title.equalsIgnoreCase(getString(R.string.title_alarm)))
            {

                //per stampare i numeri nel formato 05:07 invece di 5:7
                DecimalFormat formatter=new DecimalFormat("00");
                // aggiorna l'ora dell'allarme da mostrare nella descrizione
                s.setDescription(getString(R.string.description_alarm_set_to) + " " + formatter.format(hour) + ":" + formatter.format(minute));
                // dice alla lista di aggiornarsi dopo il cambiamento
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void updateSessionDuration(int newDuration)
    {
        for(SettingListItem s:mItems)
        {
            if(s.title.equalsIgnoreCase(getString(R.string.title_session_duration)))
            {
                // aggiorna il sampling rate nella descrizione
                s.setDescription(newDuration+(newDuration==1?" ora":" ore"));
                // dice alla lista di aggiornarsi dopo il cambiamento
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void updateSamplingRate(int newRate)
    {
        for(SettingListItem s:mItems)
        {
            if(s.title.equalsIgnoreCase(getString(R.string.title_sample_rate)))
            {
                // aggiorna il sampling rate nella descrizione
                s.setDescription(getString(R.string.description_sample_rate_set_to)+" "+newRate+"%");
                // dice alla lista di aggiornarsi dopo il cambiamento
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener { 
        public void onVoiceSelected(SettingListItem s);
    }

}

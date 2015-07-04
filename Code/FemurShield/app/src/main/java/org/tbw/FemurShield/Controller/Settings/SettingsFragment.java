package org.tbw.FemurShield.Controller.Settings;

import android.app.Activity;
import android.app.ListFragment;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.tbw.FemurShield.Controller.PreferencesEditor;
import org.tbw.FemurShield.Controller.Reminder.BootReceiver;
import org.tbw.FemurShield.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe Fragment che implementa l'interfaccia
 * {@link SettingsFragment.OnOptionSelectedListener}
 * che gestisce l'interazione con le voci della lista.
 */
public class SettingsFragment extends ListFragment {

    private List<SettingListItem> mItems;
    private PreferencesEditor prefs;
    private OnOptionSelectedListener mListener;
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
        int newRate=prefs.getSamplingRate();
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.frequency),
                getString(R.string.title_sample_rate),
                getString(R.string.description_sample_rate_set_to)+" "+newRate+"%"));

        int newDuration=prefs.getSessionDuration();
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.duration),
                getString(R.string.title_session_duration),
                newDuration+(newDuration==1?" ora":" ore")));


        int temp=prefs.getEmailContactsNumber();
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.email),
                getString(R.string.title_email_recipient),
                temp+" "+(temp==1?"contatto presente":"contatti presenti")));

        String alarmMessage;
        if(isAlarmEnabled()) {
            //per stampare i numeri nel formato 05:07 invece di 5:7
            DecimalFormat formatter = new DecimalFormat("00");
            String hour = formatter.format(prefs.getAlarmHour());
            String minute = formatter.format(prefs.getAlarmMinute());
            alarmMessage=getString(R.string.description_alarm_set_to) + " " + hour + ":" + minute;
        }else
            alarmMessage=getString(R.string.alarm_disabled_message);

        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.alarm),
                getString(R.string.title_alarm),alarmMessage));

        //TODO da togliere alla fine
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.fall),
                "Simula Caduta", "Simula caduta per motivi di debug"));

        //imposto l'adapter
        mAdapter=new SettingListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);
    }

    private boolean isAlarmEnabled()
    {
        ComponentName receiver = new ComponentName(getActivity(), BootReceiver.class);
        PackageManager pm = getActivity().getPackageManager();
        int result=pm.getComponentEnabledSetting(receiver);
        switch(result)
        {
            case PackageManager.COMPONENT_ENABLED_STATE_ENABLED: return true;
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED: return false;
            case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT: return false;
            default: return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for(SettingListItem s:mItems)
        {
            if(s.title.equalsIgnoreCase(getString(R.string.title_email_recipient)))
            {

                //aggiorna la descrizione email
                int temp=prefs.getEmailContactsNumber();
                s.setDescription(temp+" "+(temp==1?"contatto presente":"contatti presenti"));
                // dice alla lista di aggiornarsi dopo il cambiamento
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
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
            mListener = (OnOptionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnOptionSelectedListener");
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
            mListener.onOptionSelected(item);
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
    public interface OnOptionSelectedListener {
        public void onOptionSelected(SettingListItem s);
    }

}

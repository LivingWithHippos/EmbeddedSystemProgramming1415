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
 * Fragment che contiene la lista delle opzioni e ne gestisce gli effetti,
 * usa l'adapter {@link SettingListAdapter}
 */
public class SettingsFragment extends ListFragment {

    private List<SettingListItem> mItems;
    private PreferencesEditor prefs;
    private OnOptionSelectedListener mListener;
    private SettingListAdapter mAdapter;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = new ArrayList<SettingListItem>();
        //mi serve per ottenere le icone
        Resources resources = getResources();

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


        //imposto l'adapter
        mAdapter=new SettingListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);
    }

    /**
     * Controlla che il servizio di allarme sia attivo.
     * @return true se e' attivo, false altrimenti
     */
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
                    + " deve implementare OnOptionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // tolgo i divisori tra le voci
        getListView().setDivider(null);
    }

    /**
     * Rileva il click su un opzione e lo gestisce tramite callback
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // recupera l'elemento cliccato
        SettingListItem item = mItems.get(position);

        if(mListener!=null&item!=null)
            mListener.onOptionSelected(item);
    }

    /**
     * Aggiorna la descrizione dell'allarme dopo una sua modifica
     * @param hour l'ora dell'allarme impostato
     * @param minute i minuti dell'allarme impostato
     */
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

    /**
     * Aggiorna la descrizione della durata sessione
     * @param newDuration la nuova durata massima delle sessioni
     */
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

    /**
     * Aggiorna la descrizione del sampling rate
     * @param newRate la nouva frequenza di campionamento
     */
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

    public interface OnOptionSelectedListener {
        /**
         * Gestisce la voce premuta
         * @param s la voce premuta
         */
        public void onOptionSelected(SettingListItem s);
    }

}

package org.tbw.FemurShield.Controller.Settings;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import org.tbw.FemurShield.Controller.PreferencesEditor;
import org.tbw.FemurShield.R;

/**
 * Created by Marco on 03/05/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private PreferencesEditor prefs;
    private OnAlarmChangedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // inizializzo l'oggetto per salvare e leggere i valori dell'orologio
        prefs=new PreferencesEditor(getActivity());
        //carico i valori precedenti o imposto l'ora di default alle 9:00 se non presente
        int hour=prefs.getAlarmHour();
        int minute=prefs.getAlarmMinute();
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), R.style.AppDialogTheme, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAlarmChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAlarmChangedListener");
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        // salva l'orario selezionato
        prefs.setAlarmHour(hourOfDay);
        prefs.setAlarmMinute(minute);

        mCallback.OnAlarmChanged(hourOfDay, minute);
    }

    public interface OnAlarmChangedListener{

        public void OnAlarmChanged(int hourOfDay, int minute);
    }
}

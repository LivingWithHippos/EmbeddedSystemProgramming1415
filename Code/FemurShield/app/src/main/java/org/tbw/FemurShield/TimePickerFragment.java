package org.tbw.FemurShield;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Created by Marco on 03/05/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private SharedPreferences prefs;
    private OnAlarmChangedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // inizializzo l'oggetto per salvare e leggere i valori dell'orologio
        prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
        //carico i valori precedenti o imposto l'ora di default alle 9:00 se non presente
        int hour=prefs.getInt("alarm_hour",9);
        int minute=prefs.getInt("alarm_minute",0);
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),R.style.AppDialogTheme, this, hour, minute,
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

        mCallback.OnAlarmChanged(hourOfDay, minute);
    }

    public interface OnAlarmChangedListener{

        public void OnAlarmChanged(int hourOfDay, int minute);
    }
}

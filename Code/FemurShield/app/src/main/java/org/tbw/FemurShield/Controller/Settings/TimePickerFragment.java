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
 * Dialog che permette la scelta di un orario per l'allarme e ne notifica il cambiamento tramite callback
 *
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

        try {
            mCallback = (OnAlarmChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnAlarmChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        // salva l'orario selezionato
        prefs.setAlarmHour(hourOfDay);
        prefs.setAlarmMinute(minute);

        mCallback.OnAlarmChanged(hourOfDay, minute);
    }

    public interface OnAlarmChangedListener{

        /**
         * Indica all'activity che l'orario dell'allarme e' stato modificato
         * @param hourOfDay l'ora dell'allarme
         * @param minute il minuto dell'allarme
         */
        public void OnAlarmChanged(int hourOfDay, int minute);
    }
}

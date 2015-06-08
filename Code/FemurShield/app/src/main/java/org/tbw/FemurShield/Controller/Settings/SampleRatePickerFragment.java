package org.tbw.FemurShield.Controller.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import org.tbw.FemurShield.Controller.PreferencesEditor;
import org.tbw.FemurShield.R;

/**
 * Created by Marco on 04/05/2015.
 * Fragment che contiene lo slider
 * per determinare la frequenza di campionamento
 */
public class SampleRatePickerFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private PreferencesEditor prefs;
    private OnSamplingRateChangedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // inizializzo l'oggetto per salvare e leggere i valori dell'orologio
        prefs=new PreferencesEditor(getActivity());
        //carico i valori precedenti o imposto a meta
        //TODO vedere quale e un valore di default adatto
        int progress=prefs.getSamplingRate();
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        final Context themeContext = getActivity();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.seekbar_picker_dialog, null);
        //reimposta la seekbar alla posizione dell ultima volta
        SeekBar seekBar=(SeekBar)view.findViewById(R.id.seekBar);
        if (seekBar != null) {
            seekBar.setProgress(prefs.getSamplingRate());
        }
        alert.setView(view);
        alert.setPositiveButton(getString(R.string.ok), this);
        alert.setNegativeButton(getString(R.string.cancel), this);
        return alert.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSamplingRateChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSamplingRateChangedListener");
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:

                SeekBar seekBar=(SeekBar)getDialog().findViewById(R.id.seekBar);
                if (seekBar != null) {
                    int progress=seekBar.getProgress();
                    prefs.setSamplingRate(progress);
                    mCallback.onSamplingRateChanged(progress);
                }
                break;
        }
    }

    public interface OnSamplingRateChangedListener {
        void onSamplingRateChanged(int newRate);
    }
}

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
 * @author Marco Biasin
 *         <p>
 *         Fragment che contiene lo slider per decidere la frequenza di campionamento del sensore.
 */
public class SampleRatePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private PreferencesEditor prefs;
    private OnSamplingRateChangedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // inizializzo l'oggetto per salvare e leggere i valori dell'orologio
        prefs = new PreferencesEditor(getActivity());
        //carico i valori precedenti o imposto a meta
        int progress = prefs.getSamplingRate();
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        final Context themeContext = getActivity();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.seekbar_picker_dialog, null);
        //reimposta la seekbar alla posizione dell ultima volta
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
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

        try {
            mCallback = (OnSamplingRateChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnSamplingRateChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     * Gestisce il click sul bottone OK o ANNULLA
     *
     * @param dialog
     * @param which  il bottone premuto
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:

                SeekBar seekBar = (SeekBar) getDialog().findViewById(R.id.seekBar);
                if (seekBar != null) {
                    int progress = seekBar.getProgress();
                    prefs.setSamplingRate(progress);
                    mCallback.onSamplingRateChanged(progress);
                }
                break;
        }
    }

    public interface OnSamplingRateChangedListener {
        /**
         * notifica l'activity che e' stata impostata una nuova frequenza di campionamento
         *
         * @param newRate la nuova frequenza di campionamento
         */
        void onSamplingRateChanged(int newRate);
    }
}

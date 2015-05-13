package org.tbw.FemurShield.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 04/05/2015.
 * Fragment che contiene lo slider
 * per determinare la frequenza di campionamento
 */
public class SliderFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private SharedPreferences prefs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // inizializzo l'oggetto per salvare e leggere i valori dell'orologio
        prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
        //carico i valori precedenti o imposto a meta
        //TODO vedere quale e un valore di default adatto
        int progress=prefs.getInt("sample_rate",50);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        final Context themeContext = getActivity();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.seekbar_picker_dialog, null);
        //reimposta la seekbar alla posizione dell ultima volta
        SeekBar seekBar=(SeekBar)view.findViewById(R.id.seekBar);
        if (seekBar != null) {
            prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
            seekBar.setProgress(prefs.getInt("sample_rate",50));
        }
        alert.setView(view);
        alert.setPositiveButton(getString(R.string.ok), this);
        alert.setNegativeButton(getString(R.string.cancel), this);
        return alert.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:

                SeekBar seekBar=(SeekBar)getDialog().findViewById(R.id.seekBar);
                if (seekBar != null) {
                    int progress=seekBar.getProgress();
                    prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("sample_rate", progress);
                    editor.commit();
                }
                break;
        }
    }
}

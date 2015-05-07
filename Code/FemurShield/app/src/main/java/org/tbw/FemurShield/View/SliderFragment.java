package org.tbw.FemurShield.View;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 04/05/2015.
 * Fragment che contiene lo slider
 * per determinare la frequenza di campionamento
 */
public class SliderFragment extends DialogFragment implements SliderDialog.OnProgressSetListener{

    private SharedPreferences prefs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // inizializzo l'oggetto per salvare e leggere i valori dell'orologio
        prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
        //carico i valori precedenti o imposto a meta
        //TODO vedere quale e un valore di default adatto
        int progress=prefs.getInt("sample_rate",50);
        return new SliderDialog(getActivity(), R.style.AppDialogTheme,this,progress);
    }

    @Override
    public void onProgressSet(SeekBar view, int progress) {
        prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("sample_rate", progress);
        editor.commit();
    }


}

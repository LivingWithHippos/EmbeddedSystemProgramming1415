package org.tbw.FemurShield;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.text.InputType;

/**
 * Created by Marco on 05/05/2015.
 */
public class DurationFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private SharedPreferences prefs;
    private EditText mEditText;
    private OnDurationChangedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.AppDialogTheme);
        alert.setTitle("Durata Sessione");
        //alert.setView(R.layout.duration_picker_dialog);
        mEditText = new EditText(getActivity());
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEditText.setMaxLines(1);
        mEditText.setHint(getString(R.string.et_duration_hint));
        mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2)});
        mEditText.setPadding(convertDpToPx(10, mEditText), convertDpToPx(10, mEditText), convertDpToPx(10,mEditText), convertDpToPx(10,mEditText));
        alert.setView(mEditText);
        alert.setPositiveButton(getString(R.string.ok), this);
        alert.setNegativeButton(getString(R.string.cancel), this);
        return alert.create();
    }

    //Callback
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (mCallback != null) {
                    String temp=mEditText.getText().toString();
                    if(temp!=null) {
                        int duration=getActivity().getPreferences(Context.MODE_PRIVATE).getInt("session_duration",12);
                        try {
                            duration = Integer.parseInt(temp.trim());
                        }catch(NumberFormatException nfe){
                                Log.e("FemurShield","Errore di conversione string a int");
                            }

                            if(duration<1)
                                duration=1;
                            if(duration>24)
                                duration=24;
                            mCallback.onDurationChanged(duration);
                    }
                }
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDurationChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDurationChangedListener");
        }
    }

    private int convertDpToPx(int dp, View view) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return Math.round(pixels);
    }

    /**
     * Interfaccia di callback per quando l'utente ha finito premendo il bottone ok
     */
    public interface OnDurationChangedListener {

        /**
         * @param newDuration The view associated with this listener.
         */
        void onDurationChanged(int newDuration);
    }
}

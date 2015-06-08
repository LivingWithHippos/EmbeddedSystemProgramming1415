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
import android.widget.EditText;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 08/05/2015.
 */
public class AddContactFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private OnUserInsertedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCallback=(OnUserInsertedListener)getActivity();

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        final Context themeContext = getActivity();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.fragment_add_contact, null);
        alert.setView(view);
        alert.setPositiveButton(getString(R.string.ok), this);
        alert.setNegativeButton(getString(R.string.cancel), this);
        return alert.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                if (mCallback != null) {
                    EditText nome=(EditText)getDialog().findViewById(R.id.etEmailName);

                    EditText indirizzo=(EditText)getDialog().findViewById(R.id.etEmailAddress);
                    mCallback.onUserInserted(nome,indirizzo);
                }
                break;
        }
    }

    /* Questo metodo controlla che l'attivita' abbia implementato l'interfaccia per il callback*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnUserInsertedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUserInsertedListener");
        }
    }

    /**
     * Interfaccia di callback per quando l'utente ha finito premendo il bottone ok
     */
    public interface OnUserInsertedListener {

        /**
         * @param nome EditText in cui e stato inserito il nome
         * @param indirizzo EditText in cui e stato inserita l email
         */
        void onUserInserted(EditText nome,EditText indirizzo);
    }
}

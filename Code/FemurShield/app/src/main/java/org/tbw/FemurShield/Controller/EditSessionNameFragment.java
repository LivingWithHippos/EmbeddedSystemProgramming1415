package org.tbw.FemurShield.Controller;

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


public class EditSessionNameFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private OnSessionNameInsertedListener mCallback;
    public final static String SESSION_DATA="session_data";
    private String sessiondata="";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCallback=(OnSessionNameInsertedListener)getActivity();

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        final Context themeContext = getActivity();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.fragment_edit_session_name, null);
        alert.setView(view);
        alert.setPositiveButton(getString(R.string.ok), this);
        alert.setNegativeButton(getString(R.string.cancel), this);

        Bundle bundle=getArguments();
        sessiondata=bundle.getString(SESSION_DATA);
        return alert.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                if (mCallback != null) {
                    EditText nome = (EditText) getDialog().findViewById(R.id.etSessionName);
                    mCallback.onSessionNameInserted(nome.getText().toString(),sessiondata);
                }
                break;
        }
    }

    /* Questo metodo controlla che l'attivita' abbia implementato l'interfaccia per il callback*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnSessionNameInsertedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSessionNameInsertedListener");
        }
    }

    /**
     * Interfaccia di callback per quando l'utente ha finito premendo il bottone ok
     */
    public interface OnSessionNameInsertedListener {

        /**
         * @param nuovonome EditText in cui e stato inserito il nome
         */
        void onSessionNameInserted(String nuovonome, String data);
    }

}

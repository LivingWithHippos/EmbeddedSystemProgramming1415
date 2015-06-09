package org.tbw.FemurShield.Controller.Settings;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 08/05/2015.
 */
public class AddContactFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private OnUserInsertedListener mCallback;
    private OnUserToUpdateInsertedListener mUpdateCallback;
    public final static String CONTACT_MODE="contact_mode";
    public final static String OLD_EMAIL="old_email";
    public final static String OLD_NAME="old_name";
    public final static boolean MODE_NEW_USER=true;
    public final static boolean MODE_EDIT_USER=false;
    private boolean mode;

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

        Bundle bundle=getArguments();
        mode=bundle.getBoolean(CONTACT_MODE);
        if(mode==MODE_EDIT_USER)
        {
            //TODO: stiamo modificando un utente
            String oldName=bundle.getString(OLD_NAME);
            String oldEmail=bundle.getString(OLD_EMAIL);
            TextView title=(TextView)view.findViewById(R.id.tvAddContactTitle);
            title.setText(getString(R.string.edit_contact_dialog_title));
            EditText etName=(EditText)view.findViewById(R.id.etEmailName);
            etName.setText(oldName);
            EditText etAddress=(EditText)view.findViewById(R.id.etEmailAddress);
            etAddress.setText(oldEmail);
        }
        return alert.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                if(mode==MODE_NEW_USER) {
                    if (mCallback != null) {
                        EditText nome = (EditText) getDialog().findViewById(R.id.etEmailName);
                        EditText indirizzo = (EditText) getDialog().findViewById(R.id.etEmailAddress);
                        mCallback.onUserInserted(nome, indirizzo);
                    }
                }
                else
                    {
                        if (mUpdateCallback != null) {
                            EditText nome=(EditText)getDialog().findViewById(R.id.etEmailName);
                            EditText indirizzo=(EditText)getDialog().findViewById(R.id.etEmailAddress);
                            String oldEmail=getArguments().getString(OLD_EMAIL);
                            mUpdateCallback.onUserToUpdateInserted(nome,indirizzo,oldEmail);
                        }
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
        try {
            mUpdateCallback = (OnUserToUpdateInsertedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnUserToUpdateInsertedListener");
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
        boolean onUserInserted(EditText nome,EditText indirizzo);
    }

    public interface OnUserToUpdateInsertedListener
    {
        boolean onUserToUpdateInserted(EditText nome,EditText indirizzo,String oldEmail);

    }
}

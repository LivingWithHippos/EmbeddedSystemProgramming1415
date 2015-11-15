package org.tbw.FemurShield.Controller.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.tbw.FemurShield.R;

/**
 * Questa classe gestisce il Dialog che permette di scegliere se cancellare o modificare un contatto
 *
 * @author Marco Biasin
 */
public class ContactOptionsDialog extends DialogFragment {

    public final static int EDIT_CONTACT = 0;
    public final static int DELETE_CONTACT = 1;
    final static String EDIT = "Modifica";
    final static String DELETE = "Elimina";
    public final static String SELECTED_MAIL = "selected_mail";
    public final static String SELECTED_NAME = "selected_name";
    private OnContactOptionsClickListener mCallback;
    private String contactName, contactAddress;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        contactAddress = bundle.getString(SELECTED_MAIL);
        contactName = bundle.getString(SELECTED_NAME);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        //attenzione! le opzioni devo essere inserite nell'ordine indicato dalle costanti EDIT_CONTACT,DELETE_CONTACT, etc...
        alert.setTitle(R.string.contact_options_dialog_title)
                .setItems(new String[]{EDIT, DELETE}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.onContactOptionClick(contactAddress, contactName, which);
                    }
                });
        return alert.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnContactOptionsClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnContactOptionsClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     *
     */
    public interface OnContactOptionsClickListener {

        /**
         * Metodo per il callback con cui avvertire l'activity che e' stato premuto su modifica o elimina di un dialog
         *
         * @param emailAddress l'indirizzo email del contatto
         * @param name         il nome del contatto
         * @param type         se il contatto e' da eliminare o modificare
         */
        public void onContactOptionClick(String emailAddress, String name, int type);
    }
}

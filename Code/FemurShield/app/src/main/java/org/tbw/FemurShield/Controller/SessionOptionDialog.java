package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.tbw.FemurShield.R;

/**
 * Created by Moro on 30/06/15.
 */
public class SessionOptionDialog extends DialogFragment {

    public final static int RENAME_SESSION=0;
    public final static int DELETE_SESSION=1;
    final static String RENAME="Rinomina";
    final static String DELETE="Elimina";
    public final static String SELECTED_DATA="selected_data";
    private OnSessionOptionsClickListener mCallback;
    private String contactData;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        contactData=bundle.getString(SELECTED_DATA);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        alert.setTitle(R.string.session_options_dialog_title)
                .setItems(new String[]{RENAME, DELETE}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(isAdded())
                            mCallback.onSessionOptionClick(contactData, which);
                    }
                });
        return alert.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnSessionOptionsClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnSessionOptionsClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface OnSessionOptionsClickListener {

        public void onSessionOptionClick(String data,int type);
    }
}

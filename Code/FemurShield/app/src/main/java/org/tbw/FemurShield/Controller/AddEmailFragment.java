package org.tbw.FemurShield.Controller;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 08/05/2015.
 */
public class AddEmailFragment extends DialogFragment{

    private AddEmailDialog.OnUserInsertedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCallback=(AddEmailDialog.OnUserInsertedListener)getActivity();
        return new AddEmailDialog(getActivity(),R.style.AppDialogTheme,mCallback);
    }


}

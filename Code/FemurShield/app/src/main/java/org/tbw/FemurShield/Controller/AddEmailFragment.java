package org.tbw.FemurShield.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 08/05/2015.
 */
public class AddEmailFragment extends DialogFragment implements AddEmailDialog.OnClickListener{

    private AddEmailDialog.OnUserInsertedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCallback=(AddEmailDialog.OnUserInsertedListener)getActivity();
        return new AddEmailDialog(getActivity(),R.style.AppDialogTheme,mCallback);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        //TODO aggiungi listener ok
    }

}

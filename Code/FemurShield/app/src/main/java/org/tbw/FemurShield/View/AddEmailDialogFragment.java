package org.tbw.FemurShield.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 08/05/2015.
 */
public class AddEmailDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{


    private SharedPreferences prefs;
    private EditText etName;
    private EditText etAddress;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //classe per creare ALert Dialog, ci applico il tema di sistema
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        alert.setTitle("Aggiungi Persona");
        // modifico le proprieta' dell'EditText nome
        etName = new EditText(getActivity());
        etName.setInputType(InputType.TYPE_CLASS_TEXT);
        etName.setMaxLines(1);
        etName.setHint(getString(R.string.et_email_name_hint));
        int etNamePadding=convertDpToPx(10, etName);
        etName.setPadding(etNamePadding, etNamePadding, etNamePadding, etNamePadding);
        alert.setView(etName);
        alert.setPositiveButton(getString(R.string.ok), this);
        alert.setNegativeButton(getString(R.string.cancel), this);

        return alert.create();

    }

    //metodo che converte da pixel a dp
    private int convertDpToPx(int dp, View view) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return Math.round(pixels);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //TODO aggiungi listener ok
    }
}

package org.tbw.FemurShield.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 08/05/2015.
 */
public class AddEmailDialog extends AlertDialog implements DialogInterface.OnClickListener {

    private OnUserInsertedListener mCallback;

    protected AddEmailDialog(Context context,int theme,OnUserInsertedListener callback) {
        super(context,theme);
        mCallback=callback;
        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.fragment_add_email, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mCallback != null) {
                    EditText nome=(EditText)findViewById(R.id.etEmailName);

                    EditText indirizzo=(EditText)findViewById(R.id.etEmailAddress);
                    mCallback.onUserInserted(nome,indirizzo);
                }
                break;
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

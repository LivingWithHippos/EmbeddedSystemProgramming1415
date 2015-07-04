package org.tbw.FemurShield.Controller.Reminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 07/06/2015.
 */
public class SessionReminderDialog extends DialogFragment implements DialogInterface.OnClickListener{
    private OnSessionStartingListener mCallback;
    private OnDialogClosedListener cDialogCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.AppDialogTheme);
        final Context themeContext = getActivity();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.fragment_session_reminder_layout, null);
        alert.setView(view);
        alert.setPositiveButton(getString(R.string.startSession), this);
        alert.setNegativeButton(getString(R.string.cancel), this);
        return alert.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
         case AlertDialog.BUTTON_POSITIVE:
                mCallback.onSessionStarted(true);
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                mCallback.onSessionStarted(false);
                break;
         }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnSessionStartingListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnSessionStartingListener");
        }
        try {
            cDialogCallback = (OnDialogClosedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnDialogClosedListener");
        }
    }

    @Override
     public void onDetach() {
        super.onDetach();
        mCallback = null;
        cDialogCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cDialogCallback.onDialogClosed();
    }

    public interface OnSessionStartingListener {
        void onSessionStarted(boolean wantToStart);
    }

    public interface OnDialogClosedListener {
        void onDialogClosed();
    }
}

package org.tbw.FemurShield;

import android.app.AlertDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View; 
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.SeekBar; 

/**
 * Created by Marco on 04/05/2015.
 */
public class SliderDialog extends AlertDialog implements OnClickListener{

    private static final String PROGRESS="progress";

    private final SeekBar mSeekBar;
    private final OnProgressSetListener mProgressSetCallback; 

    private final int mInitialProgress;

    /**
     * Interfaccia di callback per quando l'utente ha finito premendo il bottone ok
     */
    public interface OnProgressSetListener {

        /**
         * @param view The view associated with this listener.
         * @param progress Il punto impostato dall'utente
         */
        void onProgressSet(SeekBar view, int progress);
    }
    

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param actualProgress Il punto impostato inizialmente.
     */
    public SliderDialog(Context context,OnProgressSetListener callBack,int actualProgress) {
        this(context, 0, callBack, actualProgress);
    }


    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     */
    public SliderDialog(Context context, int theme, OnProgressSetListener callBack, int actualProgress) {
        super(context);

        mProgressSetCallback= callBack;
        mInitialProgress=actualProgress;

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.seekbar_picker_dialog, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
        
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(actualProgress);

        //mTimePicker.setValidationCallback(mValidationCallback);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mProgressSetCallback != null) {
                    mProgressSetCallback.onProgressSet(mSeekBar, mSeekBar.getProgress());
                }
                break;
        }
    }
    
    public void updateProgress(int progress) {
        mSeekBar.setProgress(progress);
    }



}

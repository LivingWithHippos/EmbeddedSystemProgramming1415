package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.Model.SignatureImpl;
import org.tbw.FemurShield.R;

/**
 * Created by Marco on 19/06/2015.
 */
public class SessionCommandsFragment extends Fragment {


    private ImageView rec,pause,play,stop;
    private ImageView[] buttons;

    public final static int BUTTON_REC=0,BUTTON_PAUSE=1,BUTTON_PLAY=2,BUTTON_STOP=3;
    public static final String UI_MODE="ui_mode";
    public static final boolean MODE_BIG=true,MODE_SMALL=false;

    private boolean uiMode;

    private OnCommandUpdatedListener mCallback;

    public static SessionCommandsFragment newInstance(boolean uiMode) {

        SessionCommandsFragment fragment = new SessionCommandsFragment();
        Bundle b = new Bundle();
        b.putBoolean(UI_MODE, uiMode);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();

        if(Controller.getInstance().isRecording()) {
            if (Controller.getInstance().isRunning()) {
                rec.setVisibility(ImageView.INVISIBLE);
                pause.setVisibility(ImageView.VISIBLE);
                stop.setVisibility(ImageView.VISIBLE);
                play.setVisibility(ImageView.INVISIBLE);
            }
            else{
                rec.setVisibility(ImageView.INVISIBLE);
                pause.setVisibility(ImageView.INVISIBLE);
                stop.setVisibility(ImageView.VISIBLE);
                play.setVisibility(ImageView.VISIBLE);
            }
        }
        else{
            rec.setVisibility(ImageView.VISIBLE);
            pause.setVisibility(ImageView.INVISIBLE);
            stop.setVisibility(ImageView.INVISIBLE);
            play.setVisibility(ImageView.INVISIBLE);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiMode=getArguments().getBoolean(UI_MODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_session_commands, container, false);

        rec= (ImageView) rootView.findViewById(R.id.recbtnun1);
        play= (ImageView) rootView.findViewById(R.id.startbtnui1);
        pause= (ImageView) rootView.findViewById(R.id.pausebtnui1);
        stop= (ImageView) rootView.findViewById(R.id.stopbntui1);
        buttons=new ImageView[]{rec,play,pause,stop};
        if(uiMode==MODE_SMALL)
        {
            for(ImageView iv:buttons)
            {
                ViewGroup.LayoutParams params=iv.getLayoutParams();
                params.width = convertDpToPx(47,iv);
                params.height = convertDpToPx(47,iv);
                iv.setLayoutParams(params);
            }

        }
        rec.setOnClickListener(new CommandClickListener());
        play.setOnClickListener(new CommandClickListener());
        pause.setOnClickListener(new CommandClickListener());
        stop.setOnClickListener(new CommandClickListener());

        return rootView;
    }


    private int convertDpToPx(int dp, View view) {
        DisplayMetrics dm = view.getResources().getDisplayMetrics();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
        return Math.round(pixels);
    }

    public void onRecClick(){
        //TODO: business logic
        Controller.getInstance().CreateSession();

        onPlayClick();

        //aggiorno la listView
        if(isAdded())
            mCallback.aggiornaLista(BUTTON_REC);
        Intent intent=new Intent(getActivity(),UI3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onPauseClick(){
        //TODO: business logic
        Controller.getInstance().PauseSession(getActivity());

        //modifico le visibilita' dei bottoni di controllo
        rec.setVisibility(ImageView.INVISIBLE);
        pause.setVisibility(ImageView.INVISIBLE);
        stop.setVisibility(ImageView.VISIBLE);
        play.setVisibility(ImageView.VISIBLE);
        if(isAdded())
            mCallback.aggiornaLista(BUTTON_PAUSE);

    }

    public void onPlayClick(){
        //TODO: business logic
        Controller.getInstance().StartSession(getActivity());

        //modifico le visibilita' dei bottoni di controllo
        rec.setVisibility(ImageView.INVISIBLE);
        pause.setVisibility(ImageView.VISIBLE);
        stop.setVisibility(ImageView.VISIBLE);
        play.setVisibility(ImageView.INVISIBLE);
        if(isAdded())
            mCallback.aggiornaLista(BUTTON_PLAY);
    }

    public void onStopClick(){
        //TODO: business logic
        SignatureImpl si=SignatureImpl.getInstance(SessionManager.getInstance().getActiveSession().getDataTime());
        si.stopDrawing();
        Controller.getInstance().StopSession(getActivity());

        //modifico le visibilita' dei bottoni di controllo
        rec.setVisibility(ImageView.VISIBLE);
        pause.setVisibility(ImageView.INVISIBLE);
        stop.setVisibility(ImageView.INVISIBLE);
        play.setVisibility(ImageView.INVISIBLE);

        //aggiorno la listView
        if(isAdded())
            mCallback.aggiornaLista(BUTTON_STOP);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnCommandUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare  OnCommandUpdatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    class CommandClickListener implements ImageView.OnClickListener
    {

        @Override
        public void onClick(View v) {
           Log.d("SessionCommandsFragment","view id :"+v.getId());
            switch (v.getId())
            {
                case R.id.recbtnun1: onRecClick(); break;
                case R.id.startbtnui1: onPlayClick(); break;
                case R.id.pausebtnui1: onPauseClick(); break;
                case R.id.stopbntui1: onStopClick(); break;
            }
        }
    }

    public interface OnCommandUpdatedListener
    {
        public void aggiornaLista(int buttonPressed);
    }
}

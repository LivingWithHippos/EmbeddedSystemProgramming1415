package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.tbw.FemurShield.R;

/**
 * Created by Marco on 19/06/2015.
 */
public class SessionCommandsFragment extends Fragment {


    private ImageView rec,pause,play,stop;

    public final static int BUTTON_REC=0,BUTTON_PAUSE=1,BUTTON_PLAY=2,BUTTON_STOP=3;

    private OnCommandUpdatedListener mCallback;

    public static SessionCommandsFragment newInstance() {

        SessionCommandsFragment fragment = new SessionCommandsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_session_commands, container, false);

        rec= (ImageView) rootView.findViewById(R.id.recbtnun1);
        play= (ImageView) rootView.findViewById(R.id.startbtnui1);
        pause= (ImageView) rootView.findViewById(R.id.pausebtnui1);
        stop= (ImageView) rootView.findViewById(R.id.stopbntui1);

        rec.setOnClickListener(new CommandClickListener());
        play.setOnClickListener(new CommandClickListener());
        pause.setOnClickListener(new CommandClickListener());
        stop.setOnClickListener(new CommandClickListener());

        return rootView;
    }

    public void onRecClick(){
        //TODO: business logic
        Controller.getInstance().CreateSession();

        onPlayClick();

        //aggiorno la listView
        mCallback.aggiornaLista(BUTTON_REC);
        Intent intent=new Intent(getActivity(),UI3.class);
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

        mCallback.aggiornaLista(BUTTON_PLAY);
    }

    public void onStopClick(){
        //TODO: business logic
        Controller.getInstance().StopSession(getActivity());

        //modifico le visibilita' dei bottoni di controllo
        rec.setVisibility(ImageView.VISIBLE);
        pause.setVisibility(ImageView.INVISIBLE);
        stop.setVisibility(ImageView.INVISIBLE);
        play.setVisibility(ImageView.INVISIBLE);

        //aggiorno la listView
        mCallback.aggiornaLista(BUTTON_STOP);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnCommandUpdatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCommandUpdatedListener");
        }
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

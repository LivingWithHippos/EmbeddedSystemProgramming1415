package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link FallDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FallDetailsFragment extends Fragment {

    private String sessionID;
    private String fallID;
    private String latitude,longitude;

    private LinearLayout ll;
    private int width,height;

    private Session shownSession;
    private Fall shownFall;

    private Bitmap sessionSignature;
    private ImageView ivSessionSignature;
    private ImageView ivFallSignature;
    private TextView tvFallDateTime;
    private TextView tvFallLatitude;
    private TextView tvFallLongitude;

    private String[] palette;


    public static FallDetailsFragment newInstance(String sessionID,String fallID,String[] palette) {
        FallDetailsFragment fragment = new FallDetailsFragment();
        Bundle args = new Bundle();
        args.putString(UI4.ID_SESSION, sessionID);
        args.putString(UI4.ID_FALL, fallID);
        args.putStringArray(UI4.COLOR_PALETTE,palette);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fall_details, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        //imposto la signature della sessione
        ivSessionSignature=(ImageView)rootView.findViewById(R.id.ivSessionSignatureInFallDetails);
        ivSessionSignature.setImageBitmap(sessionSignature);

        ivFallSignature=(ImageView)rootView.findViewById(R.id.ivFallSignature);
        loadBitmap(R.id.ivFallSignature,ivFallSignature,rootView);
       // ivFallSignature.setImageBitmap(fbc.getBitmap());

        //setto il testo
        tvFallDateTime=(TextView)rootView.findViewById(R.id.tvFallDetailsDateTime);
        tvFallDateTime.setText(getString(R.string.fall_date_time)+" "+fallID);
        tvFallLatitude=(TextView)rootView.findViewById(R.id.tvFallLatitude);
        tvFallLatitude.setText(getString(R.string.fall_latitude) + " " + latitude);
        tvFallLongitude=(TextView)rootView.findViewById(R.id.tvFallLongitude);
        tvFallLongitude.setText(getString(R.string.fall_longitude) + " " + longitude);

        return rootView;
    }

    public void loadBitmap(int resId, ImageView imageView,View view) {
        FallBitmapCreator fbc=new FallBitmapCreator(view,ivFallSignature,shownFall,height,width,palette);
        fbc.execute(resId);
    }

    public FallDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //inizializzo le variabili
        if (getArguments() != null) {
            sessionID = getArguments().getString(UI4.ID_SESSION);
            fallID = getArguments().getString(UI4.ID_FALL);
            palette=getArguments().getStringArray(UI4.COLOR_PALETTE);
        }

        SessionManager sm=SessionManager.getInstance();
        shownSession=sm.getAllSessionsById().get(sessionID);
        if(shownSession!=null) {
            ArrayList<Fall> fallList=shownSession.getFalls();
            for(Fall f:fallList)
                if(f.getData().equalsIgnoreCase(fallID))
                {
                    shownFall=f;
                    break;
                }

            if(shownFall!=null)
            {
                sessionSignature = shownSession.getSignature().toBitmap();
                latitude=shownFall.getPosition()[0]+"";
                longitude=shownFall.getPosition()[1]+"";

            }else
            {
                Toast.makeText(activity,getString(R.string.no_session_found),Toast.LENGTH_LONG).show();
            }


        }else
        {
            Toast.makeText(activity,getString(R.string.no_session_found),Toast.LENGTH_LONG).show();
        }
    }


}

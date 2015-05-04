package org.tbw.FemurShield;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SettingsFragment extends ListFragment {

    private List<SettingListItem> mItems;
    private SharedPreferences prefs;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
   /* public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inizializzo la lista di elementi
        mItems = new ArrayList<SettingListItem>();
        //mi serve per ottenere le icone
        Resources resources = getResources();
        //TODO: impostare icone di dimensioni diverse a seconda della dimensione dello schermo?
        //TODO: rinominare le descrizioni in modo che mostrino il valore impostato
        // prefs serve a caricare le impostazioni salvate
        prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
        //aggiungo le voci alla lista
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.frequency),
                getString(R.string.title_sample_rate),
                getString(R.string.description_sample_rate)));
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.duration),
                getString(R.string.title_session_duration),
                getString(R.string.description_session_duration)));
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.email),
                getString(R.string.title_email_recipient),
                getString(R.string.description_email_recipient)));
        int hour=prefs.getInt("alarm_hour",9);
        int minute=prefs.getInt("alarm_minute", 0);
        mItems.add(new SettingListItem(resources.getDrawable(R.drawable.alarm),
                getString(R.string.title_alarm),
                "Sveglia impostata per le "+hour+":"+minute));
        //imposto l'adapter
        setListAdapter(new SettingListAdapter(getActivity(), mItems));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // tolgo i divisori tra le voci
        getListView().setDivider(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // recupera l'elemento cliccato
        SettingListItem item = mItems.get(position);

        if(mListener!=null&item!=null)
            mListener.onVoiceSelected(item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onVoiceSelected(SettingListItem s);
    }

}

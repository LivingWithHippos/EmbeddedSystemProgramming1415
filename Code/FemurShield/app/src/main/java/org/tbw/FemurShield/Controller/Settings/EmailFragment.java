package org.tbw.FemurShield.Controller.Settings;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.tbw.FemurShield.Controller.PreferencesEditor;
import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmailFragment.OnEmailItemClickedListener} interface
 * to handle interaction events.
 * Use the {@link EmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailFragment extends ListFragment implements Button.OnClickListener {


    private List<EmailListItem> mItems;
    private OnEmailItemClickedListener mCallback;
    private OnAddEmailButtonClickListener aEmailCallback;
    private EmailListAdapter mAdapter;
    private Button addEmail;
    private HashMap<String,String> emailContacts;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmailFragment.
     */

    public static EmailFragment newInstance() {
        EmailFragment fragment = new EmailFragment();
        return fragment;
    }

    public EmailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeList();
    }

    private void initializeList()
    {
        mItems=new ArrayList<>();
        emailContacts=new PreferencesEditor(getActivity()).getEmail();
        if (emailContacts!=null)
        {
            for(HashMap.Entry<String,String> pair:emailContacts.entrySet())
                mItems.add(new EmailListItem(pair.getKey(),pair.getValue()));
        }
        else
        {
            //mItems.add(new EmailListItem("dummy@email.it","dummy"));
        }
        //imposto l'adapter
        mAdapter=new EmailListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Mi serve per il findViewByID
        View rootView = inflater.inflate(R.layout.fragment_email, container,false);
        addEmail=(Button)rootView.findViewById(R.id.add_email_button);
        addEmail.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnEmailItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onEmailItemClickedListener");
        }
        try {
            aEmailCallback = (OnAddEmailButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAddEmailButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        aEmailCallback=null;
    }



    public void addAndUpdateContact(String nome, String indirizzo)
    {
        if(mAdapter!=null){
            mAdapter.add(new EmailListItem(indirizzo,nome));
            mAdapter.notifyDataSetChanged();}

    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        EmailListItem e=mItems.get(position);
        mCallback.onEmailItemClicked(e);
    }

    @Override
    public void onClick(View v) {
        aEmailCallback.onAddEmailButtonClick();
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
    public interface OnEmailItemClickedListener {

        public void onEmailItemClicked(EmailListItem e);
    }

    public interface OnAddEmailButtonClickListener {

        public void onAddEmailButtonClick();
    }



}

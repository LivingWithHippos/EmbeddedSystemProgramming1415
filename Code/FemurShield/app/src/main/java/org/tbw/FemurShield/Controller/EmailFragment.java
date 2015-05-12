package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private SharedPreferences prefs;
    private EmailListAdapter mAdapter;
    private Button addEmail;


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


        mItems = new ArrayList<EmailListItem>();
        // prefs serve a caricare le impostazioni salvate
        prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
        //aggiungo le voci alla lista
        String[] email=getEmailData();
        if(email!=null)
            for(int i=0;i<email.length;i++)
            {
                Log.d("FemurShield",email[i]);
                mItems.add(new EmailListItem(email[i++], email[i]));
                Log.d("FemurShield", email[i]);
            }
        mItems.add(new EmailListItem("ciao@emiall.it","mama"));
        mItems.add(new EmailListItem("asdsd@asd.it", "papa"));
        //imposto l'adapter
        mAdapter=new EmailListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

    public String[] getEmailData()
    {
        SharedPreferences prefs=getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> mySet=prefs.getStringSet(getString(R.string.email_list),null);
        String[] emailData=null;
        if(mySet!=null)
        {
            int index=0;
            emailData=new String[mySet.size()*2];
            for(String s:mySet)
            {
                String userData[]=s.split("\n",2);
                Log.d("Femurshield","dimensione array: "+userData.length);
                emailData[index++]=userData[0];
                emailData[index++]=userData[1];
            }
        }
        return emailData;
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

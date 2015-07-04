package org.tbw.FemurShield.Controller.Settings;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.tbw.FemurShield.Controller.PreferencesEditor;
import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link EmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailFragment extends ListFragment implements Button.OnClickListener{



    private List<EmailListItem> mItems;
    private OnAddEmailButtonClickListener aEmailCallback;
    private OnClearEmailClickListener mClearCallback;
    private OnContactClickListener mContactClickCallback;
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
        setHasOptionsMenu(true);
        initializeList();
       // getListView().setOnItemLongClickListener();
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
            Toast.makeText(getActivity(),getString(R.string.add_contact_tip),Toast.LENGTH_SHORT).show();

        //imposto l'adapter
        mAdapter=new EmailListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Mi serve per il findViewByID
        View rootView = inflater.inflate(R.layout.fragment_email, container, false);
        addEmail=(Button)rootView.findViewById(R.id.add_email_button);
        addEmail.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list=getListView();
        list.setOnItemLongClickListener(new OnContactLongClickListener());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            aEmailCallback = (OnAddEmailButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnAddEmailButtonClickListener");
        }
        try {
            mClearCallback = (OnClearEmailClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnClearEmailClickListener");
        }
        try {
            mContactClickCallback = (OnContactClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " deve implementare OnContactClickListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        aEmailCallback=null;
        mClearCallback=null;
        mContactClickCallback=null;
    }



    public void addAndUpdateContact(String indirizzo,String nome)
    {
        if(mAdapter!=null){
            mAdapter.add(new EmailListItem(indirizzo, nome));
            mAdapter.notifyDataSetChanged();}

    }

    public void clearList()
    {
        if(mAdapter!=null) {
            if (mAdapter.getCount() > 0) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(),getString(R.string.empty_contact_list),Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onClick(View v) {
        aEmailCallback.onAddEmailButtonClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_clear_all_contacts:
                mClearCallback.onClearEmail();
        }
        // il super chiama il metodo nella UI 5
        // che gestisce gli altri bottoni del menu
        //senza bisogno di riscriverne i metodi
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem clearContacts=menu.findItem(R.id.action_clear_all_contacts);
        clearContacts.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void updateList() {
        initializeList();
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

    public interface OnAddEmailButtonClickListener {

        public void onAddEmailButtonClick();
    }
    public interface OnClearEmailClickListener {

        public void onClearEmail();
    }
    public interface OnContactClickListener {

        public void onContactLongClick(String emailAddress,String name);
    }


    public class OnContactLongClickListener implements ListView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            EmailListItem eli=(EmailListItem)parent.getItemAtPosition(position);
            mContactClickCallback.onContactLongClick(eli.address,eli.name);
            return true;
        }
    }

}

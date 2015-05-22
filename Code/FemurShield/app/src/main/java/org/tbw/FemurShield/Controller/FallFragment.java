package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vianello on 21/05/15.
 */
public class FallFragment extends ListFragment
{
    private List<FallListItem> fItems;
    private FallListAdapter fAdapter;
    private ArrayList<Fall> falls;
    private Session session;

    public FallFragment()
    {
    }

    public static FallFragment newInstance()
    {
        FallFragment fragment = new FallFragment();
        return fragment;
    }

    @Override
    public void onAttach (Activity activity)
    {
        // TODO dalla UI2 prende i dati della sessione selezionata da UI1,
        // TODO che mostra tutte le sessioni, passare tramite intent?
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startlist();
    }

    public void startlist()
    {
        fItems = new ArrayList<>();
        falls = session.getFalls();
        for(int i = 0; i< session.getFallsNumber();i++)
        {
            int num = falls.get(i).getId();
            String n = "Caduta N°"+num;
            String d = falls.get(i).getData();
            boolean s = falls.get(i).isReported();

            fItems.add(new FallListItem(n,d,s));
        }
        fAdapter=new FallListAdapter(getActivity(), fItems);
        setListAdapter(fAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_fall, container,false);
        return rootView;
    }
}
package org.tbw.FemurShield.Controller;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vianello on 21/05/15.
 */
public class FallFragment extends ListFragment
{
    private List<FallListItem> fItems;

    public FallFragment()
    {
    }

    public static FallFragment newInstance()
    {
        FallFragment fragment = new FallFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startlist();
    }

    public void startlist()
    {
        fItems = new ArrayList<>();

    }
}

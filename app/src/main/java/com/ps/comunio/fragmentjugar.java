package com.ps.comunio;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * Created by Luisca on 9/11/15.
 */
public class fragmentjugar extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comojugar, container, false);

        return rootView;
    }


    //@Override
    //public void onListItemClick(ListView l, View v, int position, long id) {
    //    super.onListItemClick(l, v, position, id);
    //}


}


package com.kamron.pogoiv.Overlay.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamron.pogoiv.R;

/**
 * Created by Johan on 2016-09-03.
 * The fragment which shows the user the results of a scan
 */
public class ResultsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_input, container, false);
    }
}

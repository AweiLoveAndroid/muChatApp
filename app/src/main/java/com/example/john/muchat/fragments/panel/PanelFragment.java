package com.example.john.muchat.fragments.panel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.john.muchat.R;


/**
 * Created by John on 2017/7/9.
 */
public class PanelFragment extends Fragment {


    public PanelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }


    public void showFace(){

    }

    public void showRecord(){

    }

    public void showGallery(){

    }
}
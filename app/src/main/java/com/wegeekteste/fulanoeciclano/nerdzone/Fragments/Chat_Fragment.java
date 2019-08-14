package com.wegeekteste.fulanoeciclano.nerdzone.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wegeekteste.fulanoeciclano.nerdzone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_Fragment extends Fragment {


    public Chat_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_, container, false);
    }

}

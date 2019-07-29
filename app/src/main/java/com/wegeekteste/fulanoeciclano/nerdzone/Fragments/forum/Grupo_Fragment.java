package com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wegeekteste.fulanoeciclano.nerdzone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Grupo_Fragment extends Fragment {


    public Grupo_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grupo_, container, false);
    }

}

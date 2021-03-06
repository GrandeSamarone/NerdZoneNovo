package com.wegeekteste.fulanoeciclano.nerdzone.Fragments.Fragments_Icons;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.IconeAdapter;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Icones;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilmesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public static final int SELECAO_ICONE = 34;
    private RecyclerView recyclerViewFilmes;
    private SwipeRefreshLayout swipeiconeFilmes;
    private DatabaseReference Icons_Filmes;
    private ArrayList<Icones> ListFilmes = new ArrayList<>();
    private IconeAdapter adapterFilmes;
    private ValueEventListener valueEventListenerFilmes;

    public FilmesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filmes, container, false);

//ATUALIZA
        swipeiconeFilmes = view.findViewById(R.id.swipe_fragment_filmes);
        swipeiconeFilmes.setRefreshing(true);
        swipeiconeFilmes.setOnRefreshListener(FilmesFragment.this);
        swipeiconeFilmes.post(new Runnable() {
            @Override
            public void run() {
                RecuperarAleatorio();
            }
        });

        recyclerViewFilmes = view.findViewById(R.id.mRecylcerfilmes);
        Icons_Filmes =  ConfiguracaoFirebase.getFirebaseDatabase().child("icones").child("iconesfilmes");

      //Configurar Adapter
        adapterFilmes = new IconeAdapter(getContext(),ListFilmes);

        //Configuracao RecycleView
        recyclerViewFilmes.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewFilmes.setAdapter(adapterFilmes);

        recyclerViewFilmes.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), recyclerViewFilmes, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (getActivity().getIntent().hasExtra("minhaconta")) {
                        Intent intent = new Intent( getActivity(), MinhaConta.class);
                        intent.putExtra("caminho_foto", ListFilmes.get(position).getUrl());
                        intent.putExtra("selecaoicone", SELECAO_ICONE);
                        startActivity(intent);
                        getActivity().finish();

                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        Icons_Filmes.removeEventListener(valueEventListenerFilmes);
    }
    @Override
    public void onRefresh() {
        RecuperarAleatorio();
    }

    public void RecuperarAleatorio(){
        ListFilmes.clear();
        swipeiconeFilmes.setRefreshing(true);
        valueEventListenerFilmes = Icons_Filmes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados:  dataSnapshot.getChildren()){
                    Icones icone = dados.getValue(Icones.class);
                    ListFilmes.add(icone);
                }
                adapterFilmes.notifyDataSetChanged();
                swipeiconeFilmes.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

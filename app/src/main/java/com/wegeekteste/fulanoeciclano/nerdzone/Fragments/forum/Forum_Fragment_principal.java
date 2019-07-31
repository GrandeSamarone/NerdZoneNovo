package com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Forum_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Novo_Grupo_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Forum_Fragment_principal extends Fragment implements View.OnClickListener {
    private Adapter_Forum_Grupo adapter_Meus_forum;
    private RecyclerView recyclerView_lista_Meus_Forum;
    private ArrayList<Forum> listaForum = new ArrayList<>();
    private Dialog dialog;
    private SharedPreferences preferences = null;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private BottomNavigationView navigation;
    private ShimmerFrameLayout shimmerContainer;
    private LinearLayout criarGrupo;
    public Forum_Fragment_principal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=inflater.inflate(R.layout.fragment_forum__fragment_principal, container, false);

       //Configuraçoes Basicas
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        db = FirebaseFirestore.getInstance();
        recyclerView_lista_Meus_Forum = view.findViewById(R.id.recycleview_grupo);
        criarGrupo = view.findViewById(R.id.line_criarGrupo);
        criarGrupo.setOnClickListener(this);

        //adapter
        adapter_Meus_forum = new Adapter_Forum_Grupo(listaForum,getContext());

        //efeito carregando do facebook
        shimmerContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container_Forum);

        //Adapter
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView_lista_Meus_Forum.setLayoutManager(layoutManager);
        recyclerView_lista_Meus_Forum.setHasFixedSize(true);
        recyclerView_lista_Meus_Forum.setAdapter(adapter_Meus_forum);


        RecuperarForum();

        // lembrar de como reparar se o collection é null
    return view;
    }


    private void RecuperarForum(){
        shimmerContainer.stopShimmerAnimation();
        shimmerContainer.setVisibility(View.GONE);
        db.collection("WeForum")
                .whereEqualTo("id_autor", identificadorUsuario)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }

                        for (DocumentChange change : snapshots.getDocumentChanges()) {
                            Forum forum_grupo = change.getDocument().toObject(Forum.class);
                            forum_grupo.setId(change.getDocument().getId());
                            //  Log.i("sdsdsd",change.getDocument().getId());
                            // Log.i("sdsdsd2",conto.getUid());
                            switch (change.getType()) {
                                case ADDED:

                                    listaForum.add(0, forum_grupo);

                                    if (listaForum.size() > 0) {
                                        //  linear_nada_cadastrado.setVisibility(View.GONE);
                                    }

                                    adapter_Meus_forum.notifyDataSetChanged();
                                    shimmerContainer.stopShimmerAnimation();
                                    shimmerContainer.setVisibility(View.GONE);
                                    Log.d("ad", "New city: " + change.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    for (Forum ct : listaForum) {

                                        if(forum_grupo.getId().equals(ct.getId())){
                                            listaForum.remove(ct);
                                            break;
                                        }
                                    }
                                    listaForum.add(0, forum_grupo);
                                    if (listaForum.size() > 0) {
                                        //linear_nada_cadastrado.setVisibility(View.GONE);
                                    }

                                    adapter_Meus_forum.notifyDataSetChanged();
                                    shimmerContainer.stopShimmerAnimation();
                                    shimmerContainer.setVisibility(View.GONE);
                                    Log.d("md", "Modified city: " + change.getDocument().getData());
                                    break;
                                case REMOVED:
                                    for (Forum ct : listaForum) {

                                        if(forum_grupo.getId().equals(ct.getId())){
                                            listaForum.remove(ct);
                                            break;
                                        }
                                    }

                                    adapter_Meus_forum.notifyDataSetChanged();
                                    shimmerContainer.stopShimmerAnimation();
                                    shimmerContainer.setVisibility(View.GONE);
                                    Log.d("rem", "Removed city: " + change.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });

    }
    @Override
    public void onResume() {
        super.onResume();
        shimmerContainer.startShimmerAnimation();
    }


    @Override
    public void onPause() {
        shimmerContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.line_criarGrupo:
                Intent it = new Intent(getContext(), Novo_Grupo_Forum.class);
                startActivity(it);

        }
    }
}

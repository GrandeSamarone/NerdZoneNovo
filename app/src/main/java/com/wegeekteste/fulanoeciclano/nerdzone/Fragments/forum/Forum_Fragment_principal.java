package com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Forum_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Convite_Activity;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Chat_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Info_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Novo_Grupo_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Forum_Fragment_principal extends Fragment implements View.OnClickListener {
    private Adapter_Forum_Grupo adapter_Meus_forum;
    private RecyclerView recyclerView_lista_Meus_Forum;
    private ArrayList<Forum> listaForum = new ArrayList<>();
    private SharedPreferences preferences = null;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private ShimmerFrameLayout shimmerContainer;
    private ListenerRegistration registration;
    private LinearLayout criarGrupo,line_convites;
    FirebaseUser fuser;
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
        line_convites=view.findViewById(R.id.line_convite);
        line_convites.setOnClickListener(this);

        //adapter
        adapter_Meus_forum = new Adapter_Forum_Grupo(listaForum,getContext());

        //efeito carregando do facebook
        shimmerContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container_Forum);

        //Adapter
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView_lista_Meus_Forum.setLayoutManager(layoutManager);
        recyclerView_lista_Meus_Forum.setHasFixedSize(true);
        recyclerView_lista_Meus_Forum.setAdapter(adapter_Meus_forum);

        recyclerView_lista_Meus_Forum.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), recyclerView_lista_Meus_Forum, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Forum> listaAtualizada = adapter_Meus_forum.getForuns();
                Toast toast = Toast.makeText(getContext(), " Carregando...",Toast.LENGTH_SHORT);
                toast.show();
                if(listaAtualizada.size()>0){
                    String id_grupo_selecionado = adapter_Meus_forum.getForuns().get(position).getId();
                    Forum forumselecionado = listaAtualizada.get(position);
                    Intent it = new Intent(getContext(), Page_Chat_grupo.class);
                    it.putExtra("forum_selecionado",forumselecionado);
                    it.putExtra("id_forum_selecionado",id_grupo_selecionado);
                    it.putExtra("id_admin",forumselecionado.getIdauthor());
                    startActivity(it);


                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));
        RecuperarForum();


        // lembrar de como reparar se o collection é null
    return view;
    }



    private void RecuperarForum(){

        shimmerContainer.stopShimmerAnimation();
        shimmerContainer.setVisibility(View.GONE);
        Query query= db.collection("WeForum")
                .whereArrayContains("membros", identificadorUsuario);
        registration=query .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }

                        for (DocumentChange change : snapshots.getDocumentChanges()) {
                            Forum forum_grupo = change.getDocument().toObject(Forum.class);
                            Log.i("sdsdsd74798",change.getDocument().getId());
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
        registration.remove();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.line_criarGrupo:
                Intent it = new Intent(getContext(), Novo_Grupo_Forum.class);
                startActivity(it);
                break;
            case R.id.line_convite:
                Intent it_convite = new Intent(getContext(), Convite_Activity.class);
                startActivity(it_convite);
                break;

        }
    }
}

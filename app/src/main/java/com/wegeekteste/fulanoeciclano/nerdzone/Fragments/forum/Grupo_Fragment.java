package com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Info_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Lista_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.IOnBackPressed;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Grupo_Fragment extends Fragment implements IOnBackPressed {
    private FirebaseFirestore db;
    private Adapter_Forum adapter_forum;
    private Adapter_Forum adapter_topico;
    private RecyclerView recicle_Grupo_geral,recicle_Topico_geral;
    private ArrayList<Forum> ListaG = new ArrayList<>();
    private ArrayList<Forum> ListaT = new ArrayList<>();
    private String id_Forum;
    private ShimmerFrameLayout shimmerContainer;

    public Grupo_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_grupo_, container, false);

        //Configuracoes_Originais
        db = FirebaseFirestore.getInstance();



        //efeito carregando do facebook
        shimmerContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container_Grupo);
        recicle_Grupo_geral=view.findViewById(R.id.RecycleViewGrupo_geral);
        adapter_forum = new Adapter_Forum(ListaG,getContext());
        //Adapter
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recicle_Grupo_geral.setLayoutManager(layoutManager);
        recicle_Grupo_geral.setHasFixedSize(true);
        recicle_Grupo_geral.setAdapter(adapter_forum);

        recicle_Grupo_geral.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), recicle_Grupo_geral, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Forum> listaAtualizada = adapter_forum.getForuns();

                if(listaAtualizada.size()>0){
                    String id_grupo_selecionado = adapter_forum.getForuns().get(position).getId();
                    Forum forumselecionado = listaAtualizada.get(position);
                    Intent it = new Intent(getContext(), Page_Info_Grupo.class);
                    it.putExtra("grupo_info",forumselecionado);
                    it.putExtra("grupo_id",id_grupo_selecionado);
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





        Recuperar_Grupo_geral();
    return view;
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

    public void Recuperar_Grupo_geral(){
        db.collection("WeForum")
                .whereEqualTo("opcao", "grupo")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }

                        for (DocumentChange change : snapshots.getDocumentChanges()) {
                            Log.i("sdsdsd",change.getDocument().getId());
                            Forum forum_grupo = change.getDocument().toObject(Forum.class);
                            forum_grupo.setId(change.getDocument().getId());
                            //  Log.i("sdsdsd",change.getDocument().getId());
                            // Log.i("sdsdsd2",conto.getUid());
                            switch (change.getType()) {
                                case ADDED:
                                    ListaG.add(0, forum_grupo);

                                    if (ListaG.size() > 0) {
                                        //  linear_nada_cadastrado.setVisibility(View.GONE);
                                    }
                                    shimmerContainer.stopShimmerAnimation();
                                    shimmerContainer.setVisibility(View.GONE);
                                    adapter_forum.notifyDataSetChanged();
                                    Log.d("ad", "New city: " + change.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    for (Forum ct : ListaG) {

                                        if(forum_grupo.getId().equals(ct.getId())){
                                            ListaG.remove(ct);
                                            break;
                                        }
                                    }
                                    ListaG.add(0, forum_grupo);
                                    if (ListaG.size() > 0) {
                                        //linear_nada_cadastrado.setVisibility(View.GONE);
                                    }
                                    shimmerContainer.stopShimmerAnimation();
                                    shimmerContainer.setVisibility(View.GONE);
                                    adapter_forum.notifyDataSetChanged();
                                    Log.d("md", "Modified city: " + change.getDocument().getData());
                                    break;
                                case REMOVED:
                                    for (Forum ct : ListaG) {

                                        if(forum_grupo.getId().equals(ct.getId())){
                                            ListaG.remove(ct);
                                            break;
                                        }
                                    }
                                    shimmerContainer.stopShimmerAnimation();
                                    shimmerContainer.setVisibility(View.GONE);
                                    adapter_forum.notifyDataSetChanged();
                                    Log.d("rem", "Removed city: " + change.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Forum_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Chat_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Info_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Novo_Grupo_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.IOnBackPressed;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Grupo_Fragment extends Fragment implements IOnBackPressed {
    private FirebaseFirestore db;
    private Adapter_Forum_Grupo adapter_forumGrupo;
    private ArrayList<Membro_Grupo> list_membro_grupo= new ArrayList<>();
    private RecyclerView recicle_Grupo_geral,recicle_Grupo_online;
    private ArrayList<Forum> ListaG = new ArrayList<>();
    private ArrayList<Forum> ListaT = new ArrayList<>();
    private String id_Forum;
    private ShimmerFrameLayout shimmerContainer;
    private ListenerRegistration registration;
    private ListenerRegistration registration_membro;
    private SharedPreferences sPreferences=null;
    private String verificacao;
    private String identificadorUsuario;
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

        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        //efeito carregando do facebook
        shimmerContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container_Grupo);
        recicle_Grupo_geral=view.findViewById(R.id.RecycleViewGrupo_geral);
        adapter_forumGrupo = new Adapter_Forum_Grupo(ListaG,getContext());
        //Adapter
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recicle_Grupo_geral.setLayoutManager(layoutManager);
        recicle_Grupo_geral.setHasFixedSize(true);
        recicle_Grupo_geral.setAdapter(adapter_forumGrupo);

        recicle_Grupo_geral.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), recicle_Grupo_geral, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Forum> listaAtualizada = adapter_forumGrupo.getForuns();

                if(listaAtualizada.size()>0){
                    String id_grupo_selecionado = adapter_forumGrupo.getForuns().get(position).getId();
                    Forum forumselecionado = listaAtualizada.get(position);


                  //Verificando se já é membro
                    Verificar_membro(id_grupo_selecionado,forumselecionado);
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



        //chamando
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
        registration.remove();
    }

    public void Recuperar_Grupo_geral(){
        Query query= db.collection("WeForum")
                .orderBy("data", Query.Direction.ASCENDING);
        registration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                    adapter_forumGrupo.notifyDataSetChanged();
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
                                    adapter_forumGrupo.notifyDataSetChanged();
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
                                    adapter_forumGrupo.notifyDataSetChanged();
                                    Log.d("rem", "Removed city: " + change.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });

    }



private void Verificar_membro(String id_grupo,Forum forum_selecionado){
    Toast toast = Toast.makeText(getContext(), " Carregando...",Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
    toast.show();
    //Verifica se é a primeira vez para vizualizar as informacoes do grupo
    db.collection("WeForum").document(id_grupo)
            .collection("Membros").document(identificadorUsuario).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Membro_Grupo membro_grupo= documentSnapshot.toObject(Membro_Grupo.class);

                      if(documentSnapshot.exists()) {
                          assert membro_grupo != null;
                          if (!membro_grupo.getBloqueio()) {
                              Intent it = new Intent(getContext(), Page_Chat_grupo.class);
                              it.putExtra("forum_selecionado", forum_selecionado);
                              it.putExtra("id_forum_selecionado", id_grupo);
                              it.putExtra("id_admin", forum_selecionado.getIdauthor());
                              startActivity(it);

                          }else{
                              Toast.makeText(getContext(), "Voce está bloqueado Otario!", Toast.LENGTH_SHORT).show();
                          }
                      }else{

                          Intent it = new Intent(getContext(), Page_Info_Grupo.class);
                          it.putExtra("grupo_info",forum_selecionado);
                          it.putExtra("grupo_id",id_grupo);
                          startActivity(it);
                      }
                }
            });

}




    @Override
    public boolean onBackPressed() {
        return false;
    }
}

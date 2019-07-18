package com.wegeekteste.fulanoeciclano.nerdzone.Forum;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.ChatActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Info_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

public class Lista_forum_Geral extends AppCompatActivity {


    private Toolbar toolbar;
    private Dialog dialog;
    private FirebaseFirestore db;
    private Adapter_Forum adapter_forum;
    private Adapter_Forum adapter_topico;
    private RecyclerView recicle_Grupo_geral,recicle_Topico_geral;
    private ArrayList<Forum> ListaG = new ArrayList<>();
    private ArrayList<Forum> ListaT = new ArrayList<>();
    private String id_Forum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_forum__geral);


        //Toolbar
        toolbar =findViewById(R.id.toolbarsecundario_sem_foto);
        toolbar.setTitle("Lista Geral");
        setSupportActionBar(toolbar);

        //Configuracoes_Originais
        db = FirebaseFirestore.getInstance();

        recicle_Grupo_geral=findViewById(R.id.RecycleViewGrupo_geral);
        recicle_Topico_geral=findViewById(R.id.RecycleViewTOpico_Geral);
        adapter_forum = new Adapter_Forum(ListaG,this);
        adapter_topico=new Adapter_Forum(ListaT,this);
        //Adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recicle_Grupo_geral.setLayoutManager(layoutManager);
        recicle_Grupo_geral.setHasFixedSize(true);
        recicle_Grupo_geral.setAdapter(adapter_forum);

        recicle_Grupo_geral.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recicle_Grupo_geral, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Forum> listaAtualizada = adapter_forum.getForuns();

                if(listaAtualizada.size()>0){
                    String id_grupo_selecionado = adapter_forum.getForuns().get(position).getId();
                    Forum forumselecionado = listaAtualizada.get(position);
                    Intent it = new Intent(Lista_forum_Geral.this, Page_Info_Grupo.class);
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
        RecyclerView.LayoutManager layoutManager_topico = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recicle_Topico_geral.setLayoutManager(layoutManager_topico);
        recicle_Topico_geral.setHasFixedSize(true);
        recicle_Topico_geral.setAdapter(adapter_topico);




       Recuperar_Grupo_geral();
        Recuperar_Topico_geral();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menufiltro:
                //abrirConfiguracoes();
                break;
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, Lista_Forum.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }else{
                    finish();
                }
        }/*
        case android.R.id.home:
        // NavUtils.navigateUpFromSameTask(this);
        startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }else{
            finish();
        }

        break;
        */

        return super.onOptionsItemSelected(item);
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

                                    adapter_forum.notifyDataSetChanged();
                                    Log.d("rem", "Removed city: " + change.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });

    }
    public void Recuperar_Topico_geral(){
        db.collection("WeForum")
                .whereEqualTo("opcao", "topico")
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
                                    ListaT.add(0, forum_grupo);

                                    if (ListaT.size() > 0) {
                                        //  linear_nada_cadastrado.setVisibility(View.GONE);
                                    }

                                    adapter_topico.notifyDataSetChanged();
                                    Log.d("ad", "New city: " + change.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    for (Forum ct : ListaT) {

                                        if(forum_grupo.getId().equals(ct.getId())){
                                            ListaT.remove(ct);
                                            break;
                                        }
                                    }
                                    ListaT.add(0, forum_grupo);
                                    if (ListaT.size() > 0) {
                                        //linear_nada_cadastrado.setVisibility(View.GONE);
                                    }

                                    adapter_topico.notifyDataSetChanged();
                                    Log.d("md", "Modified city: " + change.getDocument().getData());
                                    break;
                                case REMOVED:
                                    for (Forum ct : ListaT) {

                                        if(forum_grupo.getId().equals(ct.getId())){
                                            ListaT.remove(ct);
                                            break;
                                        }
                                    }

                                    adapter_topico.notifyDataSetChanged();
                                    Log.d("rem", "Removed city: " + change.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });

    }
}

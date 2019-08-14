package com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Abrir_Imagem.AbrirImagem;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_membros_config;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.DividerItemDecoration;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Configuracao_Grupo_Activity extends TrocarFundo {

    private TextView nomeTitle;
    private String id_grupo_chat,nome_grupo_chat_string,img_grupo_chat_string,desc_grupo_chat_string;
    private SimpleDraweeView img_ic_grupo;
    private TextView nome_grupo,desc;
    private FirebaseFirestore db;
    private RecyclerView rec_membros;
    private ListenerRegistration registration;
    private ArrayList<Membro_Grupo> list_membro = new ArrayList<>();
    private Adapter_membros_config adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_configuracao__grupo);

        nomeTitle = findViewById(R.id.app_toolbar_title_secundario);

        Toolbar toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        nomeTitle.setText("Configuração");

        img_ic_grupo=findViewById(R.id.img_icone_grupo);
        nome_grupo=findViewById(R.id.nome_grupo_conf);
        desc=findViewById(R.id.desc_grupo_conf);
        id_grupo_chat = getIntent().getStringExtra("id_grupo");
        nome_grupo_chat_string = getIntent().getStringExtra("nome_grupo");
        desc_grupo_chat_string = getIntent().getStringExtra("desc_grupo");
        img_grupo_chat_string  = getIntent().getStringExtra("img_grupo");
        if (id_grupo_chat != null) {
            Log.i("744747", id_grupo_chat);
        }
        if(nome_grupo_chat_string!=null){
            nome_grupo.setText(nome_grupo_chat_string);
        }

        if(desc_grupo_chat_string!=null){
            desc.setText(desc_grupo_chat_string);
        }

        if(img_grupo_chat_string!=null) {

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(img_grupo_chat_string))
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setProgressiveRenderingEnabled(true)
                    .setResizeOptions(new ResizeOptions(60, 60))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            img_ic_grupo.setController(controller);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);

            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setRoundingParams(roundingParams)
                    .setProgressBarImage(new CircleProgressDrawable())
                    //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                    .build();
            img_ic_grupo.setHierarchy(hierarchy);
        }
          img_ic_grupo.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent it = new Intent(Configuracao_Grupo_Activity.this, AbrirImagem.class);
                 it.putExtra("id_foto",img_grupo_chat_string);
                  it.putExtra("nome_foto",nome_grupo_chat_string);
                  startActivity(it);
              }
          });

        //configuracoes inicias
        db = FirebaseFirestore.getInstance();
        rec_membros = findViewById(R.id.membros_grupos_config);

        adapter = new Adapter_membros_config(list_membro, getApplicationContext());
        //Adapter
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Configuracao_Grupo_Activity.this, LinearLayoutManager.VERTICAL, false);
        rec_membros.setLayoutManager(layoutManager);
        rec_membros.setAdapter(adapter);
        rec_membros.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        rec_membros.addOnItemTouchListener(new RecyclerItemClickListener(
                this, rec_membros, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Membro_Grupo> listaAtualizada = adapter.getMembros();
                if(listaAtualizada.size()>0){
                    String id_grupo_selecionado = adapter.getMembros().get(position).getId_usuario();
                    Membro_Grupo forumselecionado = listaAtualizada.get(position);


                }
            }

            @Override
            public void onLongItemClick(View view, int position) {


                AlertDialog.Builder msgbox = new AlertDialog.Builder(Configuracao_Grupo_Activity.this);
                //configurando o titulo
                msgbox.setTitle("Bloquear Usuario");
                // configurando a mensagem
                msgbox.setMessage("Deseja Realmente Bloquear o  "+adapter.getMembros().get(position).getNome_usuario()+" ?");
                // Botao negativo

                msgbox.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int wich) {
                                List<Membro_Grupo> listaAtualizada = adapter.getMembros();
                                if(listaAtualizada.size()>0){
                                    String id_membro_selecionado = adapter.getMembros().get(position).getId_usuario();
                                    Membro_Grupo forumselecionado = listaAtualizada.get(position);
                                    //Verificando se já é membro
                                    Bloquear_Membro(id_membro_selecionado,forumselecionado);
                                }
                            }

                        });


                msgbox.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int wich) {
                                dialog.dismiss();
                            }
                        });
                msgbox.show();

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void Bloquear_Membro(String id_membro_selecionado, Membro_Grupo forumselecionado) {
        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("bloqueio", true);
        db.collection("WeForum").document(id_grupo_chat)
                .collection("Membros").document(id_membro_selecionado).update(membrosMap);

        //Map to remove user from array
        final Map<String, Object> removeUserFromArrayMap = new HashMap<>();
        removeUserFromArrayMap.put("membros", FieldValue.arrayRemove(id_membro_selecionado));
        db.collection("WeForum").document(id_grupo_chat)
                .update(removeUserFromArrayMap);

        Toast.makeText(this, "Bloqueado com Sucesso!", Toast.LENGTH_SHORT).show();


    }



    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecuperarForum();
    }

    @Override
    protected void onStop() {
        super.onStop();
        registration.remove();
    }

    @Override
    protected void onPause() {
        super.onPause();
        registration.remove();
    }

    private void RecuperarForum() {
        list_membro.clear();
        Query query = db.collection("WeForum").document(id_grupo_chat)
                .collection("Membros").orderBy("admin");
        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("", "listen:error", e);
                    return;
                }
                for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                    Log.i("sdsdsd777", change.getDocument().getId());
                    Membro_Grupo membro = change.getDocument().toObject(Membro_Grupo.class);

                    switch (change.getType()) {
                        case ADDED:
                            list_membro.add(0, membro);

                            if (list_membro.size() > 0) {
                                //  linear_nada_cadastrado.setVisibility(View.GONE);
                            }

                            adapter.notifyDataSetChanged();
                            Log.d("ad", "New city: " + change.getDocument().getData());
                            break;
                        case MODIFIED:
                            for (Membro_Grupo ct : list_membro) {

                                if(membro.getId_usuario().equals(ct.getId_usuario())){
                                    list_membro.remove(ct);
                                    break;
                                }
                            }
                            list_membro.add(0, membro);
                            if (list_membro.size() > 0) {
                                //linear_nada_cadastrado.setVisibility(View.GONE);
                            }

                            adapter.notifyDataSetChanged();
                            Log.d("md", "Modified city: " + change.getDocument().getData());
                            break;
                        case REMOVED:
                            for (Membro_Grupo ct : list_membro) {

                                if(membro.getId_usuario().equals(ct.getId_usuario())){

                                    list_membro.remove(ct);
                                    break;
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Log.d("rem", "Removed city: " + change.getDocument().getData());
                            break;
                    }
                }

            }
        });

    }
}




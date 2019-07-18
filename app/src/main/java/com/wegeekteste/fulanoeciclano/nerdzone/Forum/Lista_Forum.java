package com.wegeekteste.fulanoeciclano.nerdzone.Forum;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Topico;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Lista_Forum extends AppCompatActivity  {
    private Toolbar toolbar;
    private CircleImageView icone;
    private MaterialSearchView SeachViewTopico;
    private Adapter_Forum adapter_Meus_forum;
    private FloatingActionButton botaoMaisTopicos;
    private RecyclerView recyclerView_lista_Meus_Forum;
    private ArrayList<Forum> listaForum = new ArrayList<>();
    private Dialog dialog;
    private SharedPreferences preferences = null;
    private FirebaseFirestore db;
    private String identificadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_topicos);

        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("We");
        setSupportActionBar(toolbar);


       //Configuraçoes Basicas
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        db = FirebaseFirestore.getInstance();
        recyclerView_lista_Meus_Forum = findViewById(R.id.recycleview_topico);
        botaoMaisTopicos=findViewById(R.id.buton_novo_topico);

        icone=findViewById(R.id.icone_user_toolbar);
        icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Lista_Forum.this,Lista_forum_Geral.class);
                startActivity(it);
            }
        });
        botaoMaisTopicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Lista_Forum.this, Novo_Forum.class);
                startActivity(it);
                finish();
            }
        });
        //adapter
        adapter_Meus_forum = new Adapter_Forum(listaForum,this);

        //Adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView_lista_Meus_Forum.setLayoutManager(layoutManager);
        recyclerView_lista_Meus_Forum.setHasFixedSize(true);
        recyclerView_lista_Meus_Forum.setAdapter(adapter_Meus_forum);

//Aplicar Evento click
        TrocarFundos_status_bar();


        //Botao Pesquisa
        SeachViewTopico = findViewById(R.id.materialSeachComercio);
        SeachViewTopico.setHint("Pesquisar");
        SeachViewTopico.setHintTextColor(R.color.cinzaclaro);


        RecuperarForum();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




    private void RecuperarForum(){

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
                                    Log.d("rem", "Removed city: " + change.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });

    }


    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menufiltro:
                //abrirConfiguracoes();
                break;
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
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




    private void TrocarFundos_status_bar(){
        //mudando a cor do statusbar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
            //  systemBarTintManager.setStatusBarTintDrawable(Mydrawable);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.parseColor("#1565c0"));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setNavigationBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void Dialog_Primeiravez() {
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.dialog_informacao_topico, null);
        view.findViewById(R.id.botaoentendi).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //desfaz o dialog_opcao_foto.
                dialog.dismiss();
            }
        });
        //Dialog de tela
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }


}

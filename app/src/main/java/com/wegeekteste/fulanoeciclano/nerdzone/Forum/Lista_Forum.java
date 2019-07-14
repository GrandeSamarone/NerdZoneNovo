package com.wegeekteste.fulanoeciclano.nerdzone.Forum;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Topico;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Lista_Forum extends AppCompatActivity  {
    private Toolbar toolbar;
    private CircleImageView icone;
    private DatabaseReference database,database_topico;
    private FirebaseUser usuario;
    private MaterialSearchView SeachViewTopico;
    private Adapter_Topico adapter_topico;
    private ChildEventListener ChildEventListenerperfil;
    private FloatingActionButton botaoMaisTopicos;
    private RecyclerView recyclerView_lista_topico;
    private ArrayList<Forum> listaForum = new ArrayList<>();
    private ChildEventListener valueEventListenerTopicos;
    private LinearLayout linear_nada_cadastrado;
    private Dialog dialog;
    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_topicos);

        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("We");
        setSupportActionBar(toolbar);


       //Configuraçoes Basicas
        linear_nada_cadastrado= findViewById(R.id.linear_nada_cadastrado_lista_topico);
        recyclerView_lista_topico = findViewById(R.id.recycleview_topico);
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
        adapter_topico = new Adapter_Topico(listaForum,this);

        //Adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView_lista_topico.setLayoutManager(layoutManager);
        recyclerView_lista_topico.setHasFixedSize(true);
        recyclerView_lista_topico.setAdapter(adapter_topico);

//Aplicar Evento click
        TrocarFundos_status_bar();


        //Botao Pesquisa
        SeachViewTopico = findViewById(R.id.materialSeachComercio);
        SeachViewTopico.setHint("Pesquisar");
        SeachViewTopico.setHintTextColor(R.color.cinzaclaro);





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




    private void RecuperarTopicos(){

        linear_nada_cadastrado.setVisibility(View.VISIBLE);
        listaForum.clear();
        valueEventListenerTopicos = database_topico.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Forum forum = dataSnapshot.getValue(Forum.class);
                    listaForum.add(0, forum);
               if(listaForum.size()>0){
                   linear_nada_cadastrado.setVisibility(View.GONE);
               }
                    adapter_topico.notifyDataSetChanged();


            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

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

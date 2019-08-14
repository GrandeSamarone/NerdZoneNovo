package com.wegeekteste.fulanoeciclano.nerdzone.Forum;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Forum_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.APIService;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.Forum_Fragment_principal;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.Grupo_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.Topico_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.BottomNavigationBehavior;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.IOnBackPressed;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Client;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

public class Forum_principal extends TrocarFundo implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView icone;
    private MaterialSearchView SeachViewTopico;
    private Adapter_Forum_Grupo adapter_Meus_forum;
    private FloatingActionButton botaoMaisTopicos;
    private RecyclerView recyclerView_lista_Meus_Forum;
    private ArrayList<Forum> listaForum = new ArrayList<>();
    private Dialog dialog;
    private SharedPreferences preferences = null;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);


        //Configurações Básicas
       // icone=findViewById(R.id.icone_toolbar_secundario);
        //icone.setImageResource(R.drawable.ic_add_black_24dp);
        //icone.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fontgeek.ttf");
        TextView toolbarTitle = findViewById(R.id.app_toolbar_title_secundario);
        toolbarTitle.setText(R.string.text_lista_forum);
        toolbarTitle.setTextColor(getResources().getColor(R.color.branco));
       toolbarTitle.setTypeface(typeface);


        setSupportActionBar(toolbar);




        //Botao Pesquisa
        SeachViewTopico = findViewById(R.id.materialSeachComercio);
        SeachViewTopico.setHint("Pesquisar");
        SeachViewTopico.setHintTextColor(R.color.cinzaclaro);







        //Fragmentos
        loadFragment(new Forum_Fragment_principal());
        //Navegacao Inferior
        navigation = findViewById(R.id.navigation_forum);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//escondendo navegação
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            //TODO: Perform your logic to pass back press here
            for(Fragment fragment : fragmentList){
                if(fragment instanceof IOnBackPressed){
                    ((IOnBackPressed)fragment).onBackPressed();
                }
            }
        }
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







    @Override
    public void onClick(View v) {
        switch (v.getId()){
           // case R.id.icone_toolbar_secundario :
           // Intent it = new Intent(Forum_principal.this,Lista_forum_Geral.class);
            //startActivity(it);
            //break;
        }
    }

    //Recebe Fragment
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.frame_forum, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home_forum:
                    fragment = new Forum_Fragment_principal();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_grupos:
                    fragment = new Grupo_Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_topico:
                    fragment = new Topico_Fragment();
                    loadFragment(fragment);
                    return true;

            }
            return false;
        }
    };

}

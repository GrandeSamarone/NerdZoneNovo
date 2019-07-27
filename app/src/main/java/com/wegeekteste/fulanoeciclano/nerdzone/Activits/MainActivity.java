package com.wegeekteste.fulanoeciclano.nerdzone.Activits;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.TelaInicial.MainActivityFragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.BottomNavigationBehavior;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Lista_Forum;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends TrocarFundo {
    private Toolbar toolbar;
    SharedPreferences sPreferences = null;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private DocumentReference docRef;
    private BottomNavigationView navigation;
    private static final String ARQUIVO_PREFERENCIA = "arquivoreferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Configuraçoes Originais

        db = FirebaseFirestore.getInstance();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        Log.i("sd22", identificadorUsuario);



        //Configurar abas
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", MainActivityFragment.class)
                        .add("Contatos", MainActivityFragment.class)
                        .create()
        );

        //Fragmentos
        loadFragment(new MainActivityFragment());
        //Navegacao Inferior
       navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //escondendo navegação
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());


        //Verifica se é a primeira vez da instalacao
        sPreferences = getSharedPreferences("primeiravez_Main_Activit", MODE_PRIVATE);
        if (sPreferences.getBoolean("primeiravez_Main_Activit", true)) {
            sPreferences.edit().putBoolean("primeiravez_Main_Activit", false).apply();

        }
        //Toolbar
        toolbar = findViewById(R.id.toolbarmain);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //preferences
        SharedPreference();
        //Trocando Fundo statusbar
        //TrocarFundos_status_bar();

        new TrocarFundo();
    }

    //Recebe Fragment
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContaner_Principal, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MainActivityFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation:
                    return true;
                case R.id.navigation_perfil:

                    return true;
                case R.id.navigation_voltar:

                    return true;
            }
            return false;
        }
    };


    //Pegar dados do usuario do firebase e guardar local
    private void SharedPreference() {

        docRef = db.collection("Usuarios").document(identificadorUsuario);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nome", usuario.getNome());
                editor.putString("foto_usuario", usuario.getFoto());
                Log.i("sdosdo0", usuario.getFoto());
                Toast.makeText(MainActivity.this, usuario.getNome() + " pegou", Toast.LENGTH_SHORT).show();
                editor.commit();

            }
        });


    }


    @Override
    public void onStop() {
        super.onStop();

    }




}


package com.wegeekteste.fulanoeciclano.nerdzone.Activits;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.MinhaConta.Minha_Conta_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.MainActivityFragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.BottomNavigationBehavior;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;


public class MainActivity extends AppCompatActivity {

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


        //preferences
        SharedPreference();
        //Trocando Fundo statusbar
        //TrocarFundos_status_bar();
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
                    fragment = new Minha_Conta_Fragment();
                    loadFragment(fragment);
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


package com.wegeekteste.fulanoeciclano.nerdzone.Activits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.wegeekteste.fulanoeciclano.nerdzone.Autenticacao.LoginActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.Caixa_Entrada_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.Chat_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.MinhaConta.Minha_Conta_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.MainActivityFragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.BottomNavigationBehavior;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.IOnBackPressed;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import static com.tangxiaolv.telegramgallery.Utils.AndroidUtilities.showToast;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sPreferences = null;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private DocumentReference docRef;
    private BottomNavigationView navigation;
    private static final String ARQUIVO_PREFERENCIA = "arquivoreferencia";
    private Toast toast = null;
    int counter=0;
    private Fragment fragment;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    FirebaseUser fuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //Configuraçoes Originai
        db = FirebaseFirestore.getInstance();
        //Fragmentos
        loadFragment(new MainActivityFragment());
        //Navegacao Inferior
       navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //escondendo navegação
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }else{
                identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
                Log.i("sodksodk",identificadorUsuario);
                SharedPreference();
            } };

        //Verifica se é a primeira vez da instalacao
        sPreferences =getSharedPreferences("primeiravez_Main_Activit", MODE_PRIVATE);
        if (sPreferences.getBoolean("primeiravez_Main_Activit", true)) {
            sPreferences.edit().putBoolean("primeiravez_Main_Activit", false).apply();
        }

        //Trocando Fundo statusbar
        //TrocarFundos_status_bar();
    }


    //Pegar dados do usuario do firebase e guardar local
    private void SharedPreference() {

        docRef = db.collection("Usuarios").document(identificadorUsuario);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                SharedPreferences sharedPreferences =getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nome", usuario.getNome());
                editor.putString("foto_usuario", usuario.getFoto());
                editor.putString("token", usuario.getToken());
                Log.i("sdosdo0", usuario.getFoto());
                // Toast.makeText(MainActivity.this, usuario.getNome() + " pegou", Toast.LENGTH_SHORT).show();
                editor.commit();

            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("sds74485",fuser.getUid());
    }



    //Recebe Fragment
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.frameContaner_Principal, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MainActivityFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_entrada:
                    fragment = new Caixa_Entrada_Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_chat_pessoal:
                    fragment = new Chat_Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_perfil:
                    fragment = new Minha_Conta_Fragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }
    @Override
    protected void onPause() {
        killToast();
        super.onPause();
    }

    //Fechar o app quando o usuario aperta 2 vezes o botao voltar
    @Override
    public void onBackPressed() {
        counter+=1;
        showToast("Press Novamente para sair");
        if(counter==2){
            this.finish();
        }
    }



    private void showToast(String message) {
        if (this.toast == null) {
            // Create toast if found null, it would he the case of first call only
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else if (this.toast.getView() == null) {
            // Toast not showing, so create new one
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else {
            // Updating toast message is showing
            this.toast.setText(message);
        }

        // Showing toast finally
        this.toast.show();
    }

    private void killToast() {
        if (this.toast != null) {
            this.toast.cancel();
        }
    }



}


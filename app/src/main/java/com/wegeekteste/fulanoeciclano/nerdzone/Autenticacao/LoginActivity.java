package com.wegeekteste.fulanoeciclano.nerdzone.Autenticacao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.Politica_Privacidade.Politica_PrivacidadeActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Splash.TelaSplash;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user;
    private TextView textoprivacidade;
    private com.shobhitpuri.custombuttons.GoogleSignInButton botaologin;
    private Usuario usuario;
    private AlertDialog dialog;
    private SharedPreferences sPreferences = null;
    private String identificadorUsuario;
    Usuario usuarioLogado;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private CollectionReference citiesRef;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //fundo
        TrocarFundos_status_bar();

        //Configuracoes Originais
        db = FirebaseFirestore.getInstance();
        citiesRef=db.collection("Usuarios");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Dialog();
        textoprivacidade = findViewById(R.id.textView);
        textoprivacidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, Politica_PrivacidadeActivity.class);
                startActivity(it);
            }
        });


        botaologin = findViewById(R.id.sign_in_button);
        botaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = auth.getCurrentUser();
        Conferir(currentUser);
    }

    private void Conferir(FirebaseUser currentUser) {

        if (currentUser != null) {
            Toast.makeText(this, currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
               Intent it = new Intent(LoginActivity.this,TelaSplash.class);
               startActivity(it);
               finish();
    }else{
            Toast.makeText(this, "entrou aqui", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            dialog.dismiss();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Erro, Verifique sua Conexão com a internet e tente Novamente.", Toast.LENGTH_LONG).show();
                Log.i("sdoaskaok", String.valueOf(e));

                dialog.dismiss();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //  Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        dialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            //Veirica se a conta existe no Database
                            FirebaseUser user = auth.getCurrentUser();
                            VerificandoCadastro();

                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this,
                                    "Tente Novamente mais tarde", Toast.LENGTH_LONG).show();
                            // If sign in fails, display a message to the user.
                          /* Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           */
                        }

                        // ...
                    }
                });
    }
    
        public void VerificandoCadastro () {
            //identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
            FirebaseUser user = auth.getCurrentUser();
            /*Query query = citiesRef.whereEqualTo("id", user.getUid());
            if (!query.equals(null)) {
                dialog.dismiss();
                Intent its = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(its);
                finish();
            } */
                dialog.dismiss();
                usuario = new Usuario();
                usuario.setId(user.getUid());
                usuario.setNome(user.getDisplayName());
                usuario.setFoto(String.valueOf(user.getPhotoUrl()));
                usuario.setCapa("");
                usuario.setSeguidores(usuario.getSeguidores());
                usuario.setSeguindo(usuario.getSeguindo());
                usuario.setContos(usuario.getContos());
                usuario.setTopicos(usuario.getTopicos());
                usuario.setArts(usuario.getArts());
                usuario.setComercio(usuario.getComercio());
                usuario.setEvento(usuario.getEvento());
                usuario.setTipoconta(user.getEmail());
                usuario.setTiposuario("usuario");
                //   String  identificadorUsuario = Base64Custom.codificarBase64(usuario.getNome());
                usuario.salvar();

                Intent it = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(it);
                finish();

        }

    private void Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
        final View view = layoutInflater.inflate(R.layout.dialog_carregando_gif_comscroop, null);
        ImageView imageViewgif = view.findViewById(R.id.gifimage);

        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.gif_briguinha)
                .into(imageViewgif);
        builder.setView(view);
        dialog = builder.create();

    }

    //Trocar a cor do topo

    private void TrocarFundos_status_bar() {
        //mudando a cor do statusbar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_login);
        }
        if (Build.VERSION.SDK_INT <= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_login);
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
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_login);
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

}
package com.wegeekteste.fulanoeciclano.nerdzone.Fragments.MinhaConta;


import android.Manifest;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.Permissoes;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Minha_Conta_Fragment extends Fragment {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private ImageButton imageButtonCamera,imageButtonGaleria;
    private static final int SELECAO_CAMERA=100;
    private static final int SELECAO_CORTADA=300;
    private static final int SELECAO_CORTADA_CAPA=350;
    private static final int SELECAO_GALERIA=200;
    private static final int SELECAO_CAPA=50;
    private static final int SELECAO_ICONE=300;
    private static final int MINHA_CONTA=12;
    private CircleImageView circleImageViewperfil;
    private SimpleDraweeView capa_perfil;
    private LinearLayout btn_voltar,topicoclick,contoclick,fanatsclick,seguidor_click;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private TextView nome,fraserapida,n_topicos,n_contos,n_fanats,n_seguidores;
    private FloatingActionButton botaotrocarfoto;
    private Usuario usuarioLogado,perfil;
    private Usuario user;
    private FirebaseUser usuario;
    private RelativeLayout relative;
    private AlertDialog alerta;
    private ViewPager mViewPager;
    private DatabaseReference database;
    private com.google.firebase.database.ChildEventListener ChildEventListener;
    private SharedPreferences nome_usuario,foto_usuario;
    private static  final String ARQUIVO_PREFERENCIA ="arquivoreferencia";
    public Minha_Conta_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        View view= inflater.inflate(R.layout.fragment_minha__conta_, container, false);

        //configuracoes iniciais
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuario = UsuarioFirebase.getUsuarioAtual();
        database = ConfiguracaoFirebase.getDatabase().getReference().child("usuarios");
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        circleImageViewperfil=view.findViewById(R.id.circleImageViewFotoPerfil_frag);
        nome=view.findViewById(R.id.nameuser_minha_conta);
        //  fraserapida = findViewById(R.id.fraserapida_perfil);
        // capa_perfil= findViewById(R.id.capameuperfil);
        // capa_perfil.setOnClickListener(this);
        usuarioLogado=UsuarioFirebase.getDadosUsuarioLogado();
        user = new Usuario();


        //navigation.setItemIconTintList(null);
        // navigation.getMenu().findItem(R.id.navigation_perfil).setIcon(R.drawable.favicon);



        //validar permissoes
        Permissoes.validarPermissoes(permissoesNecessarias,getActivity(),1);

        CarregarDados_do_Usuario();
    return view;
    }

    private void CarregarDados_do_Usuario(){
        nome_usuario = this.getActivity().getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
        Log.i("oskdoskd99",nome_usuario.getString("nome",""));
        nome.setText(nome_usuario.getString("nome",""));

        foto_usuario = this.getActivity().getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
        String foto =foto_usuario.getString("foto_usuario","");
        Glide.with(getContext())
                .load(foto)
                .into(circleImageViewperfil);

    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}

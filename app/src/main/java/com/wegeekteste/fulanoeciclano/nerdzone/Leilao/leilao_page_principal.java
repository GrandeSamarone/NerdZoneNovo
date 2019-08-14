package com.wegeekteste.fulanoeciclano.nerdzone.Leilao;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Evento.Cadastrar_Novo_Evento;
import com.wegeekteste.fulanoeciclano.nerdzone.Evento.Evento_Lista;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Forum_principal;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Configuracao_Grupo_Activity;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Chat_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.Forum_Fragment_principal;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.Grupo_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.Topico_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.BottomNavigationBehavior;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

public class leilao_page_principal extends TrocarFundo {

    private Toolbar toolbar;
    private FloatingActionButton navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leilai_page_principal);
        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fontgeek.ttf");
        TextView toolbarTitle = findViewById(R.id.app_toolbar_title_secundario);
        toolbarTitle.setText(R.string.text_lista_leilao);
        toolbarTitle.setTextColor(getResources().getColor(R.color.branco));
        toolbarTitle.setTypeface(typeface);
        setSupportActionBar(toolbar);


        navigation = findViewById(R.id.fab_novo_leilao);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(leilao_page_principal.this, Novo_Leilao_Activity.class);
                startActivity(it);

            }
        });
        //Fragmentos
        loadFragment(new Page_Inicial_leiao_Fragment());
        //Navegacao Inferior


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //Recebe Fragment
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.frame_leilao, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    //botao Pesquisar
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_leilao,menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        return super.onCreateOptionsMenu(menu);
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
                } else {
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }


}

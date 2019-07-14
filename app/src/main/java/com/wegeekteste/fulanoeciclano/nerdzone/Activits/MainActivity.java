package com.wegeekteste.fulanoeciclano.nerdzone.Activits;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial.AdapterMercado;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial.Adapter_Conto_pag_inicial;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial.Adapter_FanArtsInicial;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial.EventoAdapterPagInicial;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.AdapterPagInicial.TopicoAdapterPagInicial;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Conto.ListaConto;
import com.wegeekteste.fulanoeciclano.nerdzone.Evento.DetalheEvento;
import com.wegeekteste.fulanoeciclano.nerdzone.FanArts.Lista_Arts;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Mercado.Detalhe_Mercado;
import com.wegeekteste.fulanoeciclano.nerdzone.Mercado.MercadoActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Comercio;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Evento;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArts;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Teste_firestore.firestore;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Detalhe_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Lista_Forum;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity  {

    private RecyclerView recyclerViewListaGibiMercado;
    private RecyclerView recyclerViewArts;
    private RecyclerView recyclerViewListaTopico;
    private RecyclerView recyclerViewListaConto;
    private RecyclerView recyclerVieweventos;
    private Adapter_Conto_pag_inicial adapterConto;
    private AdapterMercado adapterMercado;
    private EventoAdapterPagInicial adapterEvento;
    private TopicoAdapterPagInicial adapterTopico;
    private Adapter_FanArtsInicial adapterArte;
    private List<Comercio> listaGibiComercio = new ArrayList<>();
    private List<FanArts> listaArt = new ArrayList<>();
    private ArrayList<Forum> listaForum = new ArrayList<>();
    private ArrayList<Evento> ListaEvento = new ArrayList<>();
    private ArrayList<Conto> ListaContos = new ArrayList<>();
    private TextView maiseventoTxt,maiscomercioTxt,maistopicoTxt,maiscontoTxt,maisfanartsTxt;
    private Toolbar toolbar;
  private StorageReference storageReference;
    SharedPreferences sPreferences = null;
    private Dialog dialog;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Configuraçoes Originais
        db = FirebaseFirestore.getInstance();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        botoes_Mais();
        RecuperarConto();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        Log.i("sd22",identificadorUsuario);
        //todas configuraões do recycleview
        recyclerViewListaConto = findViewById(R.id.RecycleViewConto);
        recyclerViewListaGibiMercado = findViewById(R.id.RecycleViewMercado);
        recyclerVieweventos = findViewById(R.id.RecycleViewEventos);
        recyclerViewListaTopico = findViewById(R.id.RecycleViewTopicos);
        recyclerViewArts = findViewById(R.id.RecycleViewFanArts);



        //Configurar recycleView Evento
        LinearLayoutManager layoutManagerConto = new LinearLayoutManager(
                MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaConto.setLayoutManager(layoutManagerConto);
        recyclerViewListaConto.setHasFixedSize(true);
        adapterConto = new Adapter_Conto_pag_inicial(ListaContos,MainActivity.this);
        recyclerViewListaConto.setAdapter(adapterConto);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerVieweventos.setLayoutManager(layoutManager);
        recyclerVieweventos.setHasFixedSize(true);
        adapterEvento = new EventoAdapterPagInicial(ListaEvento,MainActivity.this);
        recyclerVieweventos.setAdapter(adapterEvento);
        //Configurar recycleView Mercado
        LinearLayoutManager layoutManagerMarvel = new LinearLayoutManager
                (MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaGibiMercado.setLayoutManager(layoutManagerMarvel);
        recyclerViewListaGibiMercado.setHasFixedSize(true);
        adapterMercado=new AdapterMercado(listaGibiComercio,MainActivity.this);
        recyclerViewListaGibiMercado.setAdapter(adapterMercado);
        //Configurar recycleView TOpico
        LinearLayoutManager layoutManagertopico = new LinearLayoutManager(
                MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewListaTopico.setLayoutManager(layoutManagertopico);
        recyclerViewListaTopico.setHasFixedSize(true);
        adapterTopico = new TopicoAdapterPagInicial(listaForum,MainActivity.this);
        recyclerViewListaTopico.setAdapter(adapterTopico);

        LinearLayoutManager layoutManagerArt = new LinearLayoutManager(
                MainActivity.this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewArts.setLayoutManager(layoutManagerArt);
        recyclerViewArts.setHasFixedSize(true);
        layoutManagerArt.setReverseLayout(true);
        layoutManagerArt.setStackFromEnd(true);
        adapterArte = new Adapter_FanArtsInicial(listaArt,MainActivity.this);
        recyclerViewArts.setAdapter(adapterArte);

        //Verifica se é a primeira vez da instalacao
        sPreferences = getSharedPreferences("primeiravez_Main_Activit", MODE_PRIVATE);
        if (sPreferences.getBoolean("primeiravez_Main_Activit", true)) {
            sPreferences.edit().putBoolean("primeiravez_Main_Activit", false).apply();

        }
        //Toolbar
        toolbar =findViewById(R.id.toolbarmain);
        // toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        //Trocando Fundo statusbar
        TrocarFundos_status_bar();


    }








    @Override
    public void onStop() {
        super.onStop();

     }



    //recupera e nao deixa duplicar
    public void RecuperarEvento(){
        ListaEvento.clear();
    }


    //recupera e nao deixa duplicar
    public void RecuperarMercado(){

    }

    //recupera e nao deixa duplicar
    public void RecuperarTopico() {

    }
    public void RecuperarConto(){
        db.collection("Conto")
               // .whereEqualTo("descricao", "bb")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }

                        for (DocumentChange change : snapshots.getDocumentChanges()) {
                            Conto conto = change.getDocument().toObject(Conto.class);
                            conto.setUid(change.getDocument().getId());
                            Log.i("sdsdsd",change.getDocument().getId());
                            Log.i("sdsdsd2",conto.getUid());
                            switch (change.getType()) {
                                case ADDED:
                                    ListaContos.add(0, conto);

                                    if (ListaContos.size() > 0) {
                                        //linear_nada_cadastrado.setVisibility(View.GONE);
                                    }

                                    adapterConto.notifyDataSetChanged();
                                    Log.d("ad", "New city: " + change.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    for (Conto ct : ListaContos) {

                                        if(conto.getUid().equals(ct.getUid())){
                                            ListaContos.remove(ct);
                                            break;
                                        }
                                    }
                                    ListaContos.add(0, conto);
                                    if (ListaContos.size() > 0) {
                                       // linear_nada_cadastrado.setVisibility(View.GONE);
                                    }

                                    adapterConto.notifyDataSetChanged();
                                    Log.d("md", "Modified city: " + change.getDocument().getData());
                                    break;
                                case REMOVED:
                                    for (Conto ct : ListaContos) {

                                        if(conto.getUid().equals(ct.getUid())){
                                            ListaContos.remove(ct);
                                            break;
                                        }
                                    }

                                    adapterConto.notifyDataSetChanged();
                                    Log.d("rem", "Removed city: " + change.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }


    public void RecuperarArt(){


    }

    private void Recycleview(){

        recyclerViewListaTopico.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerViewListaTopico, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Forum> listForumAtualizado = adapterTopico.getForums();

                if (listForumAtualizado.size() > 0) {
                    Forum topicoselecionado = listForumAtualizado.get(position);
                    Intent it = new Intent(MainActivity.this, Detalhe_Forum.class);
                    it.putExtra("topicoselecionado", topicoselecionado);
                    startActivity(it);

                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        recyclerViewListaConto.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerViewListaConto, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Conto> listContoAtualizado = adapterConto.getContos();

                if (listContoAtualizado.size() > 0) {
                    Conto contoselecionado = listContoAtualizado.get(position);
                    Intent it = new Intent(MainActivity.this, com.marlostrinidad.wegeek.nerdzone.Conto.Detalhe_conto.class);
                    it.putExtra("contoselecionado", contoselecionado);
                    startActivity(it);

                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        recyclerViewListaGibiMercado.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerViewListaGibiMercado, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Comercio mercadoselecionado = listaGibiComercio.get(position);
                Intent it = new Intent(MainActivity.this, Detalhe_Mercado.class);
                it.putExtra("mercadoelecionado",mercadoselecionado);
                startActivity(it);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        recyclerVieweventos.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerVieweventos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Evento eventoselecionado = ListaEvento.get(position);
                Intent it = new Intent(MainActivity.this, DetalheEvento.class);
                it.putExtra("id_do_evento",eventoselecionado.getUid());
                it.putExtra("UR_do_evento",eventoselecionado.getEstado());
                startActivity(it);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
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
        if (Build.VERSION.SDK_INT <= 19) {
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




    private void botoes_Mais(){
        maiseventoTxt = findViewById(R.id.maisevento);
        maiseventoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, firestore.class);
                startActivity(it);
            }
        });

        maiscomercioTxt= findViewById(R.id.maiscomercio);
        maiscomercioTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent( MainActivity.this, MercadoActivity.class);
                startActivity(it);
            }
        });
        maistopicoTxt = findViewById(R.id.maistopicos);
        maistopicoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, Lista_Forum.class);
                startActivity(it);
            }
        });
        maiscontoTxt = findViewById(R.id.maisconto);
        maiscontoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, ListaConto.class);
                startActivity(it);
            }
        });
        maisfanartsTxt = findViewById(R.id.maisfanart);
        maisfanartsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, Lista_Arts.class);
                startActivity(it);
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


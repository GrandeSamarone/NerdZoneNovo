package com.wegeekteste.fulanoeciclano.nerdzone.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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
import com.wegeekteste.fulanoeciclano.nerdzone.Autenticacao.LoginActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Conto.ListaConto;
import com.wegeekteste.fulanoeciclano.nerdzone.Evento.DetalheEvento;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Forum_principal;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.MinhaConta.Minha_Conta_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.Leilao.Page_Inicial_leiao_Fragment;
import com.wegeekteste.fulanoeciclano.nerdzone.HQ.Pag_producao_hq;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.IOnBackPressed;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.RecyclerItemClickListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Leilao.leilao_page_principal;
import com.wegeekteste.fulanoeciclano.nerdzone.Mercado.Detalhe_Mercado;
import com.wegeekteste.fulanoeciclano.nerdzone.Mercado.MercadoActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Comercio;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Evento;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.FanArts;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment   implements IOnBackPressed{


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
    private TextView maiseventoTxt, maiscomercioTxt, maistopicoTxt, maiscontoTxt, maisfanartsTxt,maisLeilao;
    private RelativeLayout clickMais_Leilao;
    private Toolbar toolbar;
    private StorageReference storageReference;
    SharedPreferences sPreferences = null;
    private Dialog dialog;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private DocumentReference docRef;
    private BottomNavigationView navigation;
    private static final String ARQUIVO_PREFERENCIA = "arquivoreferencia";
    private FirebaseAuth.AuthStateListener authStateListener;
    private  Fragment fragment;
    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                         // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);


        //Toolbar
        toolbar = getActivity().findViewById(R.id.toolbarmain);
        getActivity().setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        //Configuraçoes Originais
        db = FirebaseFirestore.getInstance();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
       // botoes_Mais();
        RecuperarConto();
       TrocarFundo();

        //todas configuraões do recycleview
        recyclerViewListaConto = view.findViewById(R.id.RecycleViewhistoria);
        recyclerViewListaGibiMercado = view.findViewById(R.id.RecycleViewMercado);
        recyclerVieweventos = view.findViewById(R.id.RecycleViewEventos);
        recyclerViewListaTopico = view.findViewById(R.id.RecycleViewForum);
        recyclerViewArts = view.findViewById(R.id.RecycleViewgaleria);


        //Configurar recycleView Evento
        LinearLayoutManager layoutManagerConto = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewListaConto.setLayoutManager(layoutManagerConto);
        recyclerViewListaConto.setHasFixedSize(true);
        adapterConto = new Adapter_Conto_pag_inicial(ListaContos, getActivity());
        recyclerViewListaConto.setAdapter(adapterConto);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerVieweventos.setLayoutManager(layoutManager);
        recyclerVieweventos.setHasFixedSize(true);
        adapterEvento = new EventoAdapterPagInicial(ListaEvento, getActivity());
        recyclerVieweventos.setAdapter(adapterEvento);
        //Configurar recycleView Mercado
        LinearLayoutManager layoutManagerMarvel = new LinearLayoutManager
                (getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewListaGibiMercado.setLayoutManager(layoutManagerMarvel);
        recyclerViewListaGibiMercado.setHasFixedSize(true);
        adapterMercado = new AdapterMercado(listaGibiComercio, getActivity());
        recyclerViewListaGibiMercado.setAdapter(adapterMercado);
        //Configurar recycleView TOpico
        LinearLayoutManager layoutManagertopico = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewListaTopico.setLayoutManager(layoutManagertopico);
        recyclerViewListaTopico.setHasFixedSize(true);
        adapterTopico = new TopicoAdapterPagInicial(listaForum, getActivity());
        recyclerViewListaTopico.setAdapter(adapterTopico);

        LinearLayoutManager layoutManagerArt = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewArts.setLayoutManager(layoutManagerArt);
        recyclerViewArts.setHasFixedSize(true);
        layoutManagerArt.setReverseLayout(true);
        layoutManagerArt.setStackFromEnd(true);
        adapterArte = new Adapter_FanArtsInicial(listaArt, getActivity());
        recyclerViewArts.setAdapter(adapterArte);




        maiseventoTxt = view.findViewById(R.id.maisevento);
        maiscomercioTxt = view.findViewById(R.id.maiscomercio);
        maistopicoTxt = view.findViewById(R.id.maisForum);
        maiscontoTxt = view.findViewById(R.id.maishistorias);
        maisfanartsTxt = view.findViewById(R.id.maisgaleria);

        botoes_Mais();




        authStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }else{
                identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

            }
        };


        return view;
    }




    @Override
    public void onPause() {
        super.onPause();
       TrocarFundo();
    }

    //recupera e nao deixa duplicar
    public void RecuperarEvento() {
        ListaEvento.clear();
    }


    //recupera e nao deixa duplicar
    public void RecuperarMercado() {

    }

    //recupera e nao deixa duplicar
    public void RecuperarTopico() {

    }

    public void RecuperarConto() {
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
                            conto.setId(change.getDocument().getId());
                            Log.i("sdsdsd", change.getDocument().getId());
                            Log.i("sdsdsd2", conto.getId());
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

                                        if (conto.getId().equals(ct.getId())) {
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

                                        if (conto.getId().equals(ct.getId())) {
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


    public void RecuperarArt() {


    }

    private void Recycleview() {

        recyclerViewListaTopico.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerViewListaTopico, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Forum> listForumAtualizado = adapterTopico.getForums();

                if (listForumAtualizado.size() > 0) {
                    Forum topicoselecionado = listForumAtualizado.get(position);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
        recyclerViewListaConto.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerViewListaConto, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Conto> listContoAtualizado = adapterConto.getContos();

                if (listContoAtualizado.size() > 0) {
                    Conto contoselecionado = listContoAtualizado.get(position);
                    Intent it = new Intent(getContext(), com.marlostrinidad.wegeek.nerdzone.Conto.Detalhe_conto.class);
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
        recyclerViewListaGibiMercado.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerViewListaGibiMercado, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Comercio mercadoselecionado = listaGibiComercio.get(position);
                Intent it = new Intent(getContext(), Detalhe_Mercado.class);
                it.putExtra("mercadoelecionado", mercadoselecionado);
                startActivity(it);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        recyclerVieweventos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerVieweventos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Evento eventoselecionado = ListaEvento.get(position);
                Intent it = new Intent(getContext(), DetalheEvento.class);
                it.putExtra("id_do_evento", eventoselecionado.getUid());
                it.putExtra("UR_do_evento", eventoselecionado.getEstado());
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

    //Recebe Fragment
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContaner_Principal, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void botoes_Mais() {
        maiseventoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), Pag_producao_hq.class);
                startActivity(it);
            }
        });
        maiscomercioTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), MercadoActivity.class);
                startActivity(it);
            }
        });

        maistopicoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getContext(), Forum_principal.class);
                startActivity(it);
            }
        });
        maiscontoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), ListaConto.class);
                startActivity(it);
            }
        });
        maisfanartsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new Minha_Conta_Fragment();
                loadFragment(fragment);
            }
        });

    }



    public void TrocarFundo(){
        //mudando a cor doo statusbar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(  getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(  getActivity());
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(  getActivity());
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.drawable.gradiente_toolbarstatusbar);
            //  systemBarTintManager.setStatusBarTintDrawable(Mydrawable);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            getActivity().getWindow().setNavigationBarColor(Color.parseColor("#1565c0"));
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(  getActivity());
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

    @Override
    public boolean onBackPressed() {

        return false;
    }
}


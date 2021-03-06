package com.wegeekteste.fulanoeciclano.nerdzone.Conto;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListaConto extends AppCompatActivity  {

    private Toolbar toolbar;
    private CircleImageView icone;
    private FirebaseUser usuario;
    private MaterialSearchView SeachViewConto;
    private Adapter_Conto adapter_conto;
    private ChildEventListener ChildEventListenerconto;
    private FloatingActionButton botaoMaisconto;
    private RecyclerView recyclerView_lista_conto;
    private ArrayList<Conto> Listaconto = new ArrayList<>();
    private LinearLayout linear_nada_cadastrado;
    private SharedPreferences preferences = null;
    private Dialog dialog;
    private  FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_conto);
        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("Contos");
        setSupportActionBar(toolbar);
         db = FirebaseFirestore.getInstance();
      RecuperarContos();

        preferences = getSharedPreferences("primeiravezconto", MODE_PRIVATE);
        if (preferences.getBoolean("primeiravezconto", true)) {
            preferences.edit().putBoolean("primeiravezconto", false).apply();
            Dialog_Primeiravez();
        }
       //Configuraçoes Basicas
        linear_nada_cadastrado = findViewById(R.id.linear_nada_cadastrado_conto);
        recyclerView_lista_conto = findViewById(R.id.recycleview_conto);
        botaoMaisconto=findViewById(R.id.buton_novo_conto);
        botaoMaisconto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaConto.this,Novo_Conto.class);
                startActivity(it);
                finish();
            }
        });
        //adapter
        adapter_conto = new Adapter_Conto(Listaconto,this);

        //Adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView_lista_conto.setLayoutManager(layoutManager);
        recyclerView_lista_conto.setHasFixedSize(true);
        recyclerView_lista_conto.setAdapter(adapter_conto);
        TrocarFundos_status_bar();


        //Botao Pesquisa
        SeachViewConto = findViewById(R.id.materialSeachComercio);
        SeachViewConto.setHint("Pesquisar");
        SeachViewConto.setHintTextColor(R.color.cinzaclaro);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }






    private void RecuperarContos(){
        db.collection("Conto")
                //.whereEqualTo("descricao", "bb")
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
                                    Log.i("sdsdsd",change.getDocument().getId());
                              Log.i("sdsdsd2",conto.getId());
                                switch (change.getType()) {
                                    case ADDED:
                                        Listaconto.add(0, conto);

                                        if (Listaconto.size() > 0) {
                                            linear_nada_cadastrado.setVisibility(View.GONE);
                                        }

                                        adapter_conto.notifyDataSetChanged();
                                        Log.d("ad", "New city: " + change.getDocument().getData());
                                        break;
                                    case MODIFIED:
                                        for (Conto ct : Listaconto) {

                                        if(conto.getId().equals(ct.getId())){
                                        Listaconto.remove(ct);
                                        break;
                                        }
                                        }
                                        Listaconto.add(0, conto);
                                        if (Listaconto.size() > 0) {
                                            linear_nada_cadastrado.setVisibility(View.GONE);
                                        }

                                        adapter_conto.notifyDataSetChanged();
                                        Log.d("md", "Modified city: " + change.getDocument().getData());
                                        break;
                                    case REMOVED:
                                        for (Conto ct : Listaconto) {

                                            if(conto.getId().equals(ct.getId())){
                                                Listaconto.remove(ct);
                                                break;
                                            }
                                        }

                                        adapter_conto.notifyDataSetChanged();
                                        Log.d("rem", "Removed city: " + change.getDocument().getData());
                                        break;
                                }
                            }
                        }
                });

     /*   db.collection("Conto")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("", document.getId() + " => " + document.getData());
                                Conto conto = document.toObject(Conto.class);
                                Listaconto.add(0, conto);
                                if(Listaconto.size()>0){
                                    linear_nada_cadastrado.setVisibility(View.GONE);
                                }

                                adapter_conto.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });
*/

        /*

        linear_nada_cadastrado.setVisibility(View.VISIBLE);
        Listaconto.clear();
        valueEventListenerConto = database_conto.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Conto conto = dataSnapshot.getValue(Conto.class);
                Listaconto.add(0, conto);
                if(Listaconto.size()>0){
                    linear_nada_cadastrado.setVisibility(View.GONE);
                }

                adapter_conto.notifyDataSetChanged();


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
*/

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
        }


        return super.onOptionsItemSelected(item);
    }


    private void Dialog_Primeiravez() {
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.dialog_informacao_contos, null);
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

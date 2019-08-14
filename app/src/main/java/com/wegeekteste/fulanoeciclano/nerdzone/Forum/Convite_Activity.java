package com.wegeekteste.fulanoeciclano.nerdzone.Forum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Convites;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Chat_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Chat_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_solicitacao_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

public class Convite_Activity extends TrocarFundo {

    private Toolbar toolbar;
    private TextView toolbarnome;
    private RecyclerView recyclerView_convite;
    private Adapter_Convites adapter_convites;
    private ArrayList<Membro_solicitacao_grupo> list_membro=new ArrayList<>();
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private ListenerRegistration registration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convite_);

       toolbar=findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarnome=findViewById(R.id.app_toolbar_title_secundario);
        toolbarnome.setText("Convites");

        //Configuracoes Originais
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        db = FirebaseFirestore.getInstance();
        recyclerView_convite=findViewById(R.id.recicle_convites);

       adapter_convites=new Adapter_Convites(list_membro,getApplicationContext());
        //Adapter
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Convite_Activity.this, LinearLayoutManager.VERTICAL,false);
        recyclerView_convite.setLayoutManager(layoutManager);
        recyclerView_convite.setAdapter(adapter_convites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Recuperar_Convites();

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();
                break;
            /*case R.id.menu_enviar_img:
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .getIntent(Page_Chat_grupo.this);
                startActivityForResult(intent, SELECAO_GALERIA);
                              break;
        */
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registration.remove();

    }

    @Override
    protected void onStop() {
        super.onStop();
        registration.remove();
    }

    private void Recuperar_Convites(){
        list_membro.clear();

        Query query=  db
                .collection("Permissao_Grupo").whereEqualTo("id_admin_grupo",identificadorUsuario);
        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("", "listen:error", e);
                    return;
                }

                for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                    Log.i("sdsdsd777",change.getDocument().getId());
                    Membro_solicitacao_grupo membro=change.getDocument().toObject(Membro_solicitacao_grupo.class);
                    membro.setId(change.getDocument().getId());

                    switch (change.getType()) {
                        case ADDED:
                            list_membro.add(0, membro);

                            if (list_membro.size() > 0) {
                                //  linear_nada_cadastrado.setVisibility(View.GONE);
                            }

                            adapter_convites.notifyDataSetChanged();
                            Log.d("ad", "New city: " + change.getDocument().getData());
                            break;
                        case MODIFIED:
                            for (Membro_solicitacao_grupo ct : list_membro) {

                                if(membro.getId().equals(ct.getId())){
                                    list_membro.remove(ct);
                                    break;
                                }
                            }
                            list_membro.add(0, membro);
                            if (list_membro.size() > 0) {
                                //linear_nada_cadastrado.setVisibility(View.GONE);
                            }

                            adapter_convites.notifyDataSetChanged();
                            Log.d("md", "Modified city: " + change.getDocument().getData());
                            break;
                        case REMOVED:
                            for (Membro_solicitacao_grupo ct : list_membro) {

                                if(membro.getId().equals(ct.getId())){
                                    list_membro.remove(ct);
                                    break;
                                }
                            }

                            adapter_convites.notifyDataSetChanged();
                            Log.d("rem", "Removed city: " + change.getDocument().getData());
                            break;
                    }
                }
            }
        });

            }



}

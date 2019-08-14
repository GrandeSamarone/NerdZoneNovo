package com.wegeekteste.fulanoeciclano.nerdzone.HQ;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_HQ;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes.Adapter_HQ_Producao;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

public class Producaohq_continuacao extends TrocarFundo {


    private EditText titulo,descricao;
    private RecyclerView rec_HQ;
    private ArrayList<HQ> lista_img = new ArrayList<>();
    private String id_hq;
    private FirebaseFirestore db;
    private Adapter_HQ adapter_hq_producao;
    private ListenerRegistration registration;
    private Toolbar toolbar;
    private TextView toolbarnome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producaohq_continuacao);

        toolbar=findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarnome=findViewById(R.id.app_toolbar_title_secundario);
        toolbarnome.setText("Quase lá");
        titulo=findViewById(R.id.nome_hq_producao);
        descricao=findViewById(R.id.desc_hq_prod);

        id_hq = getIntent().getStringExtra("id_hq");
        adapter_hq_producao =new Adapter_HQ(lista_img,Producaohq_continuacao.this);
        db = FirebaseFirestore.getInstance();


    }


    private void RecuperarFotoHQ(){
        Query query= db.collection("HQ")
                .whereArrayContains("id", id_hq);
        registration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("", "listen:error", e);
                    return;
                }

                for (DocumentChange change : snapshots.getDocumentChanges()) {
                    Log.i("sdsdsd",change.getDocument().getId());
                    HQ hq_producao = change.getDocument().toObject(HQ.class);
                    switch (change.getType()) {
                        case ADDED:
                            lista_img.add(0, hq_producao);
                            adapter_hq_producao.notifyDataSetChanged();
                            Log.d("ad", "New city: " + change.getDocument().getData());
                            break;
                        case MODIFIED:
                            for (HQ ct : lista_img) {

                                if(hq_producao.getId().equals(ct.getId())){
                                    lista_img.remove(ct);
                                    break;
                                }
                            }
                            lista_img.add(0, hq_producao);
                            adapter_hq_producao.notifyDataSetChanged();
                            Log.d("md", "Modified city: " + change.getDocument().getData());
                            break;
                        case REMOVED:
                            for (HQ ct : lista_img) {

                                if(hq_producao.getId().equals(ct.getId())){

                                    lista_img.remove(ct);
                                    break;
                                }
                            }
                            adapter_hq_producao.notifyDataSetChanged();
                            Log.d("rem", "Removed city: " + change.getDocument().getData());
                            break;
                    }
                }


            }
        });
    }

}

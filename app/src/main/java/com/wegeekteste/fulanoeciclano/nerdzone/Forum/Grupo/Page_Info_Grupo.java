package com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.APIService;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_solicitacao_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Client;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Data;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.MyResponse;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Sender;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.sql.CallableStatement;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Page_Info_Grupo extends TrocarFundo {

    private Button botao_entrar_grupo;
    private CircleImageView img_perfil_grupo;
    private TextView nome_info,descricao_info,criador_info,membros_info,nome_toolbar;
    private Forum forum;
    private String id_grupo_selecionado;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private SharedPreferences nome_usuario,foto_usuario;
    private static  final String ARQUIVO_PREFERENCIA ="arquivoreferencia";
    private String nome_user;
    private String foto;
    boolean notify = false;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_permissao);

        toolbar = findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
           //Configuracoes Básicas
            db = FirebaseFirestore.getInstance();
            nome_toolbar=findViewById(R.id.app_toolbar_title_secundario);
            img_perfil_grupo=findViewById(R.id.icone_grupo_info);
            nome_info=findViewById(R.id.nome_grupo_info);
            descricao_info=findViewById(R.id.desc_grupo_info);
            criador_info=findViewById(R.id.criador_grupo_info);
            membros_info=findViewById(R.id.membros_grupo_info);
            botao_entrar_grupo = findViewById(R.id.botao_grupo_info);
            identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();




            botao_entrar_grupo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Membro_solicitacao_grupo membro=new Membro_solicitacao_grupo();
                   membro.setBloqueio(false);
                   membro.setPermissao(false);
                   membro.setFoto_usuario(foto);
                   membro.setId_admin_grupo(forum.getIdauthor());
                   membro.setId_usuario(identificadorUsuario);
                   membro.setNome_usuario(nome_user);
                   membro.setId_grupo(id_grupo_selecionado);
                   membro.SalvarSolicitacao();
                   notify=true;
                    botao_entrar_grupo.setText("Solicitação Enviada!");
                    if (notify) {
                        sendNotifiaction( forum.getIdauthor(),nome_user, "Solicitacao de grupo");
                        Toast.makeText(Page_Info_Grupo.this, "etapa0", Toast.LENGTH_SHORT).show();
                    }
                    notify = false;
                }
            });


        //Recuperar dados do Grupo Selecionado;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("grupo_id")) {
            id_grupo_selecionado= String.valueOf(bundle.getSerializable("grupo_id"));


        }

        if(bundle!=null) {
            if (bundle.containsKey("grupo_info")) {
                forum = (Forum) bundle.getSerializable("grupo_info");

                Admin_grupo(forum.getIdauthor());
                nome_info.setText(forum.getTitulo());
                nome_toolbar.setText(forum.getTitulo());
                descricao_info.setText(forum.getDescricao());
                criador_info.setText(forum.getNomeauthor());

                String foto = forum.getFoto();
                if (foto != null) {
                    Uri url = Uri.parse(foto);
                    Glide.with(this)
                            .load(url)
                            .into(img_perfil_grupo);
                }
            }

        }


        Verificar_Tipo_Grupo();
        CarregarDados_do_Usuario();
        Verificar_solicitacao();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Admin_grupo("none");
    }
    private void sendNotifiaction( final String id_author_recebe, final String username_de_quem_mando,String msg){

        Toast.makeText(Page_Info_Grupo.this, "etapa1 + "+id_author_recebe, Toast.LENGTH_LONG).show();

         db.collection("WeForum").whereEqualTo("idauthor",id_author_recebe).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                             Forum forum=document.toObject(Forum.class);

                                Data data = new Data(identificadorUsuario, R.mipmap.ic_launcher,
                                        username_de_quem_mando+": "+msg, "New Message", id_author_recebe);
                                Toast.makeText(Page_Info_Grupo.this, "etapa2 + "+forum.getToken_author(), Toast.LENGTH_LONG).show();
                                Sender sender = new Sender(data,  forum.getToken_author());

                                Toast.makeText(Page_Info_Grupo.this, forum.getToken_author(), Toast.LENGTH_SHORT).show();
                                apiService.sendNotification(sender)
                                        .enqueue(new Callback<MyResponse>() {
                                            @Override
                                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                if (response.code() == 200){
                                                    if (response.body().success != 1){
                                                        Toast.makeText(Page_Info_Grupo.this, "Failed!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<MyResponse> call, Throwable t) {

                                            }
                                        });
                            }

                            }
                    }
                });

    }
    private void CarregarDados_do_Usuario(){
        nome_usuario = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
       nome_user =nome_usuario.getString("nome","");


        foto_usuario = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
        foto =foto_usuario.getString("foto_usuario","");


    }

    //Salvar o id do cabra que vai receber a notificacao
    private void Admin_grupo(String admin_id){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("Admin_grupo", admin_id);
        editor.apply();
    }
    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

               finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void Verificar_solicitacao(){
         db.collection("Permissao_Grupo").whereEqualTo("id_grupo",id_grupo_selecionado)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if(task.isSuccessful()){
                     for (QueryDocumentSnapshot document : task.getResult()) {
                         Log.d("55", document.getId() + " => " + document.getData());
                     Membro_solicitacao_grupo membro=document.toObject(Membro_solicitacao_grupo.class);
                     if(membro.getId_usuario().equals(identificadorUsuario)){
                         botao_entrar_grupo.setText("Aguarde");
                         botao_entrar_grupo.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {

                             }
                         });
                     }else{
                         botao_entrar_grupo.setText("Seja um Membro");
                     }


                     }
                 } else {
                     Log.d("747", "Error getting documents: ", task.getException());
                 }
                 }

         });

    }



  private void Buscar_dados(){
      DocumentReference docRef = db.collection("WeForum").document(id_grupo_selecionado);
      docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
              Forum forum = documentSnapshot.toObject(Forum.class);
              Log.i("7444",forum.getIdauthor().toLowerCase());
          }
      });
  }


    private void Verificar_Tipo_Grupo(){
        Query query=  db
                .collection("WeForum");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {

                    Forum forum_tipo = change.getDocument().toObject(Forum.class);

                    if(forum_tipo.getOpcao().equals("grupo_permissao")){

                      botao_entrar_grupo.setText("Permissão");
                    }else{
                        botao_entrar_grupo.setText("Se torne um membro");

                    }



                }
            }
        });
    }

  private void Verificar_situacao_membro(){
      Query query=  db
              .collection("WeForum").document(id_grupo_selecionado)
              .collection("Membros").whereEqualTo("id_usuario",identificadorUsuario);

         query.addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
               for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {

                   Membro_Grupo membro_grupo = change.getDocument().toObject(Membro_Grupo.class);
                    if(membro_grupo.getPermissao().equals(true)){
                        Intent it = new Intent(Page_Info_Grupo.this, Page_Chat_grupo.class);
                        it.putExtra("forum_selecionado",forum);
                        it.putExtra("id_forum_selecionado",id_grupo_selecionado);
                        startActivity(it);
                        finish();
                    }


               }
           }
       });
  }


}

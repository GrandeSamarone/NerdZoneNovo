package com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_chat_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Lista_forum_Geral;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Chat_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page_Chat_grupo extends TrocarFundo implements View.OnClickListener {

    private Toolbar toolbar;
    private Adapter_chat_grupo adapter;
    private Adapter_Membro_Grupo adapter_membro_grupo;
    private ArrayList<Membro_Grupo> list_membro_grupo= new ArrayList<>();
    private Chat_Grupo chat=new Chat_Grupo();
    private ArrayList<Chat_Grupo> list_conversa_grupo = new ArrayList<>();
    private RecyclerView recyclerView_chat,recycler_chat_online;
    private CircleImageView icone;
    private TextView titulo;
    private EmojiEditText edit_chat_emoji;
    private  androidx.appcompat.widget.AppCompatButton botao_env_msg;
    private EmojiPopup emojiPopup;
    private ImageView botaoicone;
    private View root;
    private String usuarioLogado;
    private String Id_Forum_selecionado;
    private Forum forum_selecionado;
    private FirebaseFirestore db;
    private Task<DocumentReference> ref;
    private String identificadorUsuario;
    private SharedPreferences Foto_usuario;
    private SharedPreferences nome_usuario;
    private  ListenerRegistration registration,registration_membro;
    private static  final String ARQUIVO_PREFERENCIA ="arquivoreferencia";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_detalhetopico);
        toolbar = findViewById(R.id.toolbartopico);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Configuracoes Iniciais
        db = FirebaseFirestore.getInstance();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        icone = findViewById(R.id.icone_chat_toolbar);
        titulo = findViewById(R.id.detalhe_topico_titulo);
        edit_chat_emoji=findViewById(R.id.caixa_de_texto_comentario_topico);
        botao_env_msg=findViewById(R.id.button_postar_comentario_topico);
        botao_env_msg.setOnClickListener(this);
        botaoicone=findViewById(R.id.botao_post_icone_topico);
        botaoicone.setOnClickListener(this);
        usuarioLogado =  UsuarioFirebase.getIdentificadorUsuario();

        //RecycleView
        LinearLayoutManager layoutManagerMembros = new LinearLayoutManager(
                Page_Chat_grupo.this, LinearLayoutManager.HORIZONTAL,false);
        recycler_chat_online=findViewById(R.id.recycler_chat_online);
        recycler_chat_online.setLayoutManager(layoutManagerMembros);

        adapter_membro_grupo = new Adapter_Membro_Grupo(list_membro_grupo,getApplicationContext());
        recycler_chat_online.setAdapter(adapter_membro_grupo);



        recyclerView_chat = findViewById(R.id.recycler_comentario_topico);
        recyclerView_chat.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView_chat.setLayoutManager(layoutManager);


        //emotion
        root=findViewById(R.id.root_view_topico);
        emojiPopup = EmojiPopup.Builder.fromRootView(root).build(edit_chat_emoji);
        setUpEmojiPopup();

        forum_selecionado = (Forum)  getIntent().getSerializableExtra("forum_selecionado");
        Id_Forum_selecionado = getIntent().getStringExtra("id_forum_selecionado");
        if(forum_selecionado!=null) {
            Log.i("373773", forum_selecionado.getFoto());
            titulo.setText(forum_selecionado.getTitulo());
            //  mensagem.setText(topicoselecionado.getDescricao());
            // RecuperarIcone_e_nome_author(topicoselecionado.getIdauthor());
            if (forum_selecionado.getFoto() != null) {
                Uri url = Uri.parse(forum_selecionado.getFoto());
                Glide.with(this)
                        .load(url)
                        .into(icone);
            }
        }



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.botao_post_icone_topico:
                emojiPopup.toggle();
                break;
            case R.id.button_postar_comentario_topico:
                SalvarMensagem();

                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Recuperar_Mensagens();
        Recuperar_Membros_online();

        // Preferences pega o nome do usuario;
        nome_usuario = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
        Foto_usuario  = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Status("online");
      //  Adicionar_Membro();

    }

    @Override
    protected void onPause() {
        super.onPause();
        registration.remove();
        registration_membro.remove();
        Status("offline");

    }
    @Override
    protected void onStop() {
        super.onStop();

        if (registration!= null) {
            registration.remove();
            registration = null;
        }
    }


    private void Status(String status){
    String nome = nome_usuario.getString("nome","");
    String foto =Foto_usuario.getString("foto_usuario","");

        Membro_Grupo membro_grupo=new Membro_Grupo();
        membro_grupo.setId_usuario(identificadorUsuario);
        membro_grupo.setId_grupo(Id_Forum_selecionado);
        membro_grupo.setNome_usuario(nome);
        membro_grupo.setFoto_usuario(foto);
        membro_grupo.setStatus(status);
        membro_grupo.SalvarMembro();

}

    private void Adicionar_Membro(){
    HashMap<String, Object> membrosMap = new HashMap<>();
    membrosMap.put("id_usuario", identificadorUsuario);
    db.collection("WeForum").document(Id_Forum_selecionado)
            .collection("Membros").document(identificadorUsuario).set(membrosMap);

}

    private void Remover_Membro(){
        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("id_usuario", identificadorUsuario);
        db.collection("WeForum").document(Id_Forum_selecionado)
                .collection("Membros").document(identificadorUsuario).delete();

    }

    private void SalvarMensagem(){
        //SharedPreferencies pegando a variavel e os dados
        String nome = nome_usuario.getString("nome","");
        String foto =Foto_usuario.getString("foto_usuario","");
        String textoComentario = edit_chat_emoji.getText().toString();


        if(textoComentario!=null && !textoComentario.equals(""))
        {
            chat.setId_grupo(Id_Forum_selecionado);
            chat.setId_usuario(identificadorUsuario);
            Log.i("sdoskdosdk77",identificadorUsuario);
            chat.setNome_usuario(nome);
            chat.setFoto_usuario(foto);
            chat.setId_grupo(Id_Forum_selecionado);
            // chat.setTempo(data_time);
            chat.setMensagem(textoComentario);
            chat.Salvar_msg();
        }else{
            Toast.makeText(this, "Insira um comentário antes da salvar!",
                    Toast.LENGTH_LONG).show();
        }
        //Limpar comentario
        edit_chat_emoji.setText("");
    }

    private void Recuperar_Mensagens(){
        list_conversa_grupo.clear();

        Query query=  db
                .collection("WeForum").document(Id_Forum_selecionado)
                .collection("Mensagens").orderBy("tempo", Query.Direction.ASCENDING);
        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                if (documentChanges != null) {
                    for (DocumentChange doc: documentChanges) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            Chat_Grupo coment=doc.getDocument().toObject(Chat_Grupo.class);
                            list_conversa_grupo.add(coment);
                            //  adapter.notifyDataSetChanged();
                        }


                        adapter = new Adapter_chat_grupo(getApplicationContext(),list_conversa_grupo);
                        recyclerView_chat.setAdapter(adapter);
                    }
                }
            }
        });

    }


private void Recuperar_Membros_online(){
    list_membro_grupo.clear();
    Query query=
            db.collection("WeForum").document(Id_Forum_selecionado)
            .collection("Membros")
            .whereEqualTo("status", "online");
    registration_membro = query .addSnapshotListener(new EventListener<QuerySnapshot>() {
              @Override
              public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                  if (e != null) {
                      Log.w("", "listen:error", e);
                      return;
                  }

                  for (DocumentChange change : snapshots.getDocumentChanges()) {
                      Log.i("sdsdsd",change.getDocument().getId());
                      Membro_Grupo membro_grupo = change.getDocument().toObject(Membro_Grupo.class);
                      membro_grupo.setId(change.getDocument().getId());
                      //  Log.i("sdsdsd",change.getDocument().getId());
                      // Log.i("sdsdsd2",conto.getUid());
                      switch (change.getType()) {
                          case ADDED:
                              list_membro_grupo.add(0, membro_grupo);

                              if (list_membro_grupo.size() > 0) {
                                  recycler_chat_online.setVisibility(View.VISIBLE);
                              }

                              adapter_membro_grupo.notifyDataSetChanged();
                              Log.d("ad", "New city: " + change.getDocument().getData());
                              break;
                          case MODIFIED:
                              for (Membro_Grupo ct : list_membro_grupo) {

                                  if(membro_grupo.getId().equals(ct.getId())){
                                      list_membro_grupo.remove(ct);
                                      break;
                                  }
                              }
                              list_membro_grupo.add(0, membro_grupo);
                              if (list_membro_grupo.size() > 0) {
                                  //linear_nada_cadastrado.setVisibility(View.GONE);
                              }

                              adapter_membro_grupo.notifyDataSetChanged();
                              Log.d("md", "Modified city: " + change.getDocument().getData());
                              break;
                          case REMOVED:
                              for (Membro_Grupo ct : list_membro_grupo) {

                                  if(membro_grupo.getId().equals(ct.getId())){
                                      list_membro_grupo.remove(ct);
                                      break;
                                  }
                              }

                              adapter_membro_grupo.notifyDataSetChanged();
                              Log.d("rem", "Removed city: " + change.getDocument().getData());
                              break;
                      }
                  }
              }
          });
}





    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

                startActivity(new Intent(Page_Chat_grupo.this, Lista_forum_Geral.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }

        return super.onOptionsItemSelected(item);
    }





    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(root)
                .setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
                    @Override
                    public void onEmojiBackspaceClick(View v) {
                        if(emojiPopup.isShowing()){
                            emojiPopup.dismiss();
                        }
                        Log.d("ss","Clicked on Backspace");
                    }
                })
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override
                    public void onEmojiPopupShown() {
                        botaoicone.setImageResource(R.drawable.ic_teclado);
                    }
                })
                .setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
                    @Override
                    public void onKeyboardOpen(final int keyBoardHeight) {
                        Log.d("ss","Clicked on Backspace");
                    }
                })
                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        botaoicone.setImageResource(R.drawable.ic_emotion_chat);
                    }
                })
                .setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override
                    public void onKeyboardClose() {
                        if (emojiPopup.isShowing()){
                            emojiPopup.dismiss();
                        }
                        Log.d("ss","Clicked on Backspace");
                    }
                })
                .build(edit_chat_emoji);
    }


}

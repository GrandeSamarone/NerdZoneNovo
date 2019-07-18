package com.wegeekteste.fulanoeciclano.nerdzone.Forum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_chat_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_comentario;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Chat_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Comentario;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detalhe_Forum extends TrocarFundo implements View.OnClickListener {

    private Toolbar toolbar;
    private Adapter_chat_grupo adapter;
    private Chat_Grupo chat=new Chat_Grupo();
    private ArrayList<Chat_Grupo> list_conversa_grupo = new ArrayList<>();
    private RecyclerView recyclerView_comentarios;
    private CircleImageView icone;
    private TextView titulo;
    private EmojiEditText edit_chat_emoji;
    private  android.support.v7.widget.AppCompatButton botao_env_msg;
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
    private  ListenerRegistration registration;
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
        recyclerView_comentarios = findViewById(R.id.recycler_comentario_topico);
        recyclerView_comentarios.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView_comentarios.setLayoutManager(layoutManager);


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
               SalvarComentario();

                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Recuperar_Mensagens();


        // Preferences pega o nome do usuario;
        nome_usuario = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
        Foto_usuario  = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registration.remove();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (registration!= null) {
            registration.remove();
            registration = null;
        }
    }


    public void SalvarComentario(){
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
     public void Recuperar_Mensagens(){
         list_conversa_grupo.clear();

                Query query=  db
                 .collection("WeForum").document(Id_Forum_selecionado)
                 .collection("mensagens").orderBy("tempo", Query.Direction.ASCENDING);
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
                                          recyclerView_comentarios.setAdapter(adapter);
                                      }
                                  }
                              }
                          });

     }






    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

                startActivity(new Intent(Detalhe_Forum.this, Lista_forum_Geral.class)
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

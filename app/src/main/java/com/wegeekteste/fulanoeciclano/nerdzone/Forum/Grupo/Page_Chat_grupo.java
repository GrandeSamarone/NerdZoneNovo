package com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_chat_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Forum_principal;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.APIService;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Chat_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Client;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class Page_Chat_grupo extends TrocarFundo implements View.OnClickListener {

    private Toolbar toolbar;
    private Adapter_chat_grupo adapter;
    private StorageReference storageReference;
    private Adapter_Membro_Grupo adapter_membro_grupo;
    private ArrayList<Membro_Grupo> list_membro_grupo = new ArrayList<>();
    private Chat_Grupo chat = new Chat_Grupo();
    private ArrayList<Chat_Grupo> list_conversa_grupo = new ArrayList<>();
    private RecyclerView recyclerView_chat, recycler_chat_online;
    private SimpleDraweeView icone;
    private TextView titulo;
    private EmojiEditText edit_chat_emoji;
    private androidx.appcompat.widget.AppCompatButton botao_env_msg;
    private EmojiPopup emojiPopup;
    private ImageView botaoicone;
    private View root;
    private String usuarioLogado;
    private String Id_Forum_selecionado,admin;
    private Forum forum_selecionado;
    private FirebaseFirestore db;
    private String identificadorUsuario;
    private SharedPreferences Foto_usuario;
    private SharedPreferences nome_usuario;
    private TextView detalhe_topico_quant_membros;
    private ListenerRegistration registration, registration_membro;
    private static final String ARQUIVO_PREFERENCIA = "arquivoreferencia";
    private static final int SELECAO_GALERIA = 200;
    private boolean typingStarted;
    private boolean isScrolling = false;
    private boolean isLastItemReached;
    private int VisibleItemCount, FistVisibleItem, totalItems;
    private LinearLayoutManager layoutManager;
    private DocumentSnapshot lastVisible;
    private AlertDialog dialog;

    //notificacao
    APIService apiService;
    boolean notify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_detalhe_chat_grupo);
        toolbar = findViewById(R.id.toolbartopico);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Configuracoes Iniciais
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        db = FirebaseFirestore.getInstance();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        icone = findViewById(R.id.icone_chat_toolbar);
        titulo = findViewById(R.id.detalhe_topico_titulo);
        edit_chat_emoji = findViewById(R.id.caixa_de_texto_comentario_topico);
        detalhe_topico_quant_membros = findViewById(R.id.detalhe_topico_quant_membros);
        botao_env_msg = findViewById(R.id.button_postar_comentario_topico);
        botao_env_msg.setOnClickListener(this);
        botaoicone = findViewById(R.id.botao_post_icone_topico);
        botaoicone.setOnClickListener(this);
        usuarioLogado = UsuarioFirebase.getIdentificadorUsuario();


        //RecycleView onlines
        LinearLayoutManager layoutManagerMembros = new LinearLayoutManager(
                Page_Chat_grupo.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_chat_online = findViewById(R.id.recycler_chat_online);
        recycler_chat_online.setLayoutManager(layoutManagerMembros);
        adapter_membro_grupo = new Adapter_Membro_Grupo(list_membro_grupo, getApplicationContext());
        recycler_chat_online.setAdapter(adapter_membro_grupo);


        recyclerView_chat = findViewById(R.id.recycler_chat_grupo);
        recyclerView_chat.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView_chat.setLayoutManager(layoutManager);
        adapter = new Adapter_chat_grupo(getApplicationContext(), list_conversa_grupo);
        recyclerView_chat.setAdapter(adapter);

        //emotion
        root = findViewById(R.id.root_view_topico);
        emojiPopup = EmojiPopup.Builder.fromRootView(root).build(edit_chat_emoji);
        setUpEmojiPopup();

        forum_selecionado = (Forum) getIntent().getSerializableExtra("forum_selecionado");
        Id_Forum_selecionado = getIntent().getStringExtra("id_forum_selecionado");
        admin = getIntent().getStringExtra("id_admin");
        if (forum_selecionado != null) {
            titulo.setText(forum_selecionado.getTitulo());
            detalhe_topico_quant_membros.setText(String.valueOf(forum_selecionado.getMembro_count()));
            //  mensagem.setText(topicoselecionado.getDescricao());
            // RecuperarIcone_e_nome_author(topicoselecionado.getIdauthor());
            if (forum_selecionado.getFoto() != null) {
                String stringcapa = forum_selecionado.getFoto();
                if (stringcapa != null) {
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(stringcapa))
                            .setLocalThumbnailPreviewsEnabled(true)
                            .setProgressiveRenderingEnabled(true)
                            .setResizeOptions(new ResizeOptions(60, 60))
                            .build();

                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .build();
                    icone.setController(controller);
                    RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);

                    GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
                    GenericDraweeHierarchy hierarchy = builder
                            .setRoundingParams(roundingParams)
                            .setProgressBarImage(new CircleProgressDrawable())
                            //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                            .build();
                    icone.setHierarchy(hierarchy);
                }
            }
        }



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        Digitando();
        Recuperar_Mensagens();
        //Conta a quantidade de membros
        quant_membros();
        //recuperar que está digitando
        Recuperar_Membros_Digitand_online();
        // Preferences pega o nome do usuario;
        nome_usuario = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
        Foto_usuario = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Status("online");
        //  Adicionar_Membro();
        // Online();

    }

    @Override
    protected void onPause() {
        super.onPause();
        registration.remove();
        registration_membro.remove();
        Status("offline");
        //RemoveMembro
        // offline();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (registration != null) {
            registration.remove();
            registration = null;
        }
    }


    private void CarregarRecicle() {

    }


    private void Digitando() {
        edit_chat_emoji.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {
                    Log.i("sdsdsd7474", String.valueOf(TextUtils.isEmpty(s.toString())));
                    typingStarted = true;
                    //  user_digitando.setText("Marlos trinidad");
                    Adicionar_Membro_digitando_true();

                    //send typing started status
                } else if (s.toString().trim().length() == 0 && typingStarted) {
                    //Log.i(TAG, “typing stopped event…”);
                    typingStarted = false;
                    //user_digitando.setText("Marlos trinidad");
                    Adicionar_Membro_digitando_false();
                    //send typing stopped status
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

                Log.i("sdsdsd1", s.subSequence(start, start + count).toString());

            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                s.toString();
                Log.i("sdsdsd2", s.subSequence(start, start + count).toString());
            }
        });
    }

    private void Status(String status) {
        String nome = nome_usuario.getString("nome", "");
        String foto = Foto_usuario.getString("foto_usuario", "");
        String token = Foto_usuario.getString("token", "");

        Membro_Grupo membro_grupo = new Membro_Grupo();
        membro_grupo.setId_usuario(identificadorUsuario);
        membro_grupo.setId_grupo(Id_Forum_selecionado);
        membro_grupo.setNome_usuario(nome);
        membro_grupo.setFoto_usuario(foto);
        membro_grupo.setToken_usuario(token);
        membro_grupo.setStatus(status);

        if(admin.equals(identificadorUsuario)){
             membro_grupo.setId_admin(admin);
            membro_grupo.setAdmin(true);
        }else{
            membro_grupo.setId_admin(null);
            membro_grupo.setAdmin(false);
        }
        membro_grupo.SalvarMembro();

    }

    //para contar quantos play online
    private void Online() {

        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("id_usuario", identificadorUsuario);
        db.collection("WeForum").document(Id_Forum_selecionado)
                .collection("Online").document(identificadorUsuario).set(membrosMap);
    }

    private void offline() {
        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("id_usuario", identificadorUsuario);
        db.collection("WeForum").document(Id_Forum_selecionado)
                .collection("Online").document(identificadorUsuario).delete();

    }

    //envia se o usuario está digitando ou nao
    private void Adicionar_Membro_digitando_true() {
        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("digitando", true);
        db.collection("WeForum").document(Id_Forum_selecionado)
                .collection("Membros").document(identificadorUsuario).update(membrosMap);

    }

    private void Adicionar_Membro_digitando_false() {
        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("digitando", false);
        db.collection("WeForum").document(Id_Forum_selecionado)
                .collection("Membros").document(identificadorUsuario).update(membrosMap);

    }


    private void SalvarMensagem() {
        //SharedPreferencies pegando a variavel e os dados
        String nome = nome_usuario.getString("nome", "");
        String foto = Foto_usuario.getString("foto_usuario", "");
        String textoComentario = edit_chat_emoji.getText().toString();
        String id_mensagem_texto = UUID.randomUUID().toString();

        if (textoComentario != null && !textoComentario.equals("")) {
            chat.setId_grupo(Id_Forum_selecionado);
            chat.setId_usuario(identificadorUsuario);
            Log.i("sdoskdosdk77", identificadorUsuario);
            chat.setNome_usuario(nome);
            chat.setMensagem_type("texto");
            chat.setFoto_usuario(foto);
            chat.setId_grupo(Id_Forum_selecionado);
            chat.setMensagem(textoComentario);
            chat.setId_mensagem(id_mensagem_texto);
            chat.Salvar_msg();
        } else {
            Toast.makeText(this, "Insira um comentário antes da salvar!",
                    Toast.LENGTH_LONG).show();
        }
        //Limpar comentario
        edit_chat_emoji.setText("");



    }
    private void SalvarMensagem_imagem(String url) {
        //SharedPreferencies pegando a variavel e os dados
        String id_mensagem_img = UUID.randomUUID().toString();
        String nome = nome_usuario.getString("nome", "");
        String foto = Foto_usuario.getString("foto_usuario", "");
        chat.setId_grupo(Id_Forum_selecionado);
        chat.setId_usuario(identificadorUsuario);
        chat.setMensagem(url);
        chat.setMensagem_type("imagem");
        chat.setNome_usuario(nome);
        chat.setFoto_usuario(foto);
        chat.setMensagem_img(url);
        chat.setId_mensagem(id_mensagem_img);
        chat.setId_grupo(Id_Forum_selecionado);
        // chat.setTempo(data_time);
        chat.Salvar_msg();

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
                            adapter.notifyDataSetChanged();
                            recyclerView_chat.smoothScrollToPosition(recyclerView_chat.getAdapter().getItemCount());
                        }


                    }
                }
            }
        });

    }


    //Recupera que está digitando
    private void Recuperar_Membros_Digitand_online() {
        list_membro_grupo.clear();
        Query query =
                db.collection("WeForum").document(Id_Forum_selecionado)
                        .collection("Membros")
                        .whereEqualTo("digitando", true);
        registration_membro = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("", "listen:error", e);
                    return;
                }

                for (DocumentChange change : snapshots.getDocumentChanges()) {
                    Log.i("sdsdsd", change.getDocument().getId());
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

                                if (membro_grupo.getId().equals(ct.getId())) {
                                    list_membro_grupo.remove(ct);
                                    break;
                                }
                            }
                            list_membro_grupo.add(0, membro_grupo);
                            if (list_membro_grupo.size() > 0) {
                                recycler_chat_online.setVisibility(View.VISIBLE);

                            }

                            adapter_membro_grupo.notifyDataSetChanged();
                            Log.d("md", "Modified city: " + change.getDocument().getData());
                            break;
                        case REMOVED:
                            for (Membro_Grupo ct : list_membro_grupo) {

                                if (membro_grupo.getId().equals(ct.getId())) {
                                    list_membro_grupo.remove(ct);
                                    break;
                                }
                            }
                            recycler_chat_online.setVisibility(View.VISIBLE);
                            adapter_membro_grupo.notifyDataSetChanged();
                            Log.d("rem", "Removed city: " + change.getDocument().getData());
                            break;
                    }
                }
            }
        });
    }

    //Conta a quantidade de membros
    private void quant_membros() {
        db = FirebaseFirestore.getInstance();
        db.collection("WeForum").document(Id_Forum_selecionado)
                .collection("Membros").whereEqualTo("bloqueio",false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("sdsdsd77", task.getResult().size() + "");
                    HashMap<String, Object> membrosMap = new HashMap<>();
                    membrosMap.put("membro_count", task.getResult().size());
                    db.collection("WeForum").document(Id_Forum_selecionado).update(membrosMap);

                    final Map<String, Object> addUserToArrayMap = new HashMap<>();
                    addUserToArrayMap.put("membros", FieldValue.arrayUnion(identificadorUsuario));
                    db.collection("WeForum").document(Id_Forum_selecionado)
                            .update(addUserToArrayMap);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }


    //botao Pesquisar
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_grupo,menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar

                startActivity(new Intent(Page_Chat_grupo.this, Forum_principal.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
              break;
            case R.id.menu_configuracao:
              Intent it = new Intent(Page_Chat_grupo.this,Configuracao_Grupo_Activity.class);
              it.putExtra("id_grupo",Id_Forum_selecionado);
                it.putExtra("nome_grupo",forum_selecionado.getTitulo());
                it.putExtra("desc_grupo",forum_selecionado.getDescricao());
                it.putExtra("img_grupo",forum_selecionado.getFoto());
              startActivity(it);
                              break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(root)
                .setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
                    @Override
                    public void onEmojiBackspaceClick(View v) {
                        if (emojiPopup.isShowing()) {
                            emojiPopup.dismiss();
                        }
                        Log.d("ss", "Clicked on Backspace");
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
                        Log.d("ss", "Clicked on Backspace");
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
                        if (emojiPopup.isShowing()) {
                            emojiPopup.dismiss();
                        }
                        Log.d("ss", "Clicked on Backspace");
                    }
                })
                .build(edit_chat_emoji);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_GALERIA:
                        CropImage.ActivityResult resultGALERIA = CropImage.getActivityResult(data);
                        Uri resultUriGALERIA = resultGALERIA.getUri();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUriGALERIA);
                        Log.i("sdoskdosdk77", String.valueOf(imagem));
                        break;


                }
                if (imagem != null) {
                    //Recuperar dados da imagem  para o  Firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();
                    String nomeImagem = UUID.randomUUID().toString();
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("forum")
                            .child(Id_Forum_selecionado)
                            .child(identificadorUsuario)
                            .child(nomeImagem);

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    LayoutInflater layoutInflater = LayoutInflater.from(Page_Chat_grupo.this);
                    final View view = layoutInflater.inflate(R.layout.dialog_carregando_gif_comscroop, null);
                    ImageView imageViewgif = view.findViewById(R.id.gifimage);

                    if (!Page_Chat_grupo.this.isFinishing()) {
                        Glide.with(this)
                                .asGif()
                                .load(R.drawable.gif_self)
                                .into(imageViewgif);
                        builder.setView(view);
                    }
                    dialog = builder.create();
                    dialog.show();


                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    //caso de errado
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                   //Salvando a imagem
                                    Log.i("sdoskdosdk77", String.valueOf(uri));
                                    SalvarMensagem_imagem(uri.toString());
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                     dialog.dismiss();
                                    Toast.makeText(Page_Chat_grupo.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

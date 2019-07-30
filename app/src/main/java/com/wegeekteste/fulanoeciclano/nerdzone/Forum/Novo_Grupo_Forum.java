package com.wegeekteste.fulanoeciclano.nerdzone.Forum;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Novo_Grupo_Forum extends TrocarFundo {

    private static final String padrao = "Obrigatório";
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private static final String ARQUIVO_PREFERENCIA = "arquivoreferencia";
    private Toolbar toolbar;
    private CircleImageView icone;
    private CircleImageView img_novo_grupo;
    private String urlimg, op;
    private Button botaosalvar;
    private DatabaseReference databaseusuario, databasetopico, SeguidoresRef;
    private DataSnapshot seguidoresSnapshot;
    private FirebaseUser usuario;
    private ChildEventListener ChildEventListenerperfil;
    private EditText titulo_topico, mensagem_topico;
    private Forum forum = new Forum();
    private Usuario perfil;
    private StorageReference storageReference;
    private Dialog dialog;
    private Uri url;
    private ChildEventListener ChildEventListenerSeguidores;
    private RadioButton radio_grupo, radio_topico;
    private String identificadorUsuario;
    private RadioGroup grupo;
    private SharedPreferences sharedPreferences;
    private SharedPreferences nome_usuario;
    private Spinner campo_categoria_grupo;
    private String Campo_string="Categoria";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo__topico);

        toolbar = findViewById(R.id.toolbarsecundario_sem_foto);
        toolbar.setTitle("Novo Grupo");
        setSupportActionBar(toolbar);


        //Configuraçoes Originais
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        titulo_topico = findViewById(R.id.nome_topico);
        mensagem_topico = findViewById(R.id.desc_topico);
        botaosalvar = findViewById(R.id.botaosalvar_Grupo);
        campo_categoria_grupo = findViewById(R.id.spinner_grupo_categoria);
        grupo = findViewById(R.id.radio_group_grupo);
        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar_Dados_Grupo();
            }
        });
        img_novo_grupo = findViewById(R.id.image_grupo_Cadastro);
        img_novo_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .getIntent(Novo_Grupo_Forum.this);
                startActivityForResult(intent, SELECAO_GALERIA);
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        VerificaRadioButton();
        CarregarDadosSpinner();

        // Preferences pega o nome do usuario;
        nome_usuario = getSharedPreferences(ARQUIVO_PREFERENCIA, MODE_PRIVATE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        CropImage.ActivityResult resultCAMERA = CropImage.getActivityResult(data);
                        Uri resultUriCAMERA = resultCAMERA.getUri();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUriCAMERA);
                        break;
                    case SELECAO_GALERIA:
                        CropImage.ActivityResult resultGALERIA = CropImage.getActivityResult(data);
                        Uri resultUriGALERIA = resultGALERIA.getUri();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUriGALERIA);

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
                            .child(identificadorUsuario)
                            .child(nomeImagem);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    LayoutInflater layoutInflater = LayoutInflater.from(Novo_Grupo_Forum.this);
                    final View view = layoutInflater.inflate(R.layout.dialog_carregando_gif_analisando, null);
                    ImageView imageViewgif = view.findViewById(R.id.gifimage);

                    Glide.with(this)
                            .asGif()
                            .load(R.drawable.gif_analizando)
                            .into(imageViewgif);
                    builder.setView(view);

                    dialog = builder.create();
                    dialog.show();
                    ;
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    //caso de errado
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    dialog.dismiss();
                                    urlimg = uri.toString();
                                    Glide.with(getApplicationContext())
                                            .load(uri)
                                            .into(img_novo_grupo);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    dialog.dismiss();
                                    Toast.makeText(Novo_Grupo_Forum.this, "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();

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


    //carregar spinner
    private void CarregarDadosSpinner() {
        //
        String[] artista = getResources().getStringArray(R.array.categoria_grupo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, artista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campo_categoria_grupo.setAdapter(adapter);
        campo_categoria_grupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (campo_categoria_grupo.getSelectedItem().toString().trim().equals("Categoria")) {
                    Toast.makeText(Novo_Grupo_Forum.this, "add uma categoria", Toast.LENGTH_SHORT).show();
                } else {
                    Campo_string = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Forum configurarTopico() {

        String nome = nome_usuario.getString("nome", "");
        String titulo = titulo_topico.getText().toString();
        String mensagem = mensagem_topico.getText().toString();

        final Calendar calendartempo = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd'-'MM'-'y", java.util.Locale.getDefault());// MM'/'dd'/'y;
        String data = simpleDateFormat.format(calendartempo.getTime());

        forum.setIdauthor(identificadorUsuario);
        forum.setNomeauthor(nome);
        forum.setTitulo(titulo);
        forum.setFoto(urlimg);
        forum.setDescricao(mensagem);
        forum.setData(data);
        forum.setCategoria(Campo_string);
        forum.setOpcao(op);
        if (url != null) {
            forum.setFoto(String.valueOf(url));
        } else {

        }
        return forum;

    }

    public void validar_Dados_Grupo() {
        forum = configurarTopico();

        if (TextUtils.isEmpty(forum.getTitulo())) {
            titulo_topico.setError(padrao);
            return;
        }
        if (TextUtils.isEmpty(forum.getDescricao())) {
            mensagem_topico.setError(padrao);
            return;
        }

            forum.SalvarForum();





        //int qtdTopicos = perfil.getTopicos() + 1;
        //perfil.setTopicos(qtdTopicos);
        //perfil.atualizarQtdTopicos();
        Toast.makeText(Novo_Grupo_Forum.this, "Grupo Criado Com Sucesso!", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(Novo_Grupo_Forum.this, Lista_Forum.class);
        startActivity(it);
        finish();
    }

    public void VerificaRadioButton(){
        //radio group
        grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId== R.id.radiogrupo_Free){
                   op="grupo_free";
                }else if(checkedId==R.id.radiogrupo_permissao){
                   op="grupo_permissao";
                }
            }
        });
    }



    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, Lista_Forum.class));
                finish();
        }
              /*
        case android.R.id.home:
        // NavUtils.navigateUpFromSameTask(this);
        startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }else{
            finish();
        }

        break;
        */

        return super.onOptionsItemSelected(item);
    }



}

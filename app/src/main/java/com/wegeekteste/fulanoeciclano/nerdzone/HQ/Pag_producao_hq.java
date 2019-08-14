package com.wegeekteste.fulanoeciclano.nerdzone.HQ;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes.Adapter_HQ_Producao;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Novo_Grupo_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ_Model;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Suporte.Contato;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Pag_producao_hq extends TrocarFundo {

    private Toolbar toolbar;
    ArrayList<Uri> FileList = new ArrayList<Uri>();
    private TextView toolbarnome;
    private Button botao_carregar,botao_proximo;
    private RecyclerViewDragDropManager dragMgr;
    private RecyclerView recyclerView_hq;
    private  int reqCode =  12 ;
    private RecyclerView.Adapter adapter;
    private ArrayList<HQ_Model> hq_model;
    private ArrayList<String> list= new ArrayList<>();
    private FirebaseFirestore db;
    private int upload_count = 0;
    private Dialog dialog;
    private Uri FileUri;
    private static final int PICK_IMG = 1;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private int uploads = 0;
    int index = 0;
    TextView textView;
    Button choose,send;
    private String identificadorUsuario;
    private static  final String ARQUIVO_PREFERENCIA_listaImagem ="Lista_imagem";
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_producao_hq);


        toolbar=findViewById(R.id.toolbarsecundario);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarnome=findViewById(R.id.app_toolbar_title_secundario);
        toolbarnome.setText("Oficina HQ");

        //configuracoes iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        botao_proximo=findViewById(R.id.prox_etapa_prod_hq);
        botao_carregar= findViewById(R.id.carregar_img_hq);
        recyclerView_hq = findViewById(R.id.rec_hq);
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        db = FirebaseFirestore.getInstance();
        botao_carregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Carregar_HQ();
            }
        });


        botao_proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(v);
            }
        });


    }

    private void SalvarImagens() {
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA_listaImagem, MODE_PRIVATE);
        String img_list = sharedPreferences.getString("list", "");

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void Carregar_HQ() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMG);
        //open album
   /*     GalleryConfig config = new GalleryConfig.Build()
                .limitPickPhoto(50)
                .singlePhoto(false)
                .hintOfPick("this is pick hint")
                .filterMimeTypes(new String[]{})
                .build();
        GalleryActivity.openActivity(Pag_producao_hq.this, reqCode, config);
    */
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();

                    int CurrentImageSelect = 0;

                    while (CurrentImageSelect < count) {
                        Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                        ImageList.add(imageuri);
                        CurrentImageSelect = CurrentImageSelect + 1;
                    }

                }

            }

        }

    }

    @SuppressLint("SetTextI18n")
    public void upload(View view) {

        final StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child("imagens");
        for (uploads=0; uploads < ImageList.size(); uploads++) {
            Uri Image  = ImageList.get(uploads);
            String nomeImagem = UUID.randomUUID().toString();
            final StorageReference imagename = ImageFolder
                    .child("HQ")
                    .child(identificadorUsuario)
                    .child(nomeImagem);


            imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = String.valueOf(uri);
                        }
                    });

                }
            });


        }


    }





   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Recebe as imagens da galeria
         if(data!=null) {
             FileUri = (Uri) data.getSerializableExtra(GalleryActivity.PHOTOS);
             Log.i("5555", String.valueOf(FileUri));
             FileList.add(FileUri);
             String nomeImagem = UUID.randomUUID().toString();
             StorageReference ImageFolder = storageReference
                     .child("imagens")
                     .child("HQ")
                     .child(identificadorUsuario)
                     .child("nometal")
                     .child(nomeImagem);
             for(upload_count = 0; upload_count < FileList.size(); upload_count++){

                 Uri IndividualFile = FileList.get(upload_count);
                 final StorageReference ImageName = ImageFolder.child("Image"+IndividualFile.getLastPathSegment());



                 ImageName.putFile(IndividualFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                         ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                             @Override
                             public void onSuccess(Uri uri) {
                                 String url = String.valueOf(uri);
                                 //  StoreLink(url);

                             }
                         });

                     }
                 });
             }





             Log.i("sspsdp4", String.valueOf(list));
             StringBuilder stringBuilder = new StringBuilder();
             for (String s : list) {
                 stringBuilder.append(s);
                 stringBuilder.append(",");
             }

             SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA_listaImagem, MODE_PRIVATE);
             SharedPreferences.Editor editor = sharedPreferences.edit();
             editor.putString("list", stringBuilder.toString());
             editor.commit();
             MostrarHQ();
         }

    }

*/
private void MostrarHQ(){
    dragMgr = new RecyclerViewDragDropManager();
    SharedPreferences sharedPreferences_img = getSharedPreferences(ARQUIVO_PREFERENCIA_listaImagem, MODE_PRIVATE);
    String string_img=sharedPreferences_img.getString("list","");
    String[] item_img=string_img.split(",");
    hq_model=new ArrayList<>();
    List<String> list_img=new ArrayList<String>();
    for(int i=0; i<item_img.length;i++){
        list_img.add(item_img[i]);
        hq_model.add(new HQ_Model(i,item_img[i]));
        Log.i("asodksdoksd", String.valueOf(i));

    }
    for(int i=0;i<list_img.size();i++){
    }




    adapter = dragMgr.createWrappedAdapter(new Adapter_HQ_Producao(hq_model, getApplicationContext()));
    recyclerView_hq.setAdapter(adapter);
    //recyclerView_hq.setLayoutManager(new LinearLayoutManager(this));
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext (), 3 );
    recyclerView_hq.setLayoutManager (layoutManager);
    // recyclerView_hq.setHasFixedSize ( true );
    //  dragMgr.attachRecyclerView(recyclerView_hq);
    dragMgr.attachRecyclerView(recyclerView_hq);
    dragMgr.setInitiateOnMove(false);
    dragMgr.setInitiateOnLongPress(true);



    }




}

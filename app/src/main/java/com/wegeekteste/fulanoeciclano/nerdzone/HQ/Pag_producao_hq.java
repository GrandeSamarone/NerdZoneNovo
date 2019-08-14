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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes.Adapter_HQ_Producao;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Novo_Grupo_Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.Base64Custom;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ_Model;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Suporte.Contato;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Pag_producao_hq extends TrocarFundo {

    private Toolbar toolbar;
    private TextView toolbarnome;
    private Button botao_carregar,botao_proximo;
    private RecyclerViewDragDropManager dragMgr;
    private RecyclerView recyclerView_hq;
    private  int reqCode =  12 ;
    private RecyclerView.Adapter adapter;
    private ArrayList<HQ_Model> hq_model;
    private ArrayList<String> list= new ArrayList<>();
    private ArrayList<String> lista_url= new ArrayList<>();
    private FirebaseFirestore db;
    private int upload_count = 0;
    private Dialog dialog;
    TextView textView;
    private String identificadorUsuario;
    private static  final String ARQUIVO_PREFERENCIA_listaImagem ="Lista_imagem";
    private StorageReference storageReference;
    private ArrayList<Uri> arrayListImageDUrl =new ArrayList<Uri>();
    private ArrayList<StorageReference> arrayListImageRef = new ArrayList<StorageReference>();

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
                Salvar_fotos();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void Carregar_HQ() {
        //open album
       GalleryConfig config = new GalleryConfig.Build()
                .limitPickPhoto(50)
                .singlePhoto(false)
                .hintOfPick("this is pick hint")
                .filterMimeTypes(new String[]{})
                .build();
        GalleryActivity.openActivity(Pag_producao_hq.this, reqCode, config);

    }







   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Recebe as imagens da galeria
         if(data!=null) {
             list = (ArrayList) data.getSerializableExtra(GalleryActivity.PHOTOS);




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


private void MostrarHQ(){
    dragMgr = new RecyclerViewDragDropManager();
    SharedPreferences sharedPreferences_img = getSharedPreferences(ARQUIVO_PREFERENCIA_listaImagem, MODE_PRIVATE);
    String string_img=sharedPreferences_img.getString("list","");
    String[] item_img=string_img.split(",");
    hq_model=new ArrayList<>();
    ArrayList<String> list_img=new ArrayList<String>();
    for(int i=0; i<item_img.length;i++){
        list_img.add(item_img[i]);
        hq_model.add(new HQ_Model(i,item_img[i]));
      //  Log.i("asodksdoksd", String.valueOf(i));

    }
    for(int i=0;i<list_img.size();i++){
        Uri uri = Uri.fromFile(new File(list_img.get(i)));

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

    private void Salvar_fotos(){
        SharedPreferences sharedPreferences_img = getSharedPreferences(ARQUIVO_PREFERENCIA_listaImagem, MODE_PRIVATE);
        String string_img=sharedPreferences_img.getString("list","");
        String[] item_img=string_img.split(",");
        hq_model=new ArrayList<>();
        ArrayList<String> list_img=new ArrayList<String>();
        for(int i=0; i<item_img.length;i++){
            list_img.add(item_img[i]);
            hq_model.add(new HQ_Model(i,item_img[i]));
            //  Log.i("asodksdoksd", String.valueOf(i));

        }
        for(int i=0;i<list_img.size();i++){
            Uri uri = Uri.fromFile(new File(list_img.get(i)));
            upload_Fotos_selecionadas(uri, i);

            SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA_listaImagem, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("list");
            editor.commit();
        }
    }
    private void upload_Fotos_selecionadas(Uri uri, int i) {
        final StorageReference ImageFolder =  FirebaseStorage.getInstance().getReference().child("imagens");
        String nomeImagem = UUID.randomUUID().toString();
        final StorageReference imagename = ImageFolder
                .child("HQ")
                .child(identificadorUsuario)
                .child(nomeImagem);

  //      arrayListImageRef.add(ImageFolder); //arraylist of type StorageRef \\
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(Pag_producao_hq.this);
        final View view = layoutInflater.inflate(R.layout.dialog_carregando_gif_analisando, null);
        ImageView imageViewgif = view.findViewById(R.id.gifimage);

        if (!Pag_producao_hq.this.isFinishing()) {
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.gif_analizando)
                    .into(imageViewgif);
            builder.setView(view);
        }
        dialog = builder.create();
        dialog.show();

        arrayListImageRef.add(imagename);
        imagename.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String urlConvertida = uri.toString();


                              /*  ArrayList<String> imageRefString = new ArrayList<>();
                                for (int i = 0; i < arrayListImageRef.size(); i++) {
                                    imageRefString.add(arrayListImageRef.get(i).toString());
                                }
                                */
                                arrayListImageDUrl.add(uri);
                                ArrayList<String> imageDUrlString = new ArrayList<>();
                                for (int i = 0; i < arrayListImageDUrl.size(); i++) {
                                    imageDUrlString.add(arrayListImageDUrl.get(i).toString());
                                }
                               String nome_doc= Base64Custom.codificarBase64(arrayListImageDUrl.get(0).toString());
                                HQ hq_modelo= new HQ();
                                lista_url.add(urlConvertida);
                                if(arrayListImageDUrl.size()==lista_url.size()){
                                    hq_modelo.setList_img(lista_url);
                                    hq_modelo.setId_author(identificadorUsuario);
                                    hq_modelo.setId(nome_doc);
                                    hq_modelo.SalvarHQ();
                                    dialog.dismiss();
                                    Intent it =new Intent(Pag_producao_hq.this,Producaohq_continuacao.class);
                                    it.putExtra("id_hq",hq_modelo.getId());
                                    startActivity(it);
                                }
                              //  finish();
                              /*  arrayListImageDUrl.add(uri);

                                ArrayList<String> imageRefString = new ArrayList<>();
                                for (int i = 0; i < arrayListImageRef.size(); i++) {
                                    imageRefString.add(arrayListImageRef.get(i).toString());
                                }

                                ArrayList<String> imageDUrlString = new ArrayList<>();
                                for (int i = 0; i < arrayListImageDUrl.size(); i++) {
                                    imageDUrlString.add(arrayListImageDUrl.get(i).toString());
                                }

                                Map<String, Object> map = new HashMap<>();
                                map.put("imageRef", imageRefString);
                                map.put("imageDownloadUrl", imageDUrlString);

                                  */
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (!isFinishing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }


}

package com.wegeekteste.fulanoeciclano.nerdzone.HQ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes.Adapter_HQ_Producao;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ_Model;
import com.wegeekteste.fulanoeciclano.nerdzone.R;
import com.wegeekteste.fulanoeciclano.nerdzone.Suporte.Contato;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pag_producao_hq extends AppCompatActivity {

    private Button botao_carregar;
    private RecyclerViewDragDropManager dragMgr;
    private RecyclerView recyclerView_hq;
    private  int reqCode =  12 ;
    private RecyclerView.Adapter adapter;
    private ArrayList<HQ_Model> hq_model;
    private ArrayList<String> list= new ArrayList<>();
    private static  final String ARQUIVO_PREFERENCIA_listaImagem ="Lista_imagem";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_producao_hq);

        botao_carregar= findViewById(R.id.carregar_img_hq);
        recyclerView_hq = findViewById(R.id.rec_hq);


        botao_carregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Carregar_HQ();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        MostrarHQ();
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
         }

    }


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
        Log.i("listitem2", "id "+hq_model.get(i).getImg_id()+"caminho "+hq_model.get(i).getImg_name());
    }
    for(int i=0;i<list_img.size();i++){
        Log.i("listitem",list_img.get(i));
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

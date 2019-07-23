package com.wegeekteste.fulanoeciclano.nerdzone.HQ;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes.Adapter_HQ_Producao;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ_Model;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

public class Pag_producao_hq extends AppCompatActivity {


    private Button botao_carregar;
    private RecyclerView recyclerView_hq;
    private  int reqCode =  12 ;
    private Adapter_HQ_Producao adapter;
    private List<HQ_Model> HQ_model =new ArrayList<>();
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
// Setup D&D feature and RecyclerView
        RecyclerViewDragDropManager dragMgr = new RecyclerViewDragDropManager();

        dragMgr.setInitiateOnMove(false);
        dragMgr.setInitiateOnLongPress(true);

       // recyclerView_hq.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView_hq.setAdapter(dragMgr.createWrappedAdapter(new MyAdapter()));

        dragMgr.attachRecyclerView(recyclerView_hq);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext (), 2 );
        recyclerView_hq.setLayoutManager (layoutManager);
        recyclerView_hq.setHasFixedSize ( true );

    }




    private void Carregar_HQ() {
        //open album
        GalleryConfig config = new GalleryConfig.Build()
                .limitPickPhoto(10)
                .singlePhoto(false)
                .hintOfPick("this is pick hint")
                .filterMimeTypes(new String[]{})
                .build();
        GalleryActivity.openActivity(Pag_producao_hq.this, reqCode, config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //list of photos of seleced
        HQ_model = (List<HQ_Model>) data.getSerializableExtra(GalleryActivity.PHOTOS);



        Log.i("lsditre7", String.valueOf(HQ_model));
        adapter = new Adapter_HQ_Producao(HQ_model, getApplicationContext());
        recyclerView_hq.setAdapter(adapter);
        //  adapter.notifyDataSetChanged();
        //list of videos of seleced
        //  List<String> vides = (List<String>) data.getSerializableExtra(GalleryActivity.VIDEOS);
    }

}

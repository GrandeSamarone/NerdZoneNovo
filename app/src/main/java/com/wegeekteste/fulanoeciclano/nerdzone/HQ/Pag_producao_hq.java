package com.wegeekteste.fulanoeciclano.nerdzone.HQ;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private RecyclerViewDragDropManager dragMgr;
    private RecyclerView recyclerView_hq;
    private  int reqCode =  12 ;
    private RecyclerView.Adapter adapter;
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
        dragMgr = new RecyclerViewDragDropManager();

        dragMgr.setInitiateOnMove(false);
        dragMgr.setInitiateOnLongPress(true);


       // recyclerView_hq.setAdapter(dragMgr.createWrappedAdapter(new MyAdapter()));

        recyclerView_hq.setLayoutManager(new LinearLayoutManager(this));
      //  RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext (), 3 );
        //recyclerView_hq.setLayoutManager (layoutManager);
        recyclerView_hq.setHasFixedSize ( true );
      //  dragMgr.attachRecyclerView(recyclerView_hq);

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
        ArrayList list = (ArrayList) data.getSerializableExtra(GalleryActivity.PHOTOS);
        Receber_Imagem(list);
        Log.i("lsditre7", String.valueOf(HQ_model));
        //adapter = new Adapter_HQ_Producao(HQ_model, getApplicationContext());
       // recyclerView_hq.setAdapter(dragMgr.createWrappedAdapter(adapter));
       // dragMgr.attachRecyclerView(recyclerView_hq);
        //  adapter.notifyDataSetChanged();
        //list of videos of seleced
        //  List<String> vides = (List<String>) data.getSerializableExtra(GalleryActivity.VIDEOS);
    }

    private final String image_titles[] = {
            "Img1",
            "Img2",
            "Img3",
            "Img4",
            "Img5",
    };

  private ArrayList<HQ_Model> Receber_Imagem(ArrayList lista){


      ArrayList<HQ_Model> theimage = new ArrayList<>();
      for(int i = 0; i< lista.size(); i++){
          HQ_Model createList = new HQ_Model();
          createList.setImg_name(String.valueOf(lista.get(i)));
          createList.setImg_id(image_titles[i]);
          Log.i("lsditre77", createList.getImg_name());
          Log.i("lsditre79", createList.getImg_id());

          theimage.add(createList);
           // adapter.notifyDataSetChanged();


      }
      adapter = dragMgr.createWrappedAdapter(new Adapter_HQ_Producao(theimage, getApplicationContext()));
      recyclerView_hq.setAdapter(adapter);
      dragMgr.attachRecyclerView(recyclerView_hq);
      return theimage;

  }

}

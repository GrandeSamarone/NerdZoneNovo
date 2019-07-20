package com.wegeekteste.fulanoeciclano.nerdzone.HQ;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

public class Pag_producao_hq extends AppCompatActivity {


    private Button botao_carregar;
    private RecyclerView recyclerView_hq;
    private  int reqCode =  12 ;
    private List<String> photos;
    private BaseAdapter adapter;
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

        recyclerView_hq.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_hq.setAdapter(dragMgr.createWrappedAdapter(new MyAdapter()));

        dragMgr.attachRecyclerView(recyclerView_hq);



    }

    static class MyItem {
        public final long id;
        public final String text;

        public MyItem(long id, String text) {
            this.id = id;
            this.text = text;
        }
    }

    static class MyViewHolder extends AbstractDraggableItemViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

    static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements DraggableItemAdapter<MyViewHolder> {
        List<MyItem> mItems;

        public MyAdapter() {
            setHasStableIds(true); // this is required for D&D feature.

            mItems = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                mItems.add(new MyItem(i, "Item " + i));
            }
        }

        @Override
        public long getItemId(int position) {
            return mItems.get(position).id; // need to return stable (= not change even after reordered) value
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.membro_grupo_online, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            MyItem item = mItems.get(position);
            holder.textView.setText(item.text);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public void onMoveItem(int fromPosition, int toPosition) {
            MyItem movedItem = mItems.remove(fromPosition);
            mItems.add(toPosition, movedItem);
        }

        @Override
        public boolean onCheckCanStartDrag(@NonNull MyViewHolder holder, int position, int x, int y) {
            return true;
        }

        @Override
        public ItemDraggableRange onGetItemDraggableRange(@NonNull MyViewHolder holder, int position) {
            return null;
        }

        @Override
        public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
            return true;
        }

        @Override
        public void onItemDragStarted(int position) {
        }

        @Override
        public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        }
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
        photos = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
        adapter.notifyDataSetChanged();
        //list of videos of seleced
        //  List<String> vides = (List<String>) data.getSerializableExtra(GalleryActivity.VIDEOS);
    }

}

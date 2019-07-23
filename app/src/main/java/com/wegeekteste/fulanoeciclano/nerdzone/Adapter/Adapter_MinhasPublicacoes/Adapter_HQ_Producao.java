package com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ_Model;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_HQ_Producao  extends RecyclerView.Adapter<Adapter_HQ_Producao.MyViewHolder> implements DraggableItemAdapter<Adapter_HQ_Producao.MyViewHolder> {

    private List<HQ_Model> listHQs;
    private Context context;


    public Adapter_HQ_Producao(List<HQ_Model> listar,Context c){
        this.context=c;
        this.listHQs=listar;
    }

    public List<HQ_Model> getListHQs(){
        return this.listHQs;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.membro_grupo_online, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//           HQ_Model hq= listHQs.get(position);
        if(listHQs.get(position).getImg_id()!=null) {
            Log.i("744o", listHQs.get(position).getImg_id());
            Uri uri = Uri.parse(listHQs.get(position).getImg_id());
            Glide.with(context)
                    .load(uri)
                    .into(holder.img);
        }else{
            Log.i("744o","null");
        }

    }

    @Override
    public int getItemCount() {
        return listHQs.size();
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull MyViewHolder holder, int position, int x, int y) {
        return false;
    }

    @Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull MyViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {

    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return false;
    }

    @Override
    public void onItemDragStarted(int position) {

    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
     CircleImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           img=itemView.findViewById(R.id.Foto_membro_grupo);
        }
    }
}

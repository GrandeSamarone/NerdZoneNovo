package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Membro_Grupo extends RecyclerView.Adapter<Adapter_Membro_Grupo.MyViewHolder> {

    private List<Membro_Grupo> membro_grupos;
    private Context context;
    public Adapter_Membro_Grupo(List<Membro_Grupo> membro_grupos, Context context){
        this.membro_grupos = membro_grupos;
        this.context=context;
    }
    public List<Membro_Grupo> getMembro(){
        return this.membro_grupos;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.membro_grupo_online,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Membro_Grupo membro = membro_grupos.get(position);

        if (membro.getFoto_usuario() != null) {
            Uri uri= Uri.parse(membro.getFoto_usuario());
            Glide.with(context)
                    .load(uri)
                    .into(holder.foto__icon);
        }else {
            holder.foto__icon.setImageResource(R.mipmap.ic_launcher);
        }



    }



    @Override
    public int getItemCount() {
        return membro_grupos.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {


        CircleImageView foto__icon;



        public MyViewHolder(View itemView) {
            super(itemView);

            foto__icon = itemView.findViewById(R.id.Foto_membro_grupo);

        }
    }
}

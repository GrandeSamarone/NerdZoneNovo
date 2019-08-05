package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Chat_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Membro_Grupo extends RecyclerView.Adapter<Adapter_Membro_Grupo.MyViewHolder> {

    private List<Membro_Grupo> membro_grupos;
    private Context context;
    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;
    public Adapter_Membro_Grupo(List<Membro_Grupo> membro_grupos, Context context){
        this.membro_grupos = membro_grupos;
        this.context=context;
    }
    public List<Membro_Grupo> getMembro(){
        return this.membro_grupos;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.membro_grupo_online, parent, false);
            return new Adapter_Membro_Grupo.MyViewHolder(item);

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
    public int getItemViewType(int position) {
        Membro_Grupo membro = membro_grupos.get(position);
        String idUser = UsuarioFirebase.getIdentificadorUsuario();
        if (membro.getId_usuario().equals(idUser)){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
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

            foto__icon = itemView.findViewById(R.id.foto_membro_grupo);

        }
    }
}

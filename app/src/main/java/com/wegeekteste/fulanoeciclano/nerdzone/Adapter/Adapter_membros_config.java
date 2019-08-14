package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_membros_config extends RecyclerView.Adapter<Adapter_membros_config.MyviewHolder> {
    private List<Membro_Grupo> membro_grupos;
    private Context context;
    private String identificadorUsuario;
    public static  final int MSG_TYPE_USER = 0;
    public static  final int MSG_TYPE_ADMIN= 1;
    public Adapter_membros_config(List<Membro_Grupo> list, Context c) {
        this.context = c;
        this.membro_grupos = list;
    }
    public List<Membro_Grupo> getMembros(){
        return this.membro_grupos;
    }
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_membro_config_user, parent, false);
            return new Adapter_membros_config.MyviewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Membro_Grupo membro = membro_grupos.get(position);
        Glide.with(context).load(membro.getFoto_usuario())
                .thumbnail(0.5f)
                .into(holder.icone);

          if(membro.getNome_usuario()!=null) {
              holder.nome.setText(membro.getNome_usuario());
          }else{
              holder.nome.setText("nome");
          }

       if(membro.getBloqueio()){
           holder.bloq.setVisibility(View.VISIBLE);
       }
        if(membro.getAdmin()){
            holder.admin.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return membro_grupos.size();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder{
        private CircleImageView bloq,icone;
        private TextView nome,admin;
        private RelativeLayout line_membro;

        public MyviewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nome_membro_grupo);
            admin=itemView.findViewById(R.id.admin_membro_grupo);
            icone = itemView.findViewById(R.id.icon_membro);
            bloq=itemView.findViewById(R.id.img_bloq_user);
        }
    }



}



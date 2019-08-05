package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.database.FirebaseDatabase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Comercio;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_solicitacao_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Convites extends RecyclerView.Adapter<Adapter_Convites.MyViewHolder> {

    private List<Membro_solicitacao_grupo> membro_solitando;
    private Context context;
    private FirebaseDatabase databases=FirebaseDatabase.getInstance();
    public Adapter_Convites(List<Membro_solicitacao_grupo> solicitacao, Context context){
        this.membro_solitando = solicitacao;
        this.context=context;
    }


    @NonNull
    @Override
    public Adapter_Convites.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_convite,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Convites.MyViewHolder holder, int position) {
        Membro_solicitacao_grupo membro= membro_solitando.get(position);

        if(membro.getNome_usuario() != null){
             holder.nome.setText(membro.getNome_usuario());
        }


            holder.descricao.setText("grupo_tal");

        if(membro.getFoto_usuario()!=null){
            Glide.with(context)
                    .load(membro.getFoto_usuario())
                    .into(holder.foto);

        }

    }

    @Override
    public int getItemCount() {
        return membro_solitando.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       private TextView nome,descricao;
       private CircleImageView foto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome=itemView.findViewById(R.id.nome_titula_convite);
            descricao=itemView.findViewById(R.id.text_convite);
            foto=itemView.findViewById(R.id.foto_titula_convite);
        }
    }
}

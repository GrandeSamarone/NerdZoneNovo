package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
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

package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class Adapter_Forum_Grupo extends RecyclerView.Adapter<Adapter_Forum_Grupo.MyViewHolder> {

    private List<Forum> foruns;
    private Context context;
    private FirebaseFirestore db;
    public Adapter_Forum_Grupo(List<Forum> forum, Context context){
        this.foruns = forum;
        this.context=context;
    }
    public List<Forum> getForuns(){
        return this.foruns;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_forum_grupo,parent,false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Forum forum = foruns.get(position);
        if (forum.getTitulo() != null) {
            holder.titulo.setText(forum.getTitulo());
        }
        if (forum.getDescricao()!= null) {
            holder.legenda.setText(forum.getDescricao());
        }
        if (forum.getFoto() != null) {
            String stringcapa = forum.getFoto();
            if (stringcapa != null) {
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(stringcapa))
                        .setLocalThumbnailPreviewsEnabled(true)
                        .setProgressiveRenderingEnabled(true)
                        .setResizeOptions(new ResizeOptions(100, 100))
                        .build();

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .build();
                holder.capa.setController(controller);
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);

                GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setRoundingParams(roundingParams)
                        .setProgressBarImage(new CircleProgressDrawable())
                        //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                        .build();
                holder.capa.setHierarchy(hierarchy);

            } else {
                Toast.makeText(context, "erro", Toast.LENGTH_SHORT).show();
            }
        }

            holder.Quant_Online.setText(String.valueOf(forum.getMembro_count()));



    }



    @Override
    public int getItemCount() {
        return foruns.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView legenda;
        TextView categoria;
        TextView estado;
        SimpleDraweeView capa;
        TextView Quant_Online;



        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.texttitulo);
            legenda = itemView.findViewById(R.id.textlegenda);
            categoria = itemView.findViewById(R.id.textcategoria);
            estado = itemView.findViewById(R.id.textestado);
            capa = itemView.findViewById(R.id.capamercado);
            Quant_Online = itemView.findViewById(R.id.text_quant_online);
        }
    }
}


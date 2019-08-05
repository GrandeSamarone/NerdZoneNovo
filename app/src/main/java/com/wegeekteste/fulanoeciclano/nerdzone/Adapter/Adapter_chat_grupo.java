package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.wegeekteste.fulanoeciclano.nerdzone.Abrir_Imagem.AbrirImagem;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Chat_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_chat_grupo extends   RecyclerView.Adapter<Adapter_chat_grupo.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat_Grupo> mChat;
    private String imageurl;

    public Adapter_chat_grupo(Context mContext, List<Chat_Grupo> mChat){
        this.mChat = mChat;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_mensagem_remetentes, parent, false);
            return new Adapter_chat_grupo.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_mensagem_destinatario, parent, false);
            return new Adapter_chat_grupo.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat_Grupo chat = mChat.get(position);

        holder.show_message.setText(chat.getMensagem());
        if(chat.getNome_usuario()!=null) {
            holder.name_chat.setText(chat.getNome_usuario());
        }else{
            holder.name_chat.setText("Sem Nome");
        }
        if (chat.getFoto_usuario()!=null){
            Glide.with(mContext)
                    .load(chat.getFoto_usuario())
                    .thumbnail(0.1f)
                    .into(holder.profile_image);
        } else {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }

        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("entregue");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        } if(chat.getMensagem_img()!=null){
             holder.line.setVisibility(View.VISIBLE);
             holder.chat_img.setVisibility(View.VISIBLE);
             holder.chat_img.setImageResource(R.drawable.icon_foto_fanarts);
             holder.show_message.setVisibility(View.GONE);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(chat.getMensagem_img()))
                    .setLocalThumbnailPreviewsEnabled(true)
                    .setProgressiveRenderingEnabled(true)
                    .setResizeOptions(new ResizeOptions(100, 100))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            holder.chat_img.setController(controller);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(8f);

            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setRoundingParams(roundingParams)
                    .setProgressBarImage(new CircleProgressDrawable())
                    //  .setPlaceholderImage(context.getResources().getDrawable(R.drawable.carregando))
                    .build();
            holder.chat_img.setHierarchy(hierarchy);
        }

        holder.chat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it =new Intent(mContext, AbrirImagem.class);
                it.putExtra("id_foto", chat.getMensagem_img());
                it.putExtra("nome_foto", chat.getNome_usuario());
                mContext.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        private TextView show_message;
        private CircleImageView profile_image;
        private TextView txt_seen,name_chat;
        private LinearLayout line;
        private SimpleDraweeView chat_img;
        public ViewHolder(View itemView) {
            super(itemView);
            line=itemView.findViewById(R.id.line_img_chat);
            chat_img=itemView.findViewById(R.id.img_msg_grupo);
            name_chat=itemView.findViewById(R.id.name_chat_grupo_destinatario);
            show_message = itemView.findViewById(R.id.textMensagemtexto);
           profile_image = itemView.findViewById(R.id.icon_chat_grupo_destinario);
            txt_seen = itemView.findViewById(R.id.tempodochat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Chat_Grupo chatpos=mChat.get(position);
        String idUser = UsuarioFirebase.getIdentificadorUsuario();
        if (chatpos.getId_usuario().equals(idUser)){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }



}
package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Chat_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

public class Adapter_chat_grupo extends   RecyclerView.Adapter<Adapter_chat_grupo.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat_Grupo> mChat;
    private String imageurl;

    public Adapter_chat_grupo(Context mContext, List<Chat_Grupo> mChat){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
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
/*
        if (chat.getFoto_usuario().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(chat.getFoto_usuario()).into(holder.profile_image);
        }
*/
        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("entregue");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
      //  public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.textMensagemtexto);
           // profile_image = itemView.findViewById(R.id.chat_img_usuario);
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
package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiTextView;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Comentario;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.PerfilAmigos.Perfil;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_comentario extends RecyclerView.Adapter<Adapter_comentario.MyViewHolder> {

    private List<Comentario> comentarios;
    private Context context;
    private  String usuarioLogado =  UsuarioFirebase.getIdentificadorUsuario();
    private String Criador;

    public Adapter_comentario(List<Comentario> comentario,Context c){
        this.comentarios=comentario;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item=null;
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_commentario, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comentario comentario = comentarios.get(position);

        holder.mensagem.setText(comentario.getText());


    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome;
        private CircleImageView foto;
        private EmojiTextView mensagem;

        public MyViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.commentario_author);
            mensagem = itemView.findViewById(R.id.comentario_mensagem);
            foto = itemView.findViewById(R.id.commentario_foto);
        }
    }
}

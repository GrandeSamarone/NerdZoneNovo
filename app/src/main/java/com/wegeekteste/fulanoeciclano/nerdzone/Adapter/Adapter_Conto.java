package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.ContoLike;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto_colecao;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Usuario;
import com.wegeekteste.fulanoeciclano.nerdzone.PerfilAmigos.Perfil;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Conto extends RecyclerView.Adapter<Adapter_Conto.MyviewHolder> {

    private List<Conto> listaconto;
    private Context context;
    Usuario usuariologado = UsuarioFirebase.getDadosUsuarioLogado();
    String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
    public Adapter_Conto(List<Conto> conto,Context c){
        this.listaconto=conto;
        this.context=c;
    }
    public List<Conto> getConto(){
       return this.listaconto;}

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterconto,parent,false);

       return  new MyviewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        final Conto conto = listaconto.get(position);

        holder.conto.setText(conto.getMensagem());
        holder.nome_conto.setText(conto.getTitulo());

        DatabaseReference eventoscurtidas= ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios")
                .child("bWFybG9zdHJpbmlkYWRAZ21haWwuY29t");
        eventoscurtidas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario  user = dataSnapshot.getValue(Usuario.class);

            holder.author.setText(user.getNome());
            Log.i("sdsd",user.getId()+"id "+identificadorUsuario);
            if(!user.getId().equals(identificadorUsuario)) {

                holder.author.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(context, Perfil.class);
                        it.putExtra("id", user.getId());
                        context.startActivity(it);
                    }
                });
            }else{
                holder.author.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(context, MinhaConta.class);
                        it.putExtra("id", user.getId());
                        context.startActivity(it);
                    }
                });
            }
            /*Glide.with(context)
                        .load(user.getFoto())
                        .into(holder.imgperfil );*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return listaconto.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        private TextView conto,nome_conto,n_curtida,txt_add_colecao,author;
        private CircleImageView imgperfil;
        private SparkButton botaocurtir,botao_add_colecao;
        public MyviewHolder(View itemView) {
            super(itemView);
            author= itemView.findViewById(R.id.conto_author);
            conto = itemView.findViewById(R.id.conto_mensagem);
            txt_add_colecao = itemView.findViewById(R.id.txt_add_colecao);
            n_curtida = itemView.findViewById(R.id.conto_num_curit);
            nome_conto = itemView.findViewById(R.id.conto_titulo);
          //  imgperfil = itemView.findViewById(R.id.conto_foto_autor);
            botaocurtir = itemView.findViewById(R.id.botaocurtirconto);
            botao_add_colecao = itemView.findViewById(R.id.botao_add_a_colecao);

        }
    }
}

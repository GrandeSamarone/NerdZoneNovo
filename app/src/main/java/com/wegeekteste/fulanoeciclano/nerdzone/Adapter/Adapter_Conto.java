package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.varunest.sparkbutton.SparkButton;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Conto;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Conto extends RecyclerView.Adapter<Adapter_Conto.MyviewHolder> {

    private List<Conto> listaconto;
    private Context context;
    String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
    private FirebaseFirestore db;
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

        holder.conto_Mensagem.setText(conto.getMensagem());
        holder.nome_conto.setText(conto.getTitulo());
        holder.author.setText(conto.getNomeauthor());
//           Log.i("sdsdsd",conto.getNomeauthor());
       /* db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Usuarios").document(conto.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       Usuario user = document.toObject(Usuario.class);
                        holder.author.setText(user.getNome());
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

*/


    }

    @Override
    public int getItemCount() {
        return listaconto.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        private TextView conto_Mensagem,nome_conto,n_curtida,txt_add_colecao,author;
        private CircleImageView imgperfil;
        private SparkButton botaocurtir,botao_add_colecao;
        public MyviewHolder(View itemView) {
            super(itemView);
            author= itemView.findViewById(R.id.conto_author);
            conto_Mensagem = itemView.findViewById(R.id.conto_mensagem);
            txt_add_colecao = itemView.findViewById(R.id.txt_add_colecao);
            n_curtida = itemView.findViewById(R.id.conto_num_curit);
            nome_conto = itemView.findViewById(R.id.conto_titulo);
          //  imgperfil = itemView.findViewById(R.id.conto_foto_autor);
            botaocurtir = itemView.findViewById(R.id.botaocurtirconto);
            botao_add_colecao = itemView.findViewById(R.id.botao_add_a_colecao);

        }
    }
}

package com.wegeekteste.fulanoeciclano.nerdzone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Info_Grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum.APIService;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.CircleProgressDrawable;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Comercio;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Membro_solicitacao_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Client;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Data;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.MyResponse;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Sender;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Convites extends RecyclerView.Adapter<Adapter_Convites.MyViewHolder> {

    private List<Membro_solicitacao_grupo> membro_solitando;
    private Context context;
    boolean notify = false;
    APIService apiService;
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


            holder.descricao.setText(membro.getNome_grupo());

        if(membro.getFoto_usuario()!=null){
            Glide.with(context)
                    .load(membro.getFoto_usuario())
                    .into(holder.foto);

        }
        holder.aceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db;
                db = FirebaseFirestore.getInstance();
                HashMap<String, Object> membrosMap = new HashMap<>();
                membrosMap.put("id_usuario",membro.getId_usuario());
                membrosMap.put("bloqueio",false);
                membrosMap.put("permissao",true);
                membrosMap.put("foto_usuario",membro.getFoto_usuario());
                membrosMap.put("nome_usuario", membro.getNome_usuario());
                membrosMap.put("id_grupo", membro.getId_grupo());
                membrosMap.put("nome_grupo",membro.getNome_grupo());
                membrosMap.put("id_admin_grupo",membro.getId_admin_grupo());
                notify=true;
                if (notify) {
                    sendNotifiaction( membro.getId_admin_grupo(),membro.getNome_usuario(), "Olá quero participar do  "+membro.getNome_grupo());

                }
                notify = false;
                db.collection("WeForum").document(membro.getId_grupo()).collection("Membros").add(membrosMap);
            }
        });

    }

    private void sendNotifiaction(String id_admin_grupo, String nome_usuario, String s) {
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }



    @Override
    public int getItemCount() {
        return membro_solitando.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       private TextView nome,descricao;
       private CircleImageView foto;
       private Button aceita,recusar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome=itemView.findViewById(R.id.nome_titula_convite);
            descricao=itemView.findViewById(R.id.text_convite);
            foto=itemView.findViewById(R.id.foto_titula_convite);
            aceita=itemView.findViewById(R.id.botao_aceita);
            recusar=itemView.findViewById(R.id.botao_recusar);
        }
    }
}

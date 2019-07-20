package com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Forum;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Page_Info_Grupo extends AppCompatActivity {

    private Button botao_entrar_grupo;
    private CircleImageView img_perfil_grupo;
    private TextView nome_info,descricao_info,criador_info,membros_info;
    private Forum forum;
    private String id_grupo_selecionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_permissao);

           //Configuracoes Básicas
            img_perfil_grupo=findViewById(R.id.icone_grupo_info);
            nome_info=findViewById(R.id.nome_grupo_info);
            descricao_info=findViewById(R.id.desc_grupo_info);
            criador_info=findViewById(R.id.criador_grupo_info);
            membros_info=findViewById(R.id.membros_grupo_info);
            botao_entrar_grupo = findViewById(R.id.botao_grupo_info);


            botao_entrar_grupo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(Page_Info_Grupo.this, Page_Chat_grupo.class);
                    it.putExtra("forum_selecionado",forum);
                    it.putExtra("id_forum_selecionado",id_grupo_selecionado);
                    startActivity(it);

                }
            });
        //Recuperar dados do usuario Selecionado;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("grupo_id")) {
            id_grupo_selecionado= String.valueOf(bundle.getSerializable("grupo_id"));
            Log.i("sdfsd7", String.valueOf(bundle.containsKey("grupo_id")));

        }
        if(bundle!=null){
         if(bundle.containsKey("grupo_info")){
           forum= (Forum) bundle.getSerializable("grupo_info");
             nome_info.setText(forum.getTitulo());
             descricao_info.setText(forum.getDescricao());
             criador_info.setText(forum.getNomeauthor());

           String foto = forum.getFoto();
            if(foto!=null){
                Uri url = Uri.parse(foto);
                Glide.with(this)
                        .load(url)
                        .into(img_perfil_grupo);
            }
         }
           }
    }
}

package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chat_Grupo {

    private String id_usuario;
    private String nome_usuario;
    private String foto_usuario;
    private String id_grupo;
    private String mensagem;
    private String id_mensagem;
    private String mensagem_type;
    private String mensagem_img;
    private String status;
    private @ServerTimestamp Date tempo;
    private boolean isseen;


    public Chat_Grupo(String envia, String id_grupo, String msg, boolean isseen) {
        this.id_usuario = envia;
        this.id_grupo = id_grupo;
        this.mensagem = msg;
        this.isseen = isseen;
    }

    public Chat_Grupo() {
    }
    public  void Salvar_msg(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("mensagem",getMensagem());
        data.put("id_mensagem",getId_mensagem());
        data.put("mensagem_type",getMensagem_type());
        data.put("tempo", FieldValue.serverTimestamp());
        data.put("nome_usuario",getNome_usuario());
        data.put("foto_usuario",getFoto_usuario());
        data.put("status",getStatus());
        data.put("id_usuario",getId_usuario());
        data.put("id_grupo",getId_grupo());
              db
                .collection("WeForum").document(getId_grupo())
                .collection("Mensagens").add(data);
    };

    public  void Salvar_msg_img(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("mensagem",getMensagem());
        data.put("id_mensagem",getId_mensagem());
        data.put("mensagem_img",getMensagem_img());
        data.put("tempo", FieldValue.serverTimestamp());
        data.put("nome_usuario",getNome_usuario());
        data.put("foto_usuario",getFoto_usuario());
        data.put("status",getStatus());
        data.put("id_usuario",getId_usuario());
        data.put("id_grupo",getId_grupo());
        db
                .collection("WeForum").document(getId_grupo())
                .collection("Mensagens").add(data);
    };

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(String id_grupo) {
        this.id_grupo = id_grupo;
    }

    public Date getTempo() {
        return tempo;
    }

    public void setTempo(Date tempo) {
        this.tempo = tempo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem_img() {
        return mensagem_img;
    }

    public void setMensagem_img(String mensagem_img) {
        this.mensagem_img = mensagem_img;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }


    public String getFoto_usuario() {
        return foto_usuario;
    }

    public void setFoto_usuario(String foto_usuario) {
        this.foto_usuario = foto_usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_mensagem() {
        return id_mensagem;
    }

    public String getMensagem_type() {
        return mensagem_type;
    }

    public void setMensagem_type(String mensagem_type) {
        this.mensagem_type = mensagem_type;
    }

    public void setId_mensagem(String id_mensagem) {
        this.id_mensagem = id_mensagem;
    }
}

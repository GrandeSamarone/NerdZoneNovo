package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Mensagem_Grupo {
    private String id_usuario;
    private String mensagem;
    private String nome_usuario;
    private String foto_usuario;
    private @ServerTimestamp
    Date  tempo;

public Mensagem_Grupo(){

}

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
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
}

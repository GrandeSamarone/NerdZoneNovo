package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Membro_solicitacao_grupo {
    private String foto_usuario;
    private String id_admin_grupo;
    private String id_grupo;
    private String id_usuario;
    private String nome_usuario;
    private Boolean permissao;
    private Boolean bloqueio;

    public Membro_solicitacao_grupo(){

    }
    public void SalvarSolicitacao() {
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("id_usuario",getId_usuario());
        membrosMap.put("bloqueio",getBloqueio());
        membrosMap.put("permissao",getPermissao());
        membrosMap.put("foto_usuario",getFoto_usuario() );
        membrosMap.put("nome_usuario", getNome_usuario());
        membrosMap.put("id_grupo", getId_grupo());
        membrosMap.put("id_admin_grupo", getId_admin_grupo());

        db.collection("Permissao_Grupo").add(membrosMap);
    }

    public void cancelar() {
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("id_usuario",getId_usuario());
        membrosMap.put("bloqueio",getBloqueio());
        membrosMap.put("permissao",getPermissao());
        membrosMap.put("foto_usuario",getFoto_usuario() );
        membrosMap.put("nome_usuario", getNome_usuario());
        membrosMap.put("id_grupo", getId_grupo());
        membrosMap.put("id_admin_grupo", getId_admin_grupo());

        db.collection("Permissao_Grupo").add(membrosMap);
    }
    public String getFoto_usuario() {
        return foto_usuario;
    }

    public void setFoto_usuario(String foto_usuario) {
        this.foto_usuario = foto_usuario;
    }

    public String getId_admin_grupo() {
        return id_admin_grupo;
    }

    public void setId_admin_grupo(String id_admin_grupo) {
        this.id_admin_grupo = id_admin_grupo;
    }

    public String getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(String id_grupo) {
        this.id_grupo = id_grupo;
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

    public Boolean getPermissao() {
        return permissao;
    }

    public void setPermissao(Boolean permissao) {
        this.permissao = permissao;
    }

    public Boolean getBloqueio() {
        return bloqueio;
    }

    public void setBloqueio(Boolean bloqueio) {
        this.bloqueio = bloqueio;
    }



}

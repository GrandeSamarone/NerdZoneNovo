package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Membro_Grupo {
    private String id;
    private String id_usuario;
    private String nome_usuario;
    private String foto_usuario;
    private String token_usuario;
    private String status;
    private String id_grupo;
    private Boolean digitando;
    private Boolean permissao;
    private Boolean bloqueio;
    private Boolean admin;
    private String id_admin;



    public Membro_Grupo(){

    }
    public void  SalvarMembro(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> membrosMap = new HashMap<>();
        membrosMap.put("status", getStatus());
        membrosMap.put("id_usuario", getId_usuario());
        membrosMap.put("token_usuario", getToken_usuario());
        membrosMap.put("bloqueio",false);
        membrosMap.put("permissao",getPermissao());
        membrosMap.put("admin",getAdmin());
        membrosMap.put("id_admin",getId_admin());
        membrosMap.put("foto_usuario",getFoto_usuario() );
        membrosMap.put("nome_usuario", getNome_usuario());
        membrosMap.put("digitando", false);

        db.collection("WeForum").document(getId_grupo())
                .collection("Membros").document(getId_usuario()).set(membrosMap);

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(String id_grupo) {
        this.id_grupo = id_grupo;
    }


    public Boolean getDigitando() {
        return digitando;
    }

    public void setDigitando(Boolean digitando) {
        this.digitando = digitando;
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


    public String getToken_usuario() {
        return token_usuario;
    }

    public void setToken_usuario(String token_usuario) {
        this.token_usuario = token_usuario;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getId_admin() {
        return id_admin;
    }

    public void setId_admin(String id_admin) {
        this.id_admin = id_admin;
    }
}

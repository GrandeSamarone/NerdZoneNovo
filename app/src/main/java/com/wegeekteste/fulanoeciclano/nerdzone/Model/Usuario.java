package com.wegeekteste.fulanoeciclano.nerdzone.Model;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fulanoeciclano on 21/05/2018.
 */

public class Usuario implements Serializable {

    private String id;
    private String token;
    private String tipoconta;
    private String nome;
    private String frase;
    private String foto;
    private String Capa;
    private String tiposuario;
    private int seguidores = 0;
    private int seguindo = 0;
    private int topicos=0;
    private int contos=0;
    private int livros=0;
    private int arts = 0;
    private int comercio = 0;
    private int evento = 0;

    public Usuario() {
    }
    public void salvar(){

        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> newUsuario = new HashMap<>();
        newUsuario.put("id",identificadorUsuario);
        newUsuario.put("token",getToken());
        newUsuario.put("nome", getNome());
        newUsuario.put("frase", getFrase());
        newUsuario.put("foto", getFoto());
        newUsuario.put("seguidores",0);
        newUsuario.put("seguindo",0);
        newUsuario.put("contos",getContos());
        newUsuario.put("topicos",getTopicos());
        newUsuario.put("arts",getArts());
        newUsuario.put("comercio",getComercio());
        newUsuario.put("evento",getEvento());
        newUsuario.put("tipoconta",getTipoconta());
        newUsuario.put("tipousuario","user");

// Add a new document with a generated ID
        db.collection("Usuarios")
                .document(identificadorUsuario)
                .set(newUsuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        /*DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("usuarios").child(getId());
        usuario.setValue(this);

    */

    }
    public void atualizar(){
        String identificadorUSuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosref = database.child("usuarios")
                .child(identificadorUSuario);
        Map<String,Object> valoresUsuario = ConverterparaMap();
        usuariosref.updateChildren(valoresUsuario);
    }
    public void atualizarCapa(){
        String identificadorUSuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();

        DatabaseReference usuariosref = database.child("usuarios")
                .child(identificadorUSuario);
        Map<String,Object> valorUsuario = ConverterCapaparaMap();
        usuariosref.updateChildren(valorUsuario);
    }


    @Exclude
    public Map<String,Object> ConverterCapaparaMap(){
        HashMap<String,Object> usuarioMap = new HashMap<>();
        usuarioMap.put("capa",getCapa());
        return usuarioMap;
    }
    @Exclude
    public Map<String,Object> ConverterparaMap(){
        HashMap<String,Object> usuarioMap = new HashMap<>();
        usuarioMap.put("nome",getNome());
        usuarioMap.put("frase",getFrase());
        usuarioMap.put("foto",getFoto());

        return usuarioMap;
    }
    public void atualizarQtdTopicos(){
        String identificadorUSuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosref = database.child("usuarios")
                .child(identificadorUSuario);
        HashMap<String,Object> dados = new HashMap<>();
        dados.put("topicos",getTopicos());
        usuariosref.updateChildren(dados);
    }
    public void atualizarQtdContos(){
        String identificadorUSuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosref = database.child("usuarios")
                .child(identificadorUSuario);
        HashMap<String,Object> dados = new HashMap<>();
        dados.put("contos",getContos());
        usuariosref.updateChildren(dados);
    }

    public void atualizarQtdFanArts(){
        String identificadorUSuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosref = database.child("usuarios")
                .child(identificadorUSuario);
        HashMap<String,Object> dados = new HashMap<>();
        dados.put("arts",getArts());
        usuariosref.updateChildren(dados);
    }

    public void atualizarQtdComercio(){
        String identificadorUSuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosref = database.child("usuarios")
                .child(identificadorUSuario);
        HashMap<String,Object> dados = new HashMap<>();
        dados.put("arts",getArts());
        usuariosref.updateChildren(dados);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCapa() {
        return Capa;
    }

    public void setCapa(String capa) {
        Capa = capa;
    }

    public String getTiposuario() {
        return tiposuario;
    }

    public void setTiposuario(String tiposuario) {
        this.tiposuario = tiposuario;
    }

    public String getTipoconta() {
        return tipoconta;
    }

    public void setTipoconta(String tipoconta) {
        this.tipoconta = tipoconta;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getTopicos() {
        return topicos;
    }

    public void setTopicos(int topicos) {
        this.topicos = topicos;
    }

    public int getContos() {
        return contos;
    }

    public void setContos(int contos) {
        this.contos = contos;
    }

    public int getLivros() {
        return livros;
    }

    public void setLivros(int livros) {
        this.livros = livros;
    }

    public int getArts() {
        return arts;
    }

    public void setArts(int arts) {
        this.arts = arts;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }


    public int getComercio() {
        return comercio;
    }

    public void setComercio(int comercio) {
        this.comercio = comercio;
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

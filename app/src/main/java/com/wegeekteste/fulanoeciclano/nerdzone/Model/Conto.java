package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.Base64Custom;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Conto   implements Serializable {

    private String uid;
    private String idauthor;
    private String titulo;
    private String mensagem;
    private String data;
    private int likecount = 0;
    private int quantcolecao=0;


    public Conto() {

    }


    public  void SalvarConto(){
           // String IdConto= Base64Custom.codificarBase64("danlelis");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> newConto = new HashMap<>();
        newConto.put("id",getUid());
        newConto.put("titulo", getTitulo());
        newConto.put("descricao", getMensagem());
        newConto.put("Autor", getIdauthor());
        newConto.put("data",getData());

// Add a new document with a generated ID
        db.collection("Conto")
                .add(newConto)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //  Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public void salvarContoPublico(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("conto");
        anuncioref.child(getUid()).setValue(this);
    }
    public  void AdicioneiConto(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("adicionei-conto");
        anuncioref.child(identificadorUsuario)
                .child(getUid()).setValue(this);

        salvarContoPublico();
    }

    public void remover(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("meusconto")
                .child(identificadorUsuario)
                .child(getUid());

        anuncioref.removeValue();
     removerContocolecao();
     removerContoLike();
        removerContoPublico();
    }

    public void removerContoPublico(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("conto")
                .child(getUid());

        anuncioref.removeValue();

    }
    public void removerContoLike(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("conto-likes")
                .child(getUid());

        anuncioref.removeValue();

    }
    public void removerContocolecao(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("conto-colecao")
                .child(getUid());

        anuncioref.removeValue();

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdauthor() {
        return idauthor;
    }

    public void setIdauthor(String idauthor) {
        this.idauthor = idauthor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getQuantcolecao() {
        return quantcolecao;
    }

    public void setQuantcolecao(int quantcolecao) {
        this.quantcolecao = quantcolecao;
    }
}

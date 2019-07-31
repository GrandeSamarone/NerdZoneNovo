package com.wegeekteste.fulanoeciclano.nerdzone.Model;



import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.StorageReference;
import com.wegeekteste.fulanoeciclano.nerdzone.Config.ConfiguracaoFirebase;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Forum implements Serializable {
    private String id;
    private String idauthor;
    private String nomeauthor;
    private String titulo;
    private String foto;
    private String descricao;
    private @ServerTimestamp Date data;
    private String opcao;
    private String categoria;
    private int likecount = 0;
    private  int quantcomentario=0;
    public Map<String, Boolean> stars = new HashMap<>();
    String usuariologado = UsuarioFirebase.getIdentificadorUsuario();
    public Forum() {

    }

  public  void SalvarForum(){
      // String IdConto= Base64Custom.codificarBase64("danlelis");
      FirebaseFirestore db = FirebaseFirestore.getInstance();
      Map<String, Object> newForum = new HashMap<>();
      newForum.put("id",getId());
      newForum.put("titulo", getTitulo());
      newForum.put("descricao", getDescricao());
      newForum.put("id_autor", getIdauthor());
      newForum.put("foto", getFoto());
      newForum.put("nomeauthor", getNomeauthor());
      newForum.put("opcao", getOpcao());
      newForum.put("categoria", getCategoria());
      newForum.put("data", FieldValue.serverTimestamp());

// Add a new document with a generated ID
      db.collection("WeForum")
              //.document(getIdauthor())
              //.collection(getOpcao())
              //.document("sdsdsdsdsdsdwsd")
              .add(newForum)
             .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                 @Override
                 public void onSuccess(DocumentReference documentReference) {

                 }
             }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {

          }
      });
  }

    public void salvarTopicoPublico(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("topico");
        anuncioref.child(getId()).setValue(this);
    }

    public void remover(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("meustopicos")
                .child(identificadorUsuario)
                .child(getId());

        anuncioref.removeValue();

        removerTopicoPublico();
        deletar_img_topico();
        removerTopicoComentario();
    }

    public void removerTopicoPublico(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("topico")
                .child(getId());

        anuncioref.removeValue();

    }
    public void removerTopicoComentario(){
        DatabaseReference anuncioref = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("comentario-topico")
                .child(getId());

        anuncioref.removeValue();

    }

    public void deletar_img_topico(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage()
                .child("imagens")
                .child("topico")
                .child(identificadorUsuario)
                .child(getId());

        storageReference.delete();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getQuantcomentario() {
        return quantcomentario;
    }

    public void setQuantcomentario(int quantcomentario) {
        this.quantcomentario = quantcomentario;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNomeauthor() {
        return nomeauthor;
    }

    public void setNomeauthor(String nomeauthor) {
        this.nomeauthor = nomeauthor;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
// [END post_class]

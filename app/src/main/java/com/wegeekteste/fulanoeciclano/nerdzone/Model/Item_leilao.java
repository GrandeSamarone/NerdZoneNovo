package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import java.util.List;

public class Item_leilao {

    private String id;
    private String nome;
    private String desc;
    private String preco;
    private String author_id;
    private String author_nome;
    private String author_foto;
    private List<String> foto;
    public Item_leilao(){

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_nome() {
        return author_nome;
    }

    public void setAuthor_nome(String author_nome) {
        this.author_nome = author_nome;
    }

    public String getAuthor_foto() {
        return author_foto;
    }

    public void setAuthor_foto(String author_foto) {
        this.author_foto = author_foto;
    }

    public List<String> getFoto() {
        return foto;
    }

    public void setFoto(List<String> foto) {
        this.foto = foto;
    }
}

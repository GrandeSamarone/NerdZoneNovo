package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import java.util.List;

public class HQ_Model {


    private int   img_id;
    private String   nova_pos;
    private String   antiga_pos;
    private String fotos;

    public HQ_Model(int id,String url){
    img_id=id;
    fotos=url;

    }


    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public String getNova_pos() {
        return nova_pos;
    }

    public void setNova_pos(String nova_pos) {
        this.nova_pos = nova_pos;
    }

    public String getAntiga_pos() {
        return antiga_pos;
    }

    public void setAntiga_pos(String antiga_pos) {
        this.antiga_pos = antiga_pos;
    }

    public String getFotos() {
        return fotos;
    }

    public void setFotos(String fotos) {
        this.fotos = fotos;
    }
}

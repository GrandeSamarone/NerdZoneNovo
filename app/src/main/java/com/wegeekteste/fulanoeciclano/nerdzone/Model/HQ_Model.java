package com.wegeekteste.fulanoeciclano.nerdzone.Model;

public class HQ_Model {


    private String img_name;
    private int   img_id;
    private String   nova_pos;
    private String   antiga_pos;

    public HQ_Model(int id,String name){
    img_id=id;
    img_name=name;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
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
}

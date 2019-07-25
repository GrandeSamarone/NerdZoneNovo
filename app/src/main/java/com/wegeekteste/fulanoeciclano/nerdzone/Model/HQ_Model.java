package com.wegeekteste.fulanoeciclano.nerdzone.Model;

public class HQ_Model {


    private String img_name;
    private int   img_id;


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
}

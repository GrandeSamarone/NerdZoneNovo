package com.wegeekteste.fulanoeciclano.nerdzone.Notificacao;

public class Data {
    //id de quem quer participar do grupo
    private String id_mandou;
    private int icon;
    private String body;
    private String title;
    private String endereco;
    //id do admin do grupo
    private String id_recebe;

    public Data(String id_mandou, int icon, String body, String title, String id_recebe,String end) {
        this.id_mandou = id_mandou;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.id_recebe = id_recebe;
        this.endereco=end;
    }

    public Data() {
    }

    public String getId_mandou() {
        return id_mandou;
    }

    public void setId_mandou(String id_mandou) {
        this.id_mandou = id_mandou;
    }

    public String getId_recebe() {
        return id_recebe;
    }

    public void setId_recebe(String id_recebe) {
        this.id_recebe = id_recebe;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}

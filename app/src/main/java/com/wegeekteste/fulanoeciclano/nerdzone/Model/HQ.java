package com.wegeekteste.fulanoeciclano.nerdzone.Model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HQ {

    private String id;
    private String id_author;
    private ArrayList<String>List_img;


    public HQ(){
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        //String idBefore = db.collection("HQ").document().getId();
        //setId(idBefore);
    }

 public void SalvarHQ() {
     FirebaseFirestore db = FirebaseFirestore.getInstance();
     Map<String, Object> newHQ = new HashMap<>();
     newHQ.put("id", getId());
     newHQ.put("id_author", getId_author());
     newHQ.put("list_img", getList_img());

     db.collection("HQ")
             .document(getId())
             .set(newHQ);
             //.collection(getOpcao())
             //.document("sdsdsdsdsdsdwsd")

 }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getList_img() {
        return List_img;
    }

    public void setList_img(ArrayList<String> list_img) {
        List_img = list_img;
    }

    public String getId_author() {
        return id_author;
    }

    public void setId_author(String id_author) {
        this.id_author = id_author;
    }
}

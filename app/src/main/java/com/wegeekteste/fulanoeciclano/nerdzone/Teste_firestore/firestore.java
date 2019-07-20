package com.wegeekteste.fulanoeciclano.nerdzone.Teste_firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;

import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.util.HashMap;
import java.util.Map;

public class firestore extends AppCompatActivity {

    private Button botao;
    private AppCompatEditText titulo,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        titulo = findViewById(R.id.titulo_firestore);
        desc = findViewById(R.id.desc_firestore);
        botao = findViewById(R.id.botaosalvarfirestore);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> newConto = new HashMap<>();
                newConto.put("titulo", titulo.getText().toString());
                newConto.put("descricao", desc.getText().toString());
                newConto.put("Autor", "Marlos");

// Add a new document with a generated ID
                db.collection("conto")
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
        });
  /*  botao2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Map<String, Object> docData = new HashMap<>();
            docData.put("stringExample", "Hello world!");
            docData.put("booleanExample", true);
            docData.put("numberExample", 3.14159265);
            docData.put("dateExample", new Timestamp(new Date()));
            docData.put("listExample", Arrays.asList(1, 2, 3));
            docData.put("nullExample", null);

            Map<String, Object> nestedData = new HashMap<>();
            nestedData.put("a", 5);
            nestedData.put("b", true);

            docData.put("objectExample", nestedData);

            db.collection("data").document("one")
                    .set(docData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                         //  Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          //  Log.w(TAG, "Error writing document", e);
                        }
                    });

            DocumentReference docRef = db.collection("cities").document("SF");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("sdsd", "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d("aaa", "No such document");
                        }
                    } else {
                        Log.d("dggd", "get failed with ", task.getException());
                    }
                }
            });
        }
    });
*/
    }




}

package com.wegeekteste.fulanoeciclano.nerdzone.Notificacao;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String refreshToken) {
        super.onNewToken(refreshToken);
        Log.d("NEW_TOKEN",refreshToken);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //String refreshToken = FirebaseInstanceId.getInstance().getToken();
        if (firebaseUser != null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken) {
        FirebaseFirestore  db = FirebaseFirestore.getInstance();
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        db.collection("Usuarios").document(identificadorUsuario).update("token",refreshToken);
    }
}

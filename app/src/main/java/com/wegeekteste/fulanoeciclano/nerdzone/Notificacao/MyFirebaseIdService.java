package com.wegeekteste.fulanoeciclano.nerdzone.Notificacao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.UsuarioFirebase;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        if (firebaseUser != null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken) {
        FirebaseFirestore  db = FirebaseFirestore.getInstance();
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        Token token = new Token(refreshToken);
        db.collection("Tokens").document(identificadorUsuario).set(token);
    }
}

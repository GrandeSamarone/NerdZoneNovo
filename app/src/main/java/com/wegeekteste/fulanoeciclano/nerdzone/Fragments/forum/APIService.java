package com.wegeekteste.fulanoeciclano.nerdzone.Fragments.forum;



import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.MyResponse;
import com.wegeekteste.fulanoeciclano.nerdzone.Notificacao.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAMEqj1oY:APA91bG47NzoJ-7JTvnv5aIa1EVRyJXBJ2wOGIGZ2RbrRfIstDsZWK7NX7r6FEQYW8yKCAe9HIWj6mQQkIATgROKGuMHE8Qm74zQ9GEFLQML710Y3FQCFxEGt9g8X7YhDm1fR6RGouqD"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

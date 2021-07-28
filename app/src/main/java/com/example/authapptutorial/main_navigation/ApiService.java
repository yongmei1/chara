package com.example.authapptutorial.main_navigation;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.Call;

public interface ApiService {

    @FormUrlEncoded
    @POST("chat")
    public void chatWithTheBit(
            @Field("chatInput")
                    String chatText);


public void ChatReponse(String chatbotReply);

}
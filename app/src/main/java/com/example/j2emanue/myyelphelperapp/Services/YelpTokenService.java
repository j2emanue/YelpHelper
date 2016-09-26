package com.example.j2emanue.myyelphelperapp.Services;

import com.example.j2emanue.myyelphelperapp.Model.TokenInfo;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by j2emanue on 9/23/16.
 */

public interface YelpTokenService {


    @POST("oauth2/token")
    Call<TokenInfo> getTokenInfo(
            @Query("grant_type") String grantType,
            @Query("client_id") String clientId,
            @Query("client_secret") String secret);

}

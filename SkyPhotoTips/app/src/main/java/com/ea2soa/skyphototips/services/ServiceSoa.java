package com.ea2soa.skyphototips.services;

import com.ea2soa.skyphototips.dto.RequestRegisterUser;
import com.ea2soa.skyphototips.dto.ResponseRegisterUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceSoa {

    @POST("api/register")
    Call<ResponseRegisterUser> respRegisterUser(@Body RequestRegisterUser reqRegisterUser);

}

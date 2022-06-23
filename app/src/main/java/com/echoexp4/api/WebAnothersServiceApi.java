package com.echoexp4.api;

import com.echoexp4.Requests.InvitationRequest;
import com.echoexp4.Requests.TransferRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebAnothersServiceApi {

    @POST("transfer")
    Call<Void> sendTransfer(@Body TransferRequest request);

    @POST("invitations")
    Call<Void> sendInvitations(@Body InvitationRequest request);

}

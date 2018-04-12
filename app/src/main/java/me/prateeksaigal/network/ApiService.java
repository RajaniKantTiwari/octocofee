package me.prateeksaigal.network;


import io.reactivex.Observable;
import me.prateeksaigal.base.BaseResponse;
import me.prateeksaigal.network.request.LoginRequest;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiService {
    @POST("register/user")
    Observable<BaseResponse> getLoginDetail(@Body LoginRequest request);

    @GET("register/logout")
    Observable<BaseResponse> logout();
}

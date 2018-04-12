package me.prateeksaigal.network;



import io.reactivex.Observable;
import me.prateeksaigal.base.BaseResponse;
import me.prateeksaigal.network.request.LoginRequest;


public interface Repository {
    Observable<BaseResponse> getLoginDetail(LoginRequest request);


    Observable<BaseResponse> logout();


}

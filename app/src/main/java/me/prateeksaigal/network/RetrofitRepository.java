package me.prateeksaigal.network;



import io.reactivex.Observable;
import me.prateeksaigal.base.BaseResponse;
import me.prateeksaigal.network.request.LoginRequest;
import retrofit2.Retrofit;


public class RetrofitRepository implements Repository {
    private ApiService apiService;

    public RetrofitRepository(Retrofit retrofit) {
        apiService = retrofit.create(ApiService.class);
    }

    @Override
    public Observable<BaseResponse> getLoginDetail(LoginRequest request) {
        return apiService.getLoginDetail(request);
    }



    @Override
    public Observable<BaseResponse> logout() {
        return apiService.logout();
    }



}

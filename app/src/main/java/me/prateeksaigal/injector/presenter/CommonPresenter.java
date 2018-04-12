package me.prateeksaigal.injector.presenter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;



import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.prateeksaigal.base.BaseResponse;
import me.prateeksaigal.base.MvpView;
import me.prateeksaigal.base.Presenter;
import me.prateeksaigal.network.DefaultApiObserver;
import me.prateeksaigal.network.Repository;
import me.prateeksaigal.network.request.LoginRequest;

/**
 * Created by arvind on 09/11/17.
 */

public class CommonPresenter implements Presenter<MvpView> {

    private final Repository mRepository;
    private MvpView mView;


    public CommonPresenter(Repository repository) {
        this.mRepository = repository;
    }

    @Override
    public void attachView(MvpView view) {
        this.mView = view;
    }

    public void getLoginDetail(Activity activity, LoginRequest loginRequest) {
        mView.showProgress();
        mRepository.getLoginDetail(loginRequest).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DefaultApiObserver<BaseResponse>(activity) {
            @Override
            public void onResponse(BaseResponse response) {
               mView.hideProgress();
                mView.onSuccess(response, 2);
            }

            @Override
            public void onError(Throwable call, BaseResponse baseResponse) {
                mView.hideProgress();
                mView.onError(call, 2);
            }
        });
    }


}

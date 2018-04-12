/*
 * Copyright (c) 2017 Accor. All rights reserved. Developed by Appster.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 *
 */

package me.prateeksaigal.network;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.observers.DefaultObserver;
import me.prateeksaigal.base.BaseActivity;
import me.prateeksaigal.base.BaseResponse;
import me.prateeksaigal.ocotocoffee.R;
import me.prateeksaigal.utils.CommonUtility;
import me.prateeksaigal.utils.ExplicitIntent;
import okhttp3.ResponseBody;


public abstract class DefaultApiObserver<T> extends DefaultObserver<T> {

    private final WeakReference<Activity> ref;
    private final Activity activity;


    public DefaultApiObserver(Activity activity) {
        this.activity = activity;
        ref = new WeakReference<>(activity);
    }

    public abstract void onResponse(T response);

    public abstract void onError(Throwable call, BaseResponse baseResponse);

    @Override

    public void onNext(T value) {
        /*try {*/
        ((BaseActivity) ref.get()).hideProgress();
        if (value == null) {
            ((BaseActivity) ref.get()).showToast(ref.get().getString(R.string.server_error));
        } else if (value instanceof BaseResponse) {
            BaseResponse baseResponse = ((BaseResponse) value);
            if (CommonUtility.isNotNull(baseResponse)) {
                /*if ( CommonUtility.isNotNull(baseResponse.getStatus())&&(baseResponse.getStatus().equalsIgnoreCase(AppConstants.FORBIDDEN)
                ||baseResponse.getStatus().equalsIgnoreCase(AppConstants.UN_AUTHORIZED))) {
                    ExplicitIntent.getsInstance().navigateTo(activity, LoginActivity.class);
                    activity.finish();
                } else {*/
                    onResponse(value);
                /*}*/
            }

        }
       /* } catch (Exception e) {
            LogUtils.LOGE("ApiObserver", e.printStackTrace());
        }*/

    }

    @Override
    public void onError(Throwable e) {
        ((BaseActivity) ref.get()).hideProgress();

        if (e instanceof ConnectException || e instanceof UnknownHostException) {
            if (ref.get() instanceof BaseActivity) {
                ((BaseActivity) ref.get()).showToast(ref.get().getString(R.string.no_internet));
            }
            onError(e, new BaseResponse());
            return;
        }

        if (e instanceof HttpException) {
            BaseResponse errorParser = null;
            ResponseBody body = ((HttpException) e).response().errorBody();
            Gson gson = new Gson();
            TypeAdapter<BaseResponse> adapter = gson.getAdapter
                    (BaseResponse
                            .class);
            try {
                errorParser =
                        adapter.fromJson(body.string());

            } catch (IOException t) {
                e.printStackTrace();
            }
            if (errorParser != null) {
                if (!TextUtils.isEmpty(errorParser.getMsg())) {
                    ((BaseActivity) ref.get()).showToast(errorParser.getMsg());
                }
                onError(e, errorParser);
            } else {
                ((BaseActivity) ref.get()).showToast(ref.get().getString(R.string.server_error));
                BaseResponse response = new BaseResponse();
                response.setMsg(ref.get().getString(R.string.server_error));
                onError(e, response);
            }


        }

    }

    @Override
    public void onComplete() {

    }


}




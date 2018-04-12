package me.prateeksaigal.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 * Created by ashok on 06/11/17.
 */

public abstract class BaseFragment extends Fragment implements MvpView,View.OnClickListener {
    private BaseActivity mActivity;
    public abstract void initializeData();

    public abstract void setListener();

    public abstract String getFragmentName();

    public abstract void attachView();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachView();
        initializeData();
        setListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
        }
    }

    @Override
    public void onError(Throwable message,int requestCode) {
        if (mActivity != null) {
            mActivity.onError(message,requestCode);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void showProgress() {
        if (mActivity != null) {
          mActivity.showProgress();
        }
    }

    @Override
    public void hideProgress() {
        if (mActivity != null) {
            mActivity.hideProgress();
        }
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

}

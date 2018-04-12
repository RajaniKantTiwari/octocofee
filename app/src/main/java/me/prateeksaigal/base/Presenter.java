package me.prateeksaigal.base;


/**
 *
 */
public interface Presenter<T extends MvpView> {
    void attachView(T view);
}

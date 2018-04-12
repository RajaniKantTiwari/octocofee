package me.prateeksaigal.base;


import io.reactivex.Observable;

/**
 *
 */
public interface UseCase<T> {
    Observable<T> execute();
}

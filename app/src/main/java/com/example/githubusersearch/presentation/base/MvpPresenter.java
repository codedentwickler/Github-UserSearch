package com.example.githubusersearch.presentation.base;

/**
 * Created by codedentwickler on 4/6/17.
 */

public interface MvpPresenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();

}

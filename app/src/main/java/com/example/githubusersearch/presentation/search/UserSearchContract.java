package com.example.githubusersearch.presentation.search;

import com.example.githubusersearch.data.remote.model.User;
import com.example.githubusersearch.presentation.base.MvpPresenter;
import com.example.githubusersearch.presentation.base.MvpView;

import java.util.List;

/**
 * Created by codedentwickler on 4/2/17.
 */

interface UserSearchContract {

    interface View extends MvpView{

        void showSearchResult(List<User> users);

        void showErrorMessage(String message);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends MvpPresenter<View>{

        void search(String term);

    }

}

package com.example.githubusersearch.presentation.search;

import com.example.githubusersearch.data.UserRepository;
import com.example.githubusersearch.data.remote.model.User;
import com.example.githubusersearch.presentation.base.BasePresenter;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by codedentwickler on 4/2/17.
 */

public class UserSearchPresenter extends BasePresenter<UserSearchContract.View>
        implements UserSearchContract.Presenter {

    private Scheduler mainScheduler, ioScheduler;

    private UserRepository userRepository;

    public UserSearchPresenter(Scheduler mainScheduler, Scheduler ioScheduler, UserRepository userRepository) {
        this.mainScheduler = mainScheduler;
        this.ioScheduler = ioScheduler;
        this.userRepository = userRepository;
    }

    @Override
    public void search(String term) {
        checkViewAttached();
        getView().showLoading();

        addSubscription(userRepository.searchUsers(term)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideLoading();
                        getView().showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(List<User> users) {
                        getView().hideLoading();
                        getView().showSearchResult(users);
                    }
                }));

    }
}

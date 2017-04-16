package com.example.githubusersearch.data;

import com.example.githubusersearch.data.remote.GithubUserRestService;
import com.example.githubusersearch.data.remote.model.User;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 * Created by codedentwickler on 4/2/17.
 */

public class UserRepositoryImplementation implements UserRepository {

    private GithubUserRestService githubUserRestService;

    public UserRepositoryImplementation(GithubUserRestService githubUserRestService) {
        this.githubUserRestService = githubUserRestService;
    }

    @Override
    public Observable<List<User>> searchUsers(String searchText) {
        return Observable.defer(() -> githubUserRestService.searchGithubUsers(searchText)
                .concatMap(usersList -> Observable.from(usersList.getItems())
                        .concatMap(user -> githubUserRestService.getUser(user.getLogin())).toList()))

                .retryWhen(observable -> observable.flatMap(o -> {
                    if (o instanceof IOException){
                        return Observable.just(null);
                    }
                    return Observable.error(o);
                }));
    }

}
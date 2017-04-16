package com.example.githubusersearch;

import com.example.githubusersearch.data.remote.GithubUserRestService;
import com.example.githubusersearch.data.remote.model.User;
import com.example.githubusersearch.data.remote.model.UsersList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codedentwickler on 4/6/17.
 */

public class MockGithubUserRestServiceImple implements GithubUserRestService {

    private final List<User> usersList = new ArrayList<>();
    private User dummyUser1, dummyUser2;
    private static Observable<UsersList> dummySearchGithubCallResult = null;

    public MockGithubUserRestServiceImple() {

        dummyUser1 = new User("riggaroo", "Rebecca Franks",
                "https://riggaroo.co.za/wp-content/uploads/2016/03/rebeccafranks_circle.png", "Android Dev");
        dummyUser2 = new User("riggaroo2", "Rebecca's Alter Ego",
                        "https://s-media-cache-ak0.pinimg.com/564x/e7/cf/f3/e7cff3be614f68782386bfbeecb304b1.jpg",
                        "A unicorn");
        usersList.add(dummyUser1);
        usersList.add(dummyUser2);

    }

    public static <T> void setDummySearchGithubCallResult(Observable<UsersList> result) {
        dummySearchGithubCallResult = result;
    }


    @Override
    public Observable<UsersList> searchGithubUsers(@Query("q") String searchText) {

        if (dummySearchGithubCallResult != null){
            return dummySearchGithubCallResult;
        }

        UsersList usersList = new UsersList();
        usersList.setItems(this.usersList);
        return Observable.just(usersList);
    }

    @Override
    public Observable<User> getUser(@Path("username") String username) {
        if (username.equals("riggaroo")) {
            return Observable.just(dummyUser1);
        } else if (username.equals("riggaroo2")) {
            return Observable.just(dummyUser2);
        }
        return Observable.just(null);
    }
}


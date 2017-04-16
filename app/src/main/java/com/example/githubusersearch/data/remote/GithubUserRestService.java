package com.example.githubusersearch.data.remote;

import com.example.githubusersearch.data.remote.model.User;
import com.example.githubusersearch.data.remote.model.UsersList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codedentwickler on 4/2/17.
 */

public interface GithubUserRestService {

    @GET("/search/users?per_page=2")
    Observable<UsersList> searchGithubUsers(@Query("q") String searchText);

    @GET("/users/{username}")
    Observable<User> getUser(@Path("username") String username);

}

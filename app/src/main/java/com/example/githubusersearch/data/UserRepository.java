package com.example.githubusersearch.data;

import com.example.githubusersearch.data.remote.model.User;

import java.util.List;

import rx.Observable;

/**
 * Created by codedentwickler on 4/2/17.
 */

public interface UserRepository {

    Observable<List<User>> searchUsers(String searchText);

}

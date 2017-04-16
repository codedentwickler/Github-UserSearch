package com.example.githubusersearch.injection;


import com.example.githubusersearch.MockGithubUserRestServiceImple;
import com.example.githubusersearch.data.UserRepository;
import com.example.githubusersearch.data.UserRepositoryImplementation;
import com.example.githubusersearch.data.remote.GithubUserRestService;

/**
 * Created by codedentwickler on 4/2/17.
 */

public final class Injection {

    private static GithubUserRestService userRestService;

    public static UserRepository provideUserRepo() {
        return new UserRepositoryImplementation(provideGithubUserRestService());
    }

    static GithubUserRestService provideGithubUserRestService() {
        if (userRestService == null) {
            userRestService = new MockGithubUserRestServiceImple();
        }
        return userRestService;
    }

}

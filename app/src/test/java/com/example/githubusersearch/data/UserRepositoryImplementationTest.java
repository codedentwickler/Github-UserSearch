package com.example.githubusersearch.data;

import com.example.githubusersearch.data.remote.GithubUserRestService;
import com.example.githubusersearch.data.remote.model.User;
import com.example.githubusersearch.data.remote.model.UsersList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by codedentwickler on 4/3/17.
 */
public class UserRepositoryImplementationTest {


    private static final String USER_LOGIN_CODEDENTWICKLER = "codedentwickler";
    private static final String USER_LOGIN_2_CODEDENTWICKLER= "kanyinsola";

    @Mock
    GithubUserRestService githubUserRestService;

    private UserRepository userRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        userRepository = new UserRepositoryImplementation(githubUserRestService);
    }

    @Test
    public void searchUsers_200OkResponse_InvokesCorrectApiCalls() {
        //Given
        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(Observable.just(githubUserList()));
        when(githubUserRestService.getUser(anyString())).thenReturn(
                Observable.just(user1FullDetails()), Observable.just(user2FullDetails()));

        // When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        userRepository.searchUsers(USER_LOGIN_CODEDENTWICKLER).subscribe(subscriber);

        // Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<List<User>> onNextEvents = subscriber.getOnNextEvents();
        List<User> users = onNextEvents.get(0);
        Assert.assertEquals(USER_LOGIN_CODEDENTWICKLER, users.get(0).getLogin());
        Assert.assertEquals(USER_LOGIN_2_CODEDENTWICKLER, users.get(1).getLogin());
        verify(githubUserRestService).searchGithubUsers(USER_LOGIN_CODEDENTWICKLER);
        verify(githubUserRestService).getUser(USER_LOGIN_CODEDENTWICKLER);
        verify(githubUserRestService).getUser(USER_LOGIN_2_CODEDENTWICKLER);

    }

    private UsersList githubUserList() {
        User user = new User(USER_LOGIN_CODEDENTWICKLER);

        User user2 = new User(USER_LOGIN_2_CODEDENTWICKLER);

        List<User> githubUsers = new ArrayList<>();
        githubUsers.add(user);
        githubUsers.add(user2);
        UsersList usersList = new UsersList();
        usersList.setItems(githubUsers);
        return usersList;
    }

    private User user1FullDetails() {

        return new User(USER_LOGIN_CODEDENTWICKLER, "Kanyinsola Fapohunda", "avatar_url", "Bio1");
    }

    private User user2FullDetails() {

        return new User(USER_LOGIN_2_CODEDENTWICKLER, "Oyindamola Fapohunda", "avatar_url2", "Bio2");
    }

    @Test
    public void searchUsers_IOExceptionThenSuccess_SearchUsersRetried() {
        //Given
        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(getIOExceptionError(), Observable.just(githubUserList()));
        when(githubUserRestService.getUser(anyString()))
                .thenReturn(Observable.just(user1FullDetails()), Observable.just(user2FullDetails()));

        //When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        userRepository.searchUsers(USER_LOGIN_CODEDENTWICKLER).subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(githubUserRestService, times(2)).searchGithubUsers(USER_LOGIN_CODEDENTWICKLER);
        verify(githubUserRestService).getUser(USER_LOGIN_CODEDENTWICKLER);
        verify(githubUserRestService).getUser(USER_LOGIN_2_CODEDENTWICKLER);
    }
    @Test
    public void searchUsers_GetUserIOExceptionThenSuccess_SearchUsersRetried() {
        //Given
        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(Observable.just(githubUserList()));
        when(githubUserRestService.getUser(anyString()))
                .thenReturn(getIOExceptionError(), Observable.just(user1FullDetails()),
                        Observable.just(user2FullDetails()));

        //When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        userRepository.searchUsers(USER_LOGIN_CODEDENTWICKLER).subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(githubUserRestService, times(2)).searchGithubUsers(USER_LOGIN_CODEDENTWICKLER);
        verify(githubUserRestService, times(2)).getUser(USER_LOGIN_CODEDENTWICKLER);
        verify(githubUserRestService).getUser(USER_LOGIN_2_CODEDENTWICKLER);
    }

    @Test
    public void searchUsers_OtherHttpError_SearchTerminatedWithError() {
        //Given
        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(get403ForbiddenError());

        //When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        userRepository.searchUsers(USER_LOGIN_CODEDENTWICKLER).subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertError(HttpException.class);

        verify(githubUserRestService).searchGithubUsers(USER_LOGIN_CODEDENTWICKLER);
        verify(githubUserRestService, never()).getUser(USER_LOGIN_CODEDENTWICKLER);
        verify(githubUserRestService, never()).getUser(USER_LOGIN_2_CODEDENTWICKLER);
    }

    private Observable<UsersList> get403ForbiddenError() {
        return Observable.error(new HttpException(Response.error(403, ResponseBody.create
                (MediaType.parse("application/json"), "Forbidden"))));
    }

    private Observable getIOExceptionError() {
        return Observable.error(new IOException());
    }



}
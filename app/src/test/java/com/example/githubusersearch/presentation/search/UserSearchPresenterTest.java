package com.example.githubusersearch.presentation.search;

import com.example.githubusersearch.data.UserRepository;
import com.example.githubusersearch.data.remote.model.User;
import com.example.githubusersearch.data.remote.model.UsersList;
import com.example.githubusersearch.presentation.base.BasePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by codedentwickler on 4/6/17.
 */
public class UserSearchPresenterTest {

    private static final String USER_LOGIN_CODEDENTWICKLER = "codedentwickler";
    private static final String USER_LOGIN_2_CODEDENTWICKLER= "kanyinsola";

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserSearchContract.View view;

    private UserSearchPresenter userSearchPresenter;


    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);
        userSearchPresenter = new UserSearchPresenter(
                Schedulers.immediate(), Schedulers.immediate(), userRepository);
        userSearchPresenter.attachView(view);
    }

    @Test
    public void search_ValidSearchTerm_ReturnsResult() {

        UsersList usersList = getDummyUserList();
        //Given
        when(userRepository.searchUsers(anyString()))
                .thenReturn(Observable.just(usersList.getItems()));

        userSearchPresenter.search("codedentwickler");

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view).showSearchResult(usersList.getItems());
        verify(view , never()).showErrorMessage(anyString());
    }

    UsersList getDummyUserList() {
        List<User> githubUsers = new ArrayList<>();
        githubUsers.add(user1FullDetails());
        githubUsers.add(user2FullDetails());
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
    public void search_UserRepositoryError_ErrorMessage() {

        String errorMessage = "No Internet";
        //Given
        when(userRepository.searchUsers(anyString()))
                .thenReturn(Observable.error(new IOException(errorMessage)));

        userSearchPresenter.search("returnError");

        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view,never()).showSearchResult(anyList());
        verify(view).showErrorMessage(errorMessage);
    }

    @Test(expected = BasePresenter.MvpViewNotAttachedException.class)
    public void search_NoViewAttached_ThrowMvpException() {

        userSearchPresenter.detachView();

        userSearchPresenter.search("test");

        verify(view,never()).showLoading();
        verify(view,never()).hideLoading();
        verify(view,never()).showSearchResult(anyList());
        verify(view,never()).showErrorMessage(anyString());

    }

}
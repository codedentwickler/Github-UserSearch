package com.example.githubusersearch.presentation.search;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import com.example.githubusersearch.MockGithubUserRestServiceImple;
import com.example.githubusersearch.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by codedentwickler on 4/9/17.
 */

@RunWith(AndroidJUnit4.class)
public class UserSearchActivityTest {

    @Rule
    public ActivityTestRule<UserSearchActivity> activityTestRule = new ActivityTestRule<>(UserSearchActivity.class);

    @Test
    public void searchActivity_onLaunch_HintTextDisplayed() {

        //Given activity automatically launched
        //When user doesn't interact with the view
        //Then
        onView(withText(R.string.search_for_some_users)).check(matches(isDisplayed()));
    }

    @Test
    public void searchText_ReturnsCorrectlyFromWebService_DisplaysResult() {

        //When
        onView(allOf(withId(R.id.menu_action_search),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        // When using a SearchView, there are two views that match the id menu_search - one
        // that represents the icon, and the other the edit text view. We want to click on the visible one.
        onView(withId(R.id.search_src_text)).perform(typeText("riggaroo"), pressKey(KeyEvent.KEYCODE_ENTER));

        //Then
        onView(withText(R.string.search_for_some_users)).check(matches(not(isDisplayed())));
        onView(withText("riggaroo - Rebecca Franks")).check(matches(isDisplayed()));
        onView(withText("Android Dev")).check(matches(isDisplayed()));
        onView(withText("A unicorn")).check(matches(isDisplayed()));
        onView(withText("riggaroo2 - Rebecca's Alter Ego")).check(matches(isDisplayed()));

    }

    @Test
    public void searchText_ServiceCallFails_DisplayError(){
        String errorMsg = "Server Error";
        MockGithubUserRestServiceImple.setDummySearchGithubCallResult(Observable.error(new Exception(errorMsg)));

        onView(allOf(withId(R.id.menu_action_search),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        // When using a SearchView, there are two views that match the id menu_search - one that
        // represents the icon, and the other the edit text view. We want to click on the visible one.
        onView(withId(R.id.search_src_text)).perform(typeText("riggaroo"), pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withText(errorMsg)).check(matches(isDisplayed()));
    }
}

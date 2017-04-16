package com.example.githubusersearch.presentation.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.githubusersearch.R;
import com.example.githubusersearch.data.remote.model.User;
import com.example.githubusersearch.injection.Injection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserSearchActivity extends AppCompatActivity implements UserSearchContract.View{


    private UsersAdapter usersAdapter;

    private SearchView searchView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view_users)
    RecyclerView recyclerViewUsers;

    @BindView(R.id.text_view_error_msg)
    TextView textViewErrorMessage;


    private UserSearchContract.Presenter userSearchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        userSearchPresenter = new UserSearchPresenter(
                AndroidSchedulers.mainThread(), Schedulers.io(), Injection.provideUserRepo());
        usersAdapter = new UsersAdapter(null,this);
        recyclerViewUsers.setAdapter(usersAdapter);

        userSearchPresenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_user_search,menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.menu_action_search);

        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!searchView.isIconified())
                    searchView.setIconified(true);
                userSearchPresenter.search(query);
                toolbar.setTitle(query);

                searchMenuItem.collapseActionView();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchMenuItem.expandActionView();
        return true;
    }

    @Override
    protected void onDestroy() {
        userSearchPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showSearchResult(List<User> users) {
        recyclerViewUsers.setVisibility(View.VISIBLE);
        textViewErrorMessage.setVisibility(View.GONE);
        usersAdapter.setItems(users);

    }

    @Override
    public void showErrorMessage(String message) {
        recyclerViewUsers.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.VISIBLE);
        textViewErrorMessage.setText(message);
    }

    @Override
    public void showLoading() {
        recyclerViewUsers.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        recyclerViewUsers.setVisibility(View.VISIBLE);
        textViewErrorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

    }
}

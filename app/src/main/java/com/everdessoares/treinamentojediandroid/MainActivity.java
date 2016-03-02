package com.everdessoares.treinamentojediandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.everdessoares.treinamentojediandroid.adapter.PersonAdapter;
import com.everdessoares.treinamentojediandroid.interfaces.OnItemClickListener;
import com.everdessoares.treinamentojediandroid.interfaces.OnItemLongClickListener;
import com.everdessoares.treinamentojediandroid.interfaces.PersonAPI;
import com.everdessoares.treinamentojediandroid.model.bean.Person;
import com.everdessoares.treinamentojediandroid.ui.activities.NewPersonActivity;
import com.everdessoares.treinamentojediandroid.util.Constantes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    PersonAdapter mAdapter;

    List<Person> mPersons;

    ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        configureRecyclerView();

        getData();

        FloatingActionButton fabNewPerson = (FloatingActionButton) findViewById(R.id.fab_new_person);
        fabNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewPersonActivity.class));
            }
        });
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.
                Builder().
                baseUrl(Constantes.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        PersonAPI personAPI = retrofit.create(PersonAPI.class);

        Call<List<Person>> persons = personAPI.list();

        persons.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                mPersons = response.body();

                createAdapter();
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.failure_person_list), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView itemSearch = (SearchView) item.getActionView();
        itemSearch.setOnQueryTextListener(onQueryTextListener);
        itemSearch.setQueryHint(getString(R.string.action_search));

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void createAdapter() {
        mAdapter = new PersonAdapter(mPersons, this);

        mAdapter.setOnItemClickListener(onItemClickListener);
        mAdapter.setOnItemLongClickListener(onItemLongClickListener);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void configureRecyclerView() {
        LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(llManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            filterList(newText);
            return true;
        }
    };

    private void filterList(String newText) {
        int size = mPersons.size();

        for (int i = size - 1; i >= 0; i--) {
            String namePerson = mPersons.get(i).getName();
            if (!namePerson.contains(newText)) {
                mPersons.remove(i);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void OnItemClick(View v, int position) {
            if (mActionMode != null) {
                mAdapter.setItemChecked(position);
                updateTitleBar();
            } else
                Toast.makeText(MainActivity.this, mPersons.get(position).getName() + " Click", Toast.LENGTH_LONG).show();
        }
    };

    private void updateTitleBar() {
        int total = mAdapter.totalItemChecked();
        String selected = getResources().getQuantityString(R.plurals.total_selected, total, total);
        mActionMode.setTitle(selected);
    }

    private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public void OnItemLongClick(View v, int position) {
            if (mActionMode == null)
                openActionMode(position);
        }
    };

    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                mAdapter.delete();
                mActionMode.finish();

                Snackbar.make(mRecyclerView, getString(R.string.message_deleted, mAdapter.totalItemChecked()), Snackbar.LENGTH_LONG)
                        .setAction(R.string.message_undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAdapter.undoDelete();
                            }
                        }).setActionTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                        .show();
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            closeActionMode();
        }
    };

    @Override
    public void onBackPressed() {
        if (mActionMode != null) {
            mActionMode.finish();
            closeActionMode();
        } else
            super.onBackPressed();
    }

    private void openActionMode(int position) {
        mActionMode = startSupportActionMode(callback);
        mAdapter.setItemChecked(position);
        updateTitleBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.secondaryText));
        }
    }

    private void closeActionMode() {
        mActionMode = null;
        mAdapter.clearItemsChecked();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

}

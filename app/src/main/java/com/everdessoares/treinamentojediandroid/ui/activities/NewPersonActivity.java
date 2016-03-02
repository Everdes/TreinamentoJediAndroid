package com.everdessoares.treinamentojediandroid.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.everdessoares.treinamentojediandroid.R;
import com.everdessoares.treinamentojediandroid.interfaces.PersonAPI;
import com.everdessoares.treinamentojediandroid.model.bean.Person;
import com.everdessoares.treinamentojediandroid.util.Constantes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPersonActivity extends AppCompatActivity {

    private Person mPerson;
    private EditText mEdtName;
    private EditText mEdtAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person);

        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtAge = (EditText) findViewById(R.id.edt_age);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle(getString(R.string.title_activity_new_person));
        setSupportActionBar(toolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_person, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            //TODO implementar caso o campo esteja vazio
//        if (requiredFieldsNull())
            getDataFromUI();
            insertNewPerson();
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertNewPerson() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(Constantes.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        PersonAPI personAPI = retrofit.create(PersonAPI.class);

        Call<Person> call = personAPI.insert(mPerson);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                Toast.makeText(NewPersonActivity.this, getString(R.string.person_saved), Toast.LENGTH_LONG).show();

                finish();
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Toast.makeText(NewPersonActivity.this, getString(R.string.person_save_failure), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean requiredFieldsNull() {
        if (mEdtName.getText().toString() == "" || mEdtAge.getText().toString() == "")
            return true;
        else
            return false;
    }

    private void getDataFromUI() {
        if (mPerson == null)
            mPerson = new Person();

        mPerson.setName(mEdtName.getText().toString());
        mPerson.setAge(Integer.valueOf(mEdtAge.getText().toString()));
    }
}

package com.everdessoares.treinamentojediandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.everdessoares.treinamentojediandroid.adapter.PersonAdapter;
import com.everdessoares.treinamentojediandroid.interfaces.OnItemClickListener;
import com.everdessoares.treinamentojediandroid.interfaces.OnItemLongClickListener;
import com.everdessoares.treinamentojediandroid.model.bean.Person;
import com.everdessoares.treinamentojediandroid.util.Constantes;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    PersonAdapter mAdapter;

    ArrayList<Person> mPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        configureRecyclerView();

        createPersons();

        createAdapter();
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

    private void createPersons() {
        mPersons = new ArrayList<>();

        Person padawan = new Person();
        padawan.setId(1);
        padawan.setName("Anakin Skywalker");
        padawan.setKind(Constantes.PADAWAN);
        padawan.setAge(18);

        mPersons.add(padawan);

        Person jedi = new Person();
        jedi.setId(2);
        jedi.setName("Obi-Wan Kenobi");
        jedi.setKind(Constantes.JEDI);
        jedi.setAge(30);

        mPersons.add(jedi);

        Person robot = new Person();
        robot.setId(3);
        robot.setName("C3PO");
        robot.setKind(Constantes.ROBOT);
        robot.setAge(200);

        mPersons.add(robot);

        Person villain = new Person();
        villain.setId(3);
        villain.setName("Darth Vader");
        villain.setKind(Constantes.VILLAIN);
        villain.setAge(50);

        mPersons.add(villain);
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void OnItemClick(View v, int position) {
            Toast.makeText(MainActivity.this, mPersons.get(position).getName() + " Click", Toast.LENGTH_LONG).show();
        }
    };

    private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public void OnItemLongClick(View v, int position) {
            Toast.makeText(MainActivity.this, "Long Click", Toast.LENGTH_LONG).show();

            mAdapter.delete(position);
        }
    };
}

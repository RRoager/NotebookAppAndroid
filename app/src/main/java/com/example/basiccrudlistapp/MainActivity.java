package com.example.basiccrudlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.basiccrudlistapp.adapter.MyAdapter;

public class MainActivity extends AppCompatActivity implements Updatable {

    private ListView listView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repo.init(this);
        setupList();
    }

    private void setupList() {
        listView = findViewById(R.id.myList);
        myAdapter = new MyAdapter(this, Repo.getItems());
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(( adapterView,  view, i,  l) -> {
            Repo.setCurrentNote(i);
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        myAdapter.notifyDataSetChanged();
    }

    public void addNotePressed(View view){
        Repo.createNote("", "");
    }

    @Override
    public void update(Object o) { // bliver kaldt fra Repo
        myAdapter.notifyDataSetChanged();
    }
}
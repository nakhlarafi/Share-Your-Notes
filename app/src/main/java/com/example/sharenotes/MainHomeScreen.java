package com.example.sharenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainHomeScreen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItemsHomeHelper> listItemsHomeHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_screen);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItemsHomeHelpers = new ArrayList<>();

        for (int i = 0;i<10;++i){
            ListItemsHomeHelper listItem = new ListItemsHomeHelper("Heading"+(i+1),
                    "Lorem Ipsum");
            listItemsHomeHelpers.add(listItem);
        }
        adapter = new AdapterForMainHome(listItemsHomeHelpers,this);
        recyclerView.setAdapter(adapter);
    }
}

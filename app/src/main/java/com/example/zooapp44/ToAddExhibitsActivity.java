package com.example.zooapp44;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class ToAddExhibitsActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_add_exhibit);

        ExhibitAdapter adapter = new ExhibitAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.exhibit_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setExhibitListItems(ToAddExhibits.loadJSON(this,"sample_node_info.json"));

    }
}
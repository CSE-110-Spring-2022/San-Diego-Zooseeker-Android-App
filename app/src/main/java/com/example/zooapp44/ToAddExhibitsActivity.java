package com.example.zooapp44;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class ToAddExhibitsActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private ToAddExhibitsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_add_exhibit);
        viewModel = new ViewModelProvider(this)
                .get(ToAddExhibitsViewModel.class);
        ExhibitAdapter adapter = new ExhibitAdapter();
        adapter.setHasStableIds(true);
        adapter.setOnCheckBoxClickedHandler(viewModel::toggleSelected);

        viewModel.getTodoListItems().observe(this, adapter::setExhibitListItems);
        recyclerView = findViewById(R.id.exhibit_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        ToAddDatabase db = ToAddDatabase.getSingleton(this);
//        ToAddExhibitDao toAddExhibitDao = db.toAddExhibitDao();
//        List<ToAddExhibits> exhibitlist = ToAddExhibits.loadJSON(this, "sample_node_info.json");

//        toAddExhibitDao.insertAll(exhibitlist);

    }
    public void onGoBackMainClicked(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
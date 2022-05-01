package com.example.zooapp44;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.concurrent.Future;

public class OpenExhibitListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private ToAddExhibitsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_exhibit_list);

        viewModel = new ViewModelProvider(this)
                .get(ToAddExhibitsViewModel.class);
        OpenExhibitListAdapter adapter = new OpenExhibitListAdapter(new ExhibitRoute());
        adapter.setHasStableIds(true);
//        viewModel.getToaddExhibits().observe(this, adapter::setToaddExhibits);

        recyclerView = findViewById(R.id.route);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    public void onGoBackClicked(View view){
        finish();
    }
}
package com.example.zooapp44;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OpenExhibitListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
//    private ToAddExhibitsViewModel viewModel;
    ExhibitRoute route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_exhibit_list);

        Intent intent = getIntent();
        route = ExhibitRoute.deserialize(intent.getStringExtra("Route"));

//        viewModel = new ViewModelProvider(this)
//                .get(ToAddExhibitsViewModel.class);
        OpenExhibitListAdapter adapter = new OpenExhibitListAdapter(route);
        adapter.setHasStableIds(true);
//        viewModel.getToaddExhibits().observe(this, adapter::setToaddExhibits);

        recyclerView = findViewById(R.id.route);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    public void onHomeClicked(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // finish();
    }

    public void onGetDirectionClicked(View view){
        Intent intent = new Intent(this, GetDirectionActivity.class);
        intent.putExtra("Route", ExhibitRoute.serialize(route));
        startActivity(intent);
    }

    public void onClearClicked(View view) {
        ToAddExhibitDao toAddExhibitDao = ToAddDatabase.getSingleton(this).toAddExhibitDao();
        List<ToAddExhibits> exhibitItems = toAddExhibitDao.getAll();
        for(int i = 0; i < exhibitItems.size(); i ++){
            exhibitItems.get(i).selected = false;
            toAddExhibitDao.update(exhibitItems.get(i));
        }
        setContentView(R.layout.activity_open_exhibit_list);
    }
}
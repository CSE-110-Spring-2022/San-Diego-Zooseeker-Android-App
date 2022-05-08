package com.example.zooapp44;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GetDirectionActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_direction);
        ZooGraph zg = ZooGraph.getSingleton(getApplicationContext());

        ToAddDatabase db = ToAddDatabase.getSingleton(getApplicationContext());
        ToAddExhibitDao dao = db.toAddExhibitDao();
        List<ToAddExhibits> exhibits = dao.getSelected();
        List<String> exhibitIds = new ArrayList<>();
        for (ToAddExhibits toAddExhibits : exhibits) {
            String id = toAddExhibits.id;
            exhibitIds.add(id);
        }
        String start = "entrance_exit_gate";
        ExhibitRoute route = zg.getOptimalPath(start, exhibitIds);
        String direction = ExhibitRoute.serialize(route);
        GetDirectionAdapter adapter = new GetDirectionAdapter(route);
        adapter.setHasStableIds(true);
//      viewModel.getToaddExhibits().observe(this, adapter::setToaddExhibits);

        // recyclerView = findViewById(R.id.instruction);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setAdapter(adapter);
    }

    public void onHomeClicked(View view){
        finish();
    }
}
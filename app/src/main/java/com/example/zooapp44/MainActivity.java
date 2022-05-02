package com.example.zooapp44;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onSearchBarClicked(View view) {
        Intent intent = new Intent(this,ToAddExhibitsActivity.class);

//        Intent intent = new Intent(this,ToAddExhibitsActivity.class);
        startActivity(intent);

//        List<String> exhibits = Arrays.asList("Tiger", "Bear", "Dog", "Elephant");
//        List<String> distance = Arrays.asList("300ft", "500ft", "200ft", "100ft");
//        ExhibitRoute route = new ExhibitRoute(exhibits, distance);
//        Intent intent = new Intent(this, OpenExhibitListActivity.class);
//        intent.putExtra("Route", ExhibitRoute.serialize(route));
//        startActivity(intent);
    }
}
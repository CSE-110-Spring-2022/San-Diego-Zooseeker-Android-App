package com.example.zooapp44;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class ToAddExhibitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_add_exhibits);

        List<ToAddExhibits> toadds=ToAddExhibits.loadJSON(this,"example_exhibits.json");
        Log.d("ToAddExhibits",toadds.toString());
    }
}
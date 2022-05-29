package com.example.zooapp44;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class GetDirectionActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private int current;
    private boolean isBrief = true;
    ExhibitRoute route;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_direction);

        route = ExhibitRoute.deserialize(getIntent().getStringExtra("Route"));
        preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        editor = preferences.edit();

        current = preferences.getInt("current_index", 0);
        current = 0;

        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);
        }

        updateText();

    }

    @SuppressLint("SetTextI18n")
    private void updateText() {
        TextView currentAnimalView = findViewById(R.id.current_animal);

        currentAnimalView.setText(route.getExhibit(current));
        TextView currentDistanceView = findViewById(R.id.current_distance);
        currentDistanceView.setText(route.getDistance(current, false));


        TextView instructionView = findViewById(R.id.route_instruction);
        instructionView.setMovementMethod(new ScrollingMovementMethod());

        if (isBrief) {
            instructionView.setText(route.getBriefInstruction(current));
        } else {
            instructionView.setText(route.getDetailedInstruction(current));
        }
        updateNextAnimalView();
    }


    private void updateNextAnimalView() {
        TextView nextAnimalView = findViewById(R.id.next_animal);
        if(current + 1 == route.getSize())
            nextAnimalView.setText("Entrance gate");
        else if(current + 1 > route.getSize()) {
            nextAnimalView.setText("");
        }
        else
            nextAnimalView.setText(route.getExhibit(current + 1));


        if(current == route.getSize()){
            ImageView img = findViewById(R.id.skip_btn);
            img.setVisibility(View.INVISIBLE);
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateTextBack(){
        TextView currentAnimalView = findViewById(R.id.current_animal);

        TextView nextAnimalView = findViewById(R.id.next_animal);

        currentAnimalView.setText(route.getExhibit(current));
        TextView backDistanceView = findViewById(R.id.current_distance);
        backDistanceView.setText(route.getBackDistance(current, false));

        TextView instructionView = findViewById(R.id.route_instruction);
        instructionView.setText(route.getBackInstruction(current+1));

        if(current + 1 == route.getSize())
            nextAnimalView.setText("Entrance gate");
        else if(current + 1 > route.getSize())
            nextAnimalView.setText("");
        else
            nextAnimalView.setText(route.getExhibit(current + 1));
    }

    public void onHomeClicked(View view){
        super.onBackPressed();
        startActivity(new Intent(GetDirectionActivity.this, MainActivity.class));
        finish();
    }

    public void onNextClicked(View view){
        Button back = findViewById(R.id.back_btn);
        back.setVisibility(View.VISIBLE);

        ImageView img = findViewById(R.id.skip_btn);
        img.setVisibility(View.VISIBLE);

        current++;
        editor.putInt("current_index", current);
        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);

            img.setVisibility(View.INVISIBLE);
        }
        updateText();
        editor.apply();
    }


    public void onStopClicked(View view) {
        editor.clear();
        editor.apply();
        finish();
    }

    public void onSettingClicked(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });



    public void onSkipClicked(View view) {
        // get new distance
        Double new_dist = route.weight.get(current) + route.weight.get(current + 1);
        route.weight.set(current + 1, new_dist);
        route.weight.remove(current);

        // get new exhibit name
        route.exhibits.remove(current);

        updateText();


        // disable buttons when reach the last one
        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);

            ImageView img = findViewById(R.id.skip_btn);
            img.setVisibility(View.INVISIBLE);
        }

    }
  


    public void onBriefClicked(View view){
        isBrief = true;
        updateText();
    }

    public void onDetailClicked(View view){
        isBrief = false;
        updateText();

    public void onBackClicked(View view){
        Button next = findViewById(R.id.next_btn);
        next.setVisibility(View.VISIBLE);

        ImageView img = findViewById(R.id.skip_btn);
        img.setVisibility(View.VISIBLE);

        current --;

        if(current < 0){
            Button button = findViewById(R.id.back_btn);
            button.setVisibility(View.INVISIBLE);

            img.setVisibility(View.INVISIBLE);

            TextView currentAnimalView = findViewById(R.id.current_animal);
            TextView nextAnimalView = findViewById(R.id.next_animal);

            currentAnimalView.setText("Entrance gate");
            TextView backDistanceView = findViewById(R.id.current_distance);
            backDistanceView.setText(route.getBackDistance(current, false));

            TextView instructionView = findViewById(R.id.route_instruction);
            instructionView.setText(route.getBackInstruction(current+1));

            nextAnimalView.setText(route.getExhibit(current + 1));

        } else {
            updateTextBack();
        }
    }

    

    public void onStopClicked(View view) {
        editor.clear();
        editor.apply();
        finish();
    }
}
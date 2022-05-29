package com.example.zooapp44;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class GetDirectionActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private int current;
    ExhibitRoute route;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button replan_btn, cancel_btn;

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
        instructionView.setText(route.getInstruction(current));

        updateNextAnimalView();
    }

    private void updateNextAnimalView() {
        TextView nextAnimalView = findViewById(R.id.next_animal);
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
        current++;
        editor.putInt("current_index", current);
        if(current == route.getSize()){
            Button button = findViewById(R.id.next_btn);
            button.setVisibility(View.INVISIBLE);
        }
        updateText();
        editor.apply();
    }

    public void onStopClicked(View view) {
        editor.clear();
        editor.apply();
        finish();
    }



    // belows are functions handling the reroute popup window


    public void createReplanMessage(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View replanView = inflater.inflate(R.layout.replan_popup, null);
        boolean focusable = true;

        float density = GetDirectionActivity.this.getResources().getDisplayMetrics().density;
        final PopupWindow replanPopupView = new PopupWindow(replanView, (int)density*400, (int)density*300, focusable);
        replanPopupView.showAtLocation(view, Gravity.CENTER, 0, 0);

        cancel_btn = (Button) replanView.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            // onClick method for cancel button
            @Override
            public void onClick(View view) {
                replanPopupView.dismiss();
            }
        });

        replan_btn = (Button) replanView.findViewById(R.id.replan_btn);
        replan_btn.setOnClickListener(new View.OnClickListener() {
            // onClick method for replan button
            @Override
            public void onClick(View view){
                // get current location and replan new route
            }
        });

    }


    public void onOffClick(View view) {
        createReplanMessage(view);
    }
}
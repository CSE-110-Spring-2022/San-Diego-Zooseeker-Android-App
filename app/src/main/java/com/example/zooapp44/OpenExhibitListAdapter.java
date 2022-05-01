package com.example.zooapp44;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class OpenExhibitListAdapter extends RecyclerView.Adapter<OpenExhibitListAdapter.ViewHolder> {
    private List<ToAddExhibits> toaddExhibits = Collections.emptyList();
    private BiConsumer<ToAddExhibits, String> onTextEditedHandler;

    public void setToaddExhibits(List<ToAddExhibits> newToaddExhibits){
        this.toaddExhibits.clear();
        this.toaddExhibits = newToaddExhibits;
        notifyDataSetChanged();
    }

    public void setOnTextEditedHandler(BiConsumer<ToAddExhibits, String> onTextEdited){
        this.onTextEditedHandler = onTextEdited;
    }

    @NonNull
    @Override
    public OpenExhibitListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exhibit_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenExhibitListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView TVAnimalName;
        private final TextView TVDistance;
        private ToAddExhibits toaddExhibits;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.TVAnimalName = itemView.findViewById(R.id.animal_name);
            this.TVDistance = itemView.findViewById(R.id.distance);

            this.TVAnimalName.setOnFocusChangeListener((view, hasFocus) -> {
                if(!hasFocus){
                    onTextEditedHandler.accept(toaddExhibits, TVAnimalName.getText().toString());
                }
            });

            this.TVDistance.setOnFocusChangeListener((view, hasFocus) -> {
                if(!hasFocus){
                    onTextEditedHandler.accept(toaddExhibits, TVDistance.getText().toString());
                }
            });
        }

        public ToAddExhibits getToaddExhibits(){ return toaddExhibits; }

        public void setToaddExhibits(ToAddExhibits toaddExhibits){
            this.toaddExhibits = toaddExhibits;
            this.TVAnimalName.setText(toaddExhibits.id);
            // Please notice that distance does exist in current list
            this.TVDistance.setText(toaddExhibits.distance);
        }
    }
}

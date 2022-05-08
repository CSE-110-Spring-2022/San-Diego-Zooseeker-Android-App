package com.example.zooapp44;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ExhibitAdapter extends RecyclerView.Adapter<ExhibitAdapter.ViewHolder> {
    private List<ToAddExhibits> exhibits= Collections.emptyList();
    private Consumer<ToAddExhibits> onCheckBoxClicked;
    private BiConsumer<ToAddExhibits, String> onTextEditedHandler ;
    public void setExhibitListItems(List<ToAddExhibits> newExhibits){
        this.exhibits.clear();
        this.exhibits=newExhibits;
        notifyDataSetChanged();
    }
    public void setOnCheckBoxClickedHandler(Consumer<ToAddExhibits> onCheckBoxClicked) {
        this.onCheckBoxClicked = onCheckBoxClicked;
    }

    public void setOnTextEditedHandler(Consumer<ToAddExhibits> onCheckBoxClicked) {
        this.onTextEditedHandler = onTextEditedHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_exhibits,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setExhibit(exhibits.get(position));
    }

    @Override
    public int getItemCount() {
        System.out.println(exhibits.size());
        return exhibits.size();
    }


    public String getItemType(int position){
        return exhibits.get(position).kind;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private ToAddExhibits toAdd;
        //private final  editText;
        private CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView=itemView.findViewById(R.id.exhibits);
            this.checkBox = itemView.findViewById(R.id.chosen);
            this.checkBox.setOnClickListener(view -> {
                if (onCheckBoxClicked == null) return;
                onCheckBoxClicked.accept(toAdd);
            });
            this.textView.setOnFocusChangeListener((view, hasFocus) ->{
                        if (!hasFocus) {
                            onTextEditedHandler.accept(toAdd, textView.getText().toString());
                        }
                    }

            );
            this.checkBox.setOnClickListener(view -> {
                if (onCheckBoxClicked == null) return;
                onCheckBoxClicked.accept(toAdd);
            });
        }
        public ToAddExhibits getExhibits()
        {return toAdd;}

        public void setExhibit(ToAddExhibits toAdd){
            this.toAdd=toAdd;
            this.textView.setText(toAdd.id);
            this.checkBox.setChecked(toAdd.selected);
        }
    }

}

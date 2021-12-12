package com.example.formsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formsystem.MainActivity;
import com.example.formsystem.R;
import com.example.formsystem.model.Activity;

import java.util.ArrayList;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Activity> activityArrayList;

    public ActivitiesAdapter(Context context) {
        this.context = context;
    }

    public void setActivityArrayList(ArrayList<Activity> activityArrayList) {
        this.activityArrayList = activityArrayList;
    }

    @NonNull
    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesAdapter.ViewHolder holder, int position) {
        Activity activity = activityArrayList.get(position);

    }

    @Override
    public int getItemCount() {
        return activityArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewActivityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewActivityName = itemView.findViewById(R.id.textViewActivityName);
            //int adapterPosition = getAdapterPosition();
        }
    }
}
package com.example.formsystem.adapter;

import android.annotation.SuppressLint;
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

    public void setList(ArrayList<Activity> activityArrayList) {
        this.activityArrayList = activityArrayList;
    }

    @NonNull
    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ActivitiesAdapter.ViewHolder holder, int position) {
        Activity activity = activityArrayList.get(position);
        holder.textViewActivityName.setText(activity.getName());
        holder.textViewActivityDesc.setText(activity.getDescription());
        if (activity.getType().equals("0"))
            holder.textViewActivityType.setText("Form");
    }

    @Override
    public int getItemCount() {
        return activityArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewActivityName, textViewActivityDesc, textViewActivityType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewActivityName = itemView.findViewById(R.id.textViewActivityName);
            textViewActivityDesc = itemView.findViewById(R.id.textViewActivityDescription);
            textViewActivityType = itemView.findViewById(R.id.textViewActivityType);
            //int adapterPosition = getAdapterPosition();
        }
    }
}
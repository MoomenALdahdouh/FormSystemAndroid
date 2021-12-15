package com.example.formsystem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formsystem.R;
import com.example.formsystem.model.Activity;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.Interview;
import com.example.formsystem.ui.ViewActivitiesActivity;
import com.example.formsystem.ui.ViewInterviewActivity;

import java.util.ArrayList;

public class InterviewsAdapter extends RecyclerView.Adapter<InterviewsAdapter.ViewHolder> {
    public static final String INTERVIEW_ID = "INTERVIEW_ID";
    public static final String FORM_ID = "FORM_ID";
    public static final String INTERVIEW_TITLE = "INTERVIEW_TITLE";
    public static final String INTERVIEW_LOCATION = "INTERVIEW_LOCATION";
    public static final String INTERVIEW_LATITUDE = "INTERVIEW_LATITUDE";
    public static final String INTERVIEW_LONGITUDE = "INTERVIEW_LONGITUDE";
    private Context context;
    private ArrayList<Interview> interviewsArrayList;
    private Form form;

    public InterviewsAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Interview> interviewsArrayList) {
        this.interviewsArrayList = interviewsArrayList;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    @NonNull
    @Override
    public InterviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.interview_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InterviewsAdapter.ViewHolder holder, int position) {
        try {
            Interview interview = interviewsArrayList.get(position);
            holder.textViewInterviewTitle.setText(interview.getTitle());
            holder.textViewInterviewCreatedAt.setText(interview.getCreated_at());
        } catch (Exception exception) {

        }
    }

    @Override
    public int getItemCount() {
        return interviewsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewInterviewTitle, textViewInterviewCreatedAt;
        ConstraintLayout constraintLayoutInterview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInterviewTitle = itemView.findViewById(R.id.textViewInterviewTitle);
            textViewInterviewCreatedAt = itemView.findViewById(R.id.textViewInterviewCreatedAt);
            constraintLayoutInterview = itemView.findViewById(R.id.interviewItemId);
            constraintLayoutInterview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = getAdapterPosition();
                    Interview interviewClicked = interviewsArrayList.get(adapterPosition);
                    String interviewId = interviewClicked.getId();
                    String interviewTitle = interviewClicked.getTitle();
                    String interviewLocation = interviewClicked.getId();
                    String interviewLatitude = interviewClicked.getLatitude();
                    String interviewLongitude = interviewClicked.getLongitude();
                    Intent intent = new Intent(context, ViewInterviewActivity.class);
                    intent.putExtra(INTERVIEW_ID, interviewId);
                    intent.putExtra(INTERVIEW_TITLE, interviewTitle);
                    intent.putExtra(INTERVIEW_LOCATION, interviewLocation);
                    intent.putExtra(INTERVIEW_LATITUDE, interviewLatitude);
                    intent.putExtra(INTERVIEW_LONGITUDE, interviewLongitude);
                    intent.putExtra(FORM_ID, form.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
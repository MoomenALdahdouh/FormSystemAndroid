package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.formsystem.R;
import com.example.formsystem.adapter.InterviewsAdapter;
import com.example.formsystem.adapter.QuestionsAdapter;
import com.example.formsystem.databinding.ActivityViewImageBinding;
import com.example.formsystem.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    private ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(QuestionsAdapter.IMAGE_NAME)) {
            String imageName = intent.getStringExtra(QuestionsAdapter.IMAGE_NAME);
            Picasso.get()
                    .load(Constants.IMAGES_STORAGE + imageName)//http://camranger.com/wp-content/uploads/2014/10/Android-Icon.png
                    .into(binding.imageView8, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            binding.loadingDataConstraint.setVisibility(View.GONE);
                            binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            binding.loadingDataConstraint.setVisibility(View.GONE);
                            binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                        }

                    });
        }
    }
}
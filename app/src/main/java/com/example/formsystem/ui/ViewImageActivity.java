package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.formsystem.R;
import com.example.formsystem.adapter.InterviewsAdapter;
import com.example.formsystem.adapter.QuestionsAdapter;
import com.example.formsystem.databinding.ActivityViewImageBinding;
import com.example.formsystem.utils.Constants;
import com.example.formsystem.utils.PreferenceUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    private ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(QuestionsAdapter.IMAGE_NAME)) {
            //String imageName = intent.getStringExtra(QuestionsAdapter.IMAGE_NAME);
            String imageName = PreferenceUtils.getImage(ViewImageActivity.this);
            if (isNetworkAvailable()) {
                loadImage(imageName);
            } else {
                if (imageName.length() > 30) {
                    Log.d("imageName222", imageName);
                    byte[] decodedString = Base64.decode(imageName, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    binding.imageView8.setImageBitmap(decodedByte);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    loadImage(imageName);
                }
            }
        }
    }

    private void loadImage(String image) {
        Picasso.get()
                .load(Constants.IMAGES_STORAGE + image)//http://camranger.com/wp-content/uploads/2014/10/Android-Icon.png
                //.networkPolicy(NetworkPolicy.OFFLINE)//use this for offline support
                .into(binding.imageView8, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        //binding.loadingDataConstraint.setVisibility(View.GONE);
                        //binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        //binding.loadingDataConstraint.setVisibility(View.GONE);
                        //binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                    }

                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

package com.pucmm.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShimmerTextView shimmerTextView = findViewById(R.id.tvLoading);
        Shimmer shimmer = new Shimmer()
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setDuration(5000)
                .setStartDelay(1000);
        shimmer.start(shimmerTextView);
    }
}
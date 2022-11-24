package com.pucmm.e_commerce;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.pucmm.e_commerce.databinding.ActivityChargeBinding;
import com.romainpiel.shimmer.Shimmer;

public class ChargeActivity extends AppCompatActivity {

    ActivityChargeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChargeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Shimmer shimmer = new Shimmer()
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setDuration(5000)
                .setStartDelay(1000);
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
        shimmer.start(binding.tvLoading);

        int time = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ChargeActivity.this,LoginActivity.class));
                finish();
            }
        },time);
    }
}
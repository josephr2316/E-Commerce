package com.pucmm.e_commerce.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pucmm.e_commerce.R;

import com.pucmm.e_commerce.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {



    FragmentHomeBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = binding.inflate(inflater, container, false);
        View viewHome = binding.getRoot();
        return viewHome;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
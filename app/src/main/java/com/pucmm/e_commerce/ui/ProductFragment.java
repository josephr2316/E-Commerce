package com.pucmm.e_commerce.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.databinding.FragmentHomeBinding;
import com.pucmm.e_commerce.databinding.FragmentProductBinding;


public class ProductFragment extends Fragment {

    FragmentProductBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(inflater, container, false);
        View viewProduct = binding.getRoot();
        return viewProduct;
    }
}
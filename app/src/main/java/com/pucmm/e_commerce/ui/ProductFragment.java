package com.pucmm.e_commerce.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;
import com.pucmm.e_commerce.databinding.FragmentCategoryBinding;
import com.pucmm.e_commerce.databinding.FragmentHomeBinding;
import com.pucmm.e_commerce.databinding.FragmentProductBinding;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {

    FragmentProductBinding binding;
    private List<Product> list = new ArrayList<>();


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int spanCount = 1;
        binding.recyclerView.setHasFixedSize(true);


        super.onViewCreated(view, savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 2;
        }
        binding.recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), spanCount));
        binding.recyclerView.setAdapter(new ProductAdapter(list));
    }
}
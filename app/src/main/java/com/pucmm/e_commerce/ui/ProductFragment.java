package com.pucmm.e_commerce.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;
import com.pucmm.e_commerce.databinding.FragmentCategoryBinding;
import com.pucmm.e_commerce.databinding.FragmentHomeBinding;
import com.pucmm.e_commerce.databinding.FragmentProductBinding;
import com.pucmm.e_commerce.models.CategoryViewModel;
import com.pucmm.e_commerce.models.ProductViewModel;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {

    FragmentProductBinding binding;
    private List<Product> list = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    public ProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private boolean isAdmin;


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

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                MenuItem searchItem = menu.findItem(R.id.search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filter(newText);

                        return false;
                    }
                });

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        return viewProduct;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        isAdmin = bundle.getBoolean("admin");
        int spanCount = 1;
        binding.recyclerView.setHasFixedSize(true);
        super.onViewCreated(view, savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 2;
        }
        binding.recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), spanCount));
        productAdapter = new ProductAdapter();
        productAdapter.checkAdmin(isAdmin);

        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.init();
        productViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                Log.i("VALOR", String.valueOf(products.size()));
                productAdapter.setProductList(products);
                for (Product product : products)
                    list.add(product);
                binding.recyclerView.setAdapter(productAdapter);


            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    private void filter(String text){
        List<Product> filterProduct = new ArrayList<>();

        for(Product product : list){
            if (product.getDescripcion().toLowerCase().contains(text.toLowerCase())) {
                filterProduct.add(product);
            }
        }
        if (filterProduct.isEmpty()){
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }
        else
            productAdapter.filterList(filterProduct);


    }
}
package com.pucmm.e_commerce.ui;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;
import com.pucmm.e_commerce.database.ProductImage;
import com.pucmm.e_commerce.databinding.FragmentCategoryBinding;
import com.pucmm.e_commerce.databinding.FragmentRegisterProductBinding;
import com.pucmm.e_commerce.models.CategoryViewModel;
import com.pucmm.e_commerce.models.ProductViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RegisterProductFragment extends Fragment {


    private FragmentRegisterProductBinding binding;
    private CategoryViewModel categoryViewModel;
    private ProductViewModel productViewModel;


    //Store image uris in a array list
    private ArrayList<Uri> uriArrayList;

    private ArrayList<Bitmap> bitmapArrayList;

    //Store name's categories in a array list
    List<String> nameCategory;
    List<Category> categoryList;

    List<Product> productList;

    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = binding.inflate(inflater, container, false);
        View viewRegisterProduct = binding.getRoot();
        return viewRegisterProduct;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //init list
        nameCategory = new ArrayList<>();
        uriArrayList = new ArrayList<>();
        productList = new ArrayList<>();
        categoryList = new ArrayList<>();

        //init the category's viewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        //binding.imageSwitcher.setImageDrawable(Drawable.createFromPath("drawable/avatar"));

        categoryViewModel.init();
        productViewModel.init();
        categoryViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {

                for(Category category : categories){
                    nameCategory.add(category.getNombre());
                    categoryList.add(category);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,nameCategory);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                binding.categorySpinner.setAdapter(arrayAdapter);
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                uriArrayList.clear();
                if (result.getResultCode() == RESULT_OK && result.getData().getClipData() != null) {
                    int counter = result.getData().getClipData().getItemCount();
                    for (int i = 0; i < counter; i++){
                        Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                        uriArrayList.add(uri);
                    }
                    binding.imageSwitcher.setImageURI(uriArrayList.get(0));
                }
                else if (result.getData() != null){
                    Uri uri = result.getData().getData();
                    uriArrayList.add(uri);
                    binding.imageSwitcher.setImageURI(uriArrayList.get(0));
                }
            }
        });

        binding.imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                return imageView;
            }
        });

        //click handle, pick a bundle of images
        binding.imageSwitcher.setOnClickListener(viewImage -> {
            pickImages();
        });

        binding.addButton.setOnClickListener(viewButton->{
            String nameCategory = binding.categorySpinner.getSelectedItem().toString();
            String description = binding.descriptionEditText.getText().toString();
            String price = binding.priceEditText.getText().toString();
            ArrayList<String> productImages = new ArrayList<>();
            String id;
            Product product = new Product(description,price);

           for (Uri uri : uriArrayList){
               ImageView imageView = new ImageView(viewButton.getContext());
               imageView.setImageURI(uri);
               BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
               Bitmap bitmap = drawable.getBitmap();
               id = UUID.randomUUID().toString();
               productImages.add(id);
               String path = "products/" + id;
               productViewModel.guardarImagen(path,bitmap);
            }
            product.setProductImages(productImages);
            for (Category category : categoryList){
                if (category.getNombre().equals(nameCategory))
                    nameCategory = category.getId();
            }
            productViewModel.addProduct(product,nameCategory);
            Toast.makeText(getContext(), "Producto agregado", Toast.LENGTH_SHORT).show();

        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    private void pickImages(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Images"));






    }

}
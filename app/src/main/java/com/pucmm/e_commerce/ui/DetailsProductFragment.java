package com.pucmm.e_commerce.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.CarritoCompras;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;
import com.pucmm.e_commerce.databinding.FragmentDetailsProductBinding;
import com.pucmm.e_commerce.databinding.FragmentHomeBinding;
import com.pucmm.e_commerce.databinding.FragmentRegisterCategoryBinding;
import com.pucmm.e_commerce.repositories.FirebaseRepository;
import com.pucmm.e_commerce.repositories.LocalRepository;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "product";

    // TODO: Rename and change types of parameters
    private Product mParam1;

    private static final int CAMERA_PERMISSION = 0;
    private static final int GALLERY_PERMISSION = 1;

    private FragmentDetailsProductBinding binding;
    private FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private LocalRepository localRepository = LocalRepository.getInstance();


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RegisterCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    @NotNull
    public static DetailsProductFragment newInstance(Product param1) {
        DetailsProductFragment fragment = new DetailsProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Product) getArguments().getSerializable(ARG_PARAM1);
        }
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
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.descriptionTextView.setText(mParam1.getDescripcion());
        downloadAndSetImage(mParam1.getFirstImage(), binding.imageView);
        binding.priceTextView.setText(mParam1.getPrecio());
        binding.textView3.setText("1");
        binding.decreaseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(binding.textView3.getText().toString())>1){
                    binding.textView3.setText(String.valueOf(Integer.parseInt(binding.textView3.getText().toString())-1));
                }
            }
        });
        binding.increaseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.textView3.setText(String.valueOf(Integer.parseInt(binding.textView3.getText().toString())+1));
            }
        });
        binding.addcartBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarritoCompras carritoCompras =  localRepository.obtenerCarrito(getContext().getSharedPreferences("carrito", getContext().MODE_WORLD_WRITEABLE));
                carritoCompras.setProductID(mParam1.getCodigo(), Integer.valueOf(binding.textView3.getText().toString()));
                try {
                    localRepository.guardarCarrito(getContext().getSharedPreferences("carrito", getContext().MODE_APPEND), carritoCompras);
                } catch (JsonProcessingException e) {
                    Toast.makeText(getContext(), "Llevatelo que ya exploto", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), carritoCompras.getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void downloadAndSetImage(String nombreImagen, ImageView imagen){
        StorageReference reference = firebaseStorage.getReference().child("images/products/"+nombreImagen);
        final long ONE_MEGABYTE = 1024 * 1024;

        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imagen.setImageBitmap(Bitmap.createScaledBitmap(bmp, imagen.getWidth(), imagen.getHeight(), false));
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
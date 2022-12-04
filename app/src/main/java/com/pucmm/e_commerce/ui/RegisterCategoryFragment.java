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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.databinding.FragmentHomeBinding;
import com.pucmm.e_commerce.databinding.FragmentRegisterCategoryBinding;
import com.pucmm.e_commerce.repositories.FirebaseRepository;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "category";

    // TODO: Rename and change types of parameters
    private Category mParam1;

    private static final int CAMERA_PERMISSION = 0;
    private static final int GALLERY_PERMISSION = 1;

    private FragmentRegisterCategoryBinding binding;
    private FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RegisterCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    @NotNull
    public static RegisterCategoryFragment newInstance(Category param1) {
        RegisterCategoryFragment fragment = new RegisterCategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Category) getArguments().getSerializable(ARG_PARAM1);
        }
    }
    private void dialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("Choose your image profile")
                .setItems(R.array.image_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == CAMERA_PERMISSION && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,0);
                        }if((which==GALLERY_PERMISSION && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent,1);
                        }if((which == CAMERA_PERMISSION && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)){
                            Toast.makeText(getContext(), "Acceso denegado, no tiene permiso de camara", Toast.LENGTH_SHORT).show();
                        }
                        if((which==GALLERY_PERMISSION && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)){
                            Toast.makeText(getContext(), "Acceso denegado, no tiene permiso a la galeria", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder1.show();
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
        if(mParam1==null){
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Category category = new Category();
                    category.setNombre(binding.textEdit.getText().toString());
                    BitmapDrawable drawable = (BitmapDrawable) binding.imageView4.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    category.generarImagen();
                    firebaseRepository.addCategory(category);
                    Log.i("Se subio", "Se subio la categoria");
                    firebaseRepository.guardarImagen(category.getImagen(), bitmap);
                    Toast.makeText(getContext(), "Categoria creada", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            binding.textEdit.setText(mParam1.getNombre());
            downloadAndSetImage(mParam1.getImagen(), binding.imageView4);
            binding.textView4.setText("Modificar Categoria");
            binding.button4.setText("Actualizar");
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParam1.setNombre(binding.textEdit.getText().toString());
                    BitmapDrawable drawable = (BitmapDrawable) binding.imageView4.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    firebaseRepository.updateCategory(mParam1);
                    Log.i("Se subio", "Se subio la categoria");
                    firebaseRepository.guardarImagen(mParam1.getImagen(), bitmap);
                    Toast.makeText(getContext(), "Categoria Actualizada", Toast.LENGTH_SHORT).show();
                }
            });
        }
        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> listPermission  = new ArrayList<String>();
                if((ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) ) {
                    listPermission.add(Manifest.permission.CAMERA);
                } if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_MEDIA_LOCATION) ==PackageManager.PERMISSION_DENIED) {
                    listPermission.add(Manifest.permission.ACCESS_MEDIA_LOCATION);
                } if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_DENIED) {
                    listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                } if(listPermission.size()>0){
                    requestPermissions(listPermission.toArray(new String[0]),1);
                }else{
                    dialog();
                }
            }
        });
    }
    public void downloadAndSetImage(String nombreImagen, ImageView imagen){
        StorageReference reference = firebaseStorage.getReference().child("images/"+nombreImagen);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dialog();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    Bitmap imageCamera = (Bitmap) data.getExtras().get("data");
                    binding.imageView4.setImageBitmap(imageCamera);
                    break;
                }
                else{
                    return;
                }
            case GALLERY_PERMISSION:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedUri = data.getData();
                    binding.imageView4.setImageURI(selectedUri);
                    break;
                }
                else {
                    return;
                }
        }
    }
    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
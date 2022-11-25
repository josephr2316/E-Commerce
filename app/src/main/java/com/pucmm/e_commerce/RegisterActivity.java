package com.pucmm.e_commerce;



import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pucmm.e_commerce.databinding.ActivityRegisterBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private User newUser;
    private AlertDialog.Builder builder;
    boolean isAdmin;
    private String userUID;
    private static final int CAMERA_PERMISSION = 0;
    private static final int GALLERY_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View registerView = binding.getRoot();
        setContentView(registerView);


        TextWatcher();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            userUID = firebaseAuth.getCurrentUser().getUid();
            checkUserAccessLevel(userUID);
        }
        else userUID = null;

        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> listPermission  = new ArrayList<String>();
                if((ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) ==PackageManager.PERMISSION_DENIED) ) {
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

        binding.registerBt.setOnClickListener(view ->{

            newUser = new User();
            newUser.setName(Objects.requireNonNull(binding.nameEdt.getText()).toString());
            newUser.setUser(Objects.requireNonNull(binding.userEdt.getText()).toString());
            newUser.setEmail(Objects.requireNonNull(binding.emailEdit.getText()).toString());
            newUser.setPassword(Objects.requireNonNull(binding.passwordEdt.getText()).toString());
            newUser.setTelephoneNumber(Objects.requireNonNull(binding.phoneEdt.getText()).toString());
            newUser.setAdmin(false);

            if (newUser.getPassword().equals(binding.confirmPasswordEdt.getText().toString())){
                if (userUID!=null && isAdmin)
                {
                    builder = new AlertDialog.Builder(this);
                    builder.setMessage("The user is an admin?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                newUser.setAdmin(true);
                                createUser();
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                newUser.setAdmin(false);
                                createUser();
                                dialog.cancel();
                            })
                            .show();
                }
                else{
                    createUser();
                }
            }
            else {
                Toast.makeText(this, "Both password have to match", Toast.LENGTH_SHORT).show();
            }
        });

        binding.forgetPasswordTv.setOnClickListener(view -> {
            Intent intent = new Intent(this,ForgetPasswordActivity.class);
            startActivity(intent);
        });
        binding.loginNowTv.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dialog();
    }
    private void createUser(){
        firebaseAuth.createUserWithEmailAndPassword(binding.emailEdit.getText().toString(),binding.passwordEdt.getText().toString()).addOnSuccessListener(authResult -> {
            Toast.makeText(RegisterActivity.this,"Account created", Toast.LENGTH_SHORT).show();
            FirebaseUser user = firebaseAuth.getCurrentUser();

            assert user != null;
            DocumentReference df = firebaseFirestore.collection("Users").document(user.getUid());
            //Map<String,Object> name = new HashMap<>();

            df.set(newUser);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this,"Failed to Create Account", Toast.LENGTH_SHORT).show());

    }
    private void dialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Choose your image profile")
                .setItems(R.array.image_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == CAMERA_PERMISSION && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,0);
                        }if((which==GALLERY_PERMISSION && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent,1);
                        }if((which == CAMERA_PERMISSION && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)){
                            Toast.makeText(RegisterActivity.this, "Acceso denegado, no tiene permiso de camara", Toast.LENGTH_SHORT).show();
                        }
                        if((which==GALLERY_PERMISSION && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)){
                            Toast.makeText(RegisterActivity.this, "Acceso denegado, no tiene permiso a la galeria", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder1.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    Bitmap imageCamera = (Bitmap) data.getExtras().get("data");
                    binding.imageView2.setImageBitmap(imageCamera);
                    break;
                }
                else{
                    return;
                }
            case GALLERY_PERMISSION:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedUri = data.getData();
                    binding.imageView2.setImageURI(selectedUri);
                    break;
                }
                else {
                    return;
                }
        }
    }
    private void TextWatcher(){
        binding.nameEdt.addTextChangedListener(textWatcher);
        binding.userEdt.addTextChangedListener(textWatcher);
        binding.emailEdit.addTextChangedListener(textWatcher);
        binding.passwordEdt.addTextChangedListener(textWatcher);
        binding.confirmPasswordEdt.addTextChangedListener(textWatcher);
        binding.phoneEdt.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            binding.registerBt.setEnabled(!binding.nameEdt.getText().toString().isEmpty()
                            && !binding.userEdt.getText().toString().isEmpty()
                            && !binding.emailEdit.getText().toString().isEmpty()
                            && !binding.passwordEdt.getText().toString().isEmpty()
                            && !binding.confirmPasswordEdt.getText().toString().isEmpty()
                            && !binding.phoneEdt.getText().toString().isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    void checkUserAccessLevel(String uid){


        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        int count;
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess: "+ documentSnapshot.getData());

                if(documentSnapshot.getBoolean("admin")){
                    binding.loginNowTv.setVisibility(View.GONE);
                    binding.forgetPasswordTv.setVisibility(View.GONE);
                    isAdmin = true;
                }
            }
        });
    }


}
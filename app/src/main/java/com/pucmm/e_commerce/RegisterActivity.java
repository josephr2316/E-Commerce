package com.pucmm.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pucmm.e_commerce.databinding.ActivityRegisterBinding;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private User user;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View registerView = binding.getRoot();
        setContentView(registerView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = getIntent().getParcelableExtra("user");
        if (user!=null)
        {
            binding.loginNowTv.setVisibility(View.GONE);
            binding.forgetPasswordTv.setVisibility(View.GONE);
        }

        binding.registerBt.setOnClickListener(view ->{
            User newUser = new User();
            newUser.setName(binding.nameEdt.getText().toString());
            newUser.setUser(binding.userEdt.getText().toString());
            newUser.setPassword(binding.passwordEdt.getText().toString());
            newUser.setTelephoneNumber(binding.phoneEdt.getText().toString());
            newUser.setAdmin(false);
            if (user!=null)
            {
                builder = new AlertDialog.Builder(this);
                builder.setMessage("El usuario es un administrador?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {newUser.setAdmin(true);}
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newUser.setAdmin(false);
                                dialog.cancel();
                            }
                        })
                        .show();
            }

            firebaseAuth.createUserWithEmailAndPassword(binding.emailEdit.getText().toString(),binding.passwordEdt.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(RegisterActivity.this,"Account created", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    DocumentReference df = firebaseFirestore.collection("Users").document(user.getUid());
                    //Map<String,Object> name = new HashMap<>();

                    df.set(newUser);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this,"Failed to Create Account", Toast.LENGTH_SHORT).show();

                }
            });
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
}
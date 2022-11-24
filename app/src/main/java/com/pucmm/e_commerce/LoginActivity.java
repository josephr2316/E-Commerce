package com.pucmm.e_commerce;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pucmm.e_commerce.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View viewLogin = binding.getRoot();
        setContentView(viewLogin);

        binding.etEmail.addTextChangedListener(textWatcher);
        binding.etPassword.addTextChangedListener(textWatcher);
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.btLogin.setOnClickListener(view -> {
            firebaseAuth.signInWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        });
        binding.forgetPasswordBt.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            startActivity(intent);
        });
        binding.singUpBt.setOnClickListener(viewNew -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });



    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            binding.btLogin.setEnabled(!email.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onStart() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(this,MainActivity.class);
            //startActivity(intent);
        }
        super.onStart();
    }
}
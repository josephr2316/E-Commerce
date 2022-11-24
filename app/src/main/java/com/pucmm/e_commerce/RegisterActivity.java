package com.pucmm.e_commerce;



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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pucmm.e_commerce.databinding.ActivityRegisterBinding;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private User newUser;
    AlertDialog.Builder builder;
    boolean isAdmin;
    String userUID;


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
                                firebaseAuth.createUserWithEmailAndPassword(binding.emailEdit.getText().toString(),binding.passwordEdt.getText().toString()).addOnSuccessListener(authResult -> {
                                    Toast.makeText(RegisterActivity.this,"Account created", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    assert user != null;
                                    DocumentReference df = firebaseFirestore.collection("Users").document(user.getUid());
                                    //Map<String,Object> name = new HashMap<>();

                                    df.set(newUser);
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this,"Failed to Create Account", Toast.LENGTH_SHORT).show());

                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                newUser.setAdmin(false);
                                firebaseAuth.createUserWithEmailAndPassword(binding.emailEdit.getText().toString(),binding.passwordEdt.getText().toString()).addOnSuccessListener(authResult -> {
                                    Toast.makeText(RegisterActivity.this,"Account created", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    assert user != null;
                                    DocumentReference df = firebaseFirestore.collection("Users").document(user.getUid());
                                    //Map<String,Object> name = new HashMap<>();

                                    df.set(newUser);
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this,"Failed to Create Account", Toast.LENGTH_SHORT).show());

                                dialog.cancel();
                            })
                            .show();

                }
                else{
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
package com.pucmm.e_commerce.ui;

import androidx.activity.ViewTreeOnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.databinding.ActivityForgetPasswordBinding;
import com.pucmm.e_commerce.databinding.ActivityLoginBinding;
import com.pucmm.e_commerce.databinding.ActivityRegisterBinding;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        View forgetPasswordView = binding.getRoot();
        setContentView(forgetPasswordView);
        firebaseFirestore  = FirebaseFirestore.getInstance();
        firebaseStorage = firebaseStorage.getInstance();

        binding.getpasswordBt.setOnClickListener(view ->{
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEdt.getText().toString()).matches()){
                binding.emailEdt.setError("Please provide valid email");
                binding.emailEdt.requestFocus();
                return;
            }
            //checkUserEmail(binding.emailEdt.getText().toString());
            resetUserPassword(binding.emailEdt.getText().toString());

        });

    }

    void checkUserEmail(String email){


        firebaseFirestore.collection("Users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Toast.makeText(ForgetPasswordActivity.this,"okay",Toast.LENGTH_SHORT).show();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Toast.makeText(ForgetPasswordActivity.this, documentSnapshot.getString("email"),Toast.LENGTH_SHORT).show();

                                if(documentSnapshot.getString("email").equals(email))
                                {
                                    Toast.makeText(ForgetPasswordActivity.this, "Email was founded",Toast.LENGTH_SHORT).show();
                                    resetUserPassword(email);
                                }

                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgetPasswordActivity.this, e.toString(),Toast.LENGTH_SHORT).show();
                        Log.d("TAG",e.toString());

                        Toast.makeText(ForgetPasswordActivity.this, "Email doesn't exist",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void resetUserPassword(String email){
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("verifying..");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Reset password instructions has sent to your email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error, este correo no existe", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
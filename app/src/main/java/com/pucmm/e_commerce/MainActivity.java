package com.pucmm.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pucmm.e_commerce.databinding.ActivityMainBinding;




public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ActionBarDrawerToggle toggle;
    TextView emailHeader;

    boolean disable= false;
    String userUID;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    MenuItem itemRegister;
    boolean isAdmin;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewMain = binding.getRoot();
        setContentView(viewMain);
        Menu nav_menu = binding.navView.getMenu();
        firebaseAuth = FirebaseAuth.getInstance();
        userUID = firebaseAuth.getCurrentUser().getUid();

        Toast.makeText(this, firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        firebaseFirestore = FirebaseFirestore.getInstance();
        itemRegister = nav_menu.findItem(R.id.registerUser);
        checkUserAccessLevel(userUID);


        //toas
        //Toast.makeText(this,firebaseAuth.getUid(),Toast.LENGTH_SHORT).show();



        //binding.navView.inflateMenu(R.menu);
       // nav_menu.findItem(R.id.userFullName).setVisible(false);
        //nav_menu.findItem(R.id.userEmail).setVisible(false);
        //nav_menu.findItem(R.id.registerUser).setVisible(false);
        //disable = false;

        nav_menu.setGroupVisible(R.id.groupDrop,disable);

        emailHeader = binding.navView.getHeaderView(0).findViewById(R.id.email_header);
        emailHeader.setOnTouchListener((v, event) -> {

            if(event.getAction()==MotionEvent.ACTION_DOWN){
                if(event.getRawX()>=emailHeader.getRight()-emailHeader.getTotalPaddingRight()){
                    if(!disable){
                        emailHeader.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_up,0);
                        nav_menu.setGroupVisible(R.id.groupDrop, true);

                        if(isAdmin) itemRegister.setVisible(true);
                        else itemRegister.setVisible(false);

                        disable=true;

                    }else{
                        emailHeader.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_down,0);
                        nav_menu.setGroupVisible(R.id.groupDrop, false);
                        disable=false;
                    }
                    //emailHeader.setSelected(selection);
                }
            }
            return false;
        });

        setSupportActionBar(binding.toolbar);
        toggle = new ActionBarDrawerToggle(this, binding.drawer,binding.toolbar, R.string.open,R.string.close);
        binding.drawer.addDrawerListener(toggle);
       // binding.navView.bringToFront();
        toggle.syncState();
        //NavigationUI.setupWithNavController( binding.navView,navController);
        binding.navView.setNavigationItemSelectedListener(item -> {
               switch (item.getItemId()){
                   case R.id.registerUser:
                       Intent intentRegister = new Intent(this,RegisterActivity.class);
                       startActivity(intentRegister);
                       break;

                   case R.id.nav_logout:
                       firebaseAuth.signOut();
                       Intent intent = new Intent(this,LoginActivity.class);
                       startActivity(intent);
                       break;


               }
            return false;
        });




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
    }

    void checkUserAccessLevel(String uid){


        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess: "+ documentSnapshot.getData());
                if(documentSnapshot.getBoolean("admin")){
                    //User user2 = (User) documentSnapshot.getData();
                    isAdmin = true;
                }

            }
        });
    }
}

/*
* */
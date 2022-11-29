package com.pucmm.e_commerce.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle;
    private TextView emailHeader;
    private AppBarConfiguration appBarConfiguration;
    NavHostFragment navHostFragment;
    NavController navController;


    private boolean disable;
    String userUID;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
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
        disable= false;
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

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.categoryFragment, R.id.productFragment)
                .setOpenableLayout(binding.drawer)
                .build();

       /* appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(binding.drawer)
                .build();*/
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView,navController);





     /*   toggle = new ActionBarDrawerToggle(this, binding.drawer,binding.toolbar, R.string.open,R.string.close);
        binding.drawer.addDrawerListener(toggle);
       // binding.navView.bringToFront();
        toggle.syncState();*/


        //NavigationUI.setupWithNavController( binding.navView,navController);

//
        binding.navView.setNavigationItemSelectedListener(item -> {
               switch (item.getItemId()){
                   case R.id.registerUser:
                       Intent intentRegister = new Intent(this,RegisterActivity.class);
                       intentRegister.putExtra("vista", 0);
                       startActivity(intentRegister);
                       break;

                   case R.id.homeFragment:
                       navController.navigate(R.id.homeFragment);
                       binding.drawer.close();

                       break;

                   case R.id.categoryFragment:
                       navController.navigate(R.id.categoryFragment);
                       binding.drawer.close();
                       break;

                   case R.id.productFragment:
                       navController.navigate(R.id.productFragment);
                       binding.drawer.close();
                       break;

                   case R.id.nav_logout:
                       firebaseAuth.signOut();
                       Intent intent = new Intent(this,LoginActivity.class);
                       startActivity(intent);
                       break;
                   case R.id.nav_profile:
                        Intent intentProfile = new Intent(this,RegisterActivity.class);
                        intentProfile.putExtra("vista", 1);
                        startActivity(intentProfile);
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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
package com.pucmm.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.pucmm.e_commerce.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ActionBarDrawerToggle toggle;
    TextView emailHeader;
    boolean disable= false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewMain = binding.getRoot();
        setContentView(viewMain);
        Menu nav_menu = binding.navView.getMenu();
        //binding.navView.inflateMenu(R.menu);
       // nav_menu.findItem(R.id.userFullName).setVisible(false);
        //nav_menu.findItem(R.id.userEmail).setVisible(false);
        //nav_menu.findItem(R.id.registerUser).setVisible(false);
        //disable = false;
        nav_menu.setGroupVisible(R.id.groupDrop,disable);
        emailHeader = binding.navView.getHeaderView(0).findViewById(R.id.email_header);




        setSupportActionBar(binding.toolbar);
        toggle = new ActionBarDrawerToggle(this, binding.drawer,binding.toolbar, R.string.open,R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        //NavigationUI.setupWithNavController( binding.navView,navController);
        binding.navView.setNavigationItemSelectedListener(item -> {
//                switch (item.getItemId()){
//                    case :
//                }
            return false;
        });

        emailHeader.setOnTouchListener((v, event) -> {

            if(event.getAction()==MotionEvent.ACTION_DOWN){
                if(event.getRawX()>=emailHeader.getRight()-emailHeader.getTotalPaddingRight()){
                    if(!disable){
                        emailHeader.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_arrow_drop_up,0);
                        nav_menu.setGroupVisible(R.id.groupDrop, true);
                        disable=true;

                    }else{
                        emailHeader.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_arrow_drop_down,0);
                        nav_menu.setGroupVisible(R.id.groupDrop, false);
                        disable=false;
                    }
                    //emailHeader.setSelected(selection);
                }
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
}

/*
* */
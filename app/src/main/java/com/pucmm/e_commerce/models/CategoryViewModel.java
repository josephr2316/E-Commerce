package com.pucmm.e_commerce.models;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.repositories.FirebaseRepository;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel {

     private MutableLiveData<ArrayList<Category>> liveData;
     private FirebaseRepository firebaseRepository;

    public void init(){
        if  (liveData != null){
            return;
        }
        firebaseRepository = FirebaseRepository.getInstance();
        liveData = firebaseRepository.getList();
    }

    void addCategory(){

    }
    public MutableLiveData<ArrayList<Category>> getLiveData() {
        Log.e("LIVEDATA", liveData.toString());
        return liveData;
    }


}

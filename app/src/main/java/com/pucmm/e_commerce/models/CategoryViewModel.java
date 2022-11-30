package com.pucmm.e_commerce.models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.repositories.FirebaseRepository;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel {

     private MutableLiveData<ArrayList<Category>> liveData;
     private FirebaseRepository firebaseRepository;

    public void init(Context context){
        if  (liveData != null){
            return;
        }
        firebaseRepository = FirebaseRepository.getInstance(context);
        liveData = firebaseRepository.getCategory();
    }

    void addCategory(){

    }
    public LiveData<ArrayList<Category>> getLiveData() {
        return liveData;
    }


}

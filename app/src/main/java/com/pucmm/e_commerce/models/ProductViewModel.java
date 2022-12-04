package com.pucmm.e_commerce.models;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;
import com.pucmm.e_commerce.repositories.FirebaseRepository;

import java.util.ArrayList;

public class ProductViewModel  extends ViewModel {
    private MutableLiveData<ArrayList<Product>> liveData;
    private FirebaseRepository firebaseRepository;

    public void init(){
        if  (liveData != null){
            return;
        }
        firebaseRepository = FirebaseRepository.getInstance();
        liveData = firebaseRepository.getProductList();

    }

    public void addProduct(Product product,String name){
        firebaseRepository.addProduct(product,name);

    }

    public void guardarImagen(String id, Bitmap image) {
        firebaseRepository.guardarImagen(id,image);
    }

    public MutableLiveData<ArrayList<Product>> getLiveData() {
        Log.e("LIVEDATA", liveData.toString());
        return liveData;
    }


}

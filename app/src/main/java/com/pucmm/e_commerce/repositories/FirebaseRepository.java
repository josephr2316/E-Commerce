package com.pucmm.e_commerce.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FirebaseRepository {

    /**
     * Singleton pattern
     */
    private static FirebaseRepository instance;

    private ArrayList<Category> arrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final String TAG = "FirebaseRepository";
    public  MutableLiveData<ArrayList<Category>> data;

    public MutableLiveData<ArrayList<Category>> getList(){
        if(data==null){
            data =new MutableLiveData<ArrayList<Category>>();
            getCategory();
        }
        return data;
    }
    public static FirebaseRepository getInstance() {
        if (instance == null){
            instance = new FirebaseRepository();
        }
        return instance;
    }

    public void getCategory(){
        loadCategory();

    }
    private void loadCategory(){
        firebaseFirestore.collection("Categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        ArrayList<Category> productArrayList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : list){
                            Log.e(TAG, "ESTA ENTRANDO" );
                            productArrayList.add(documentSnapshot.toObject(Category.class));
                            arrayList.add(documentSnapshot.toObject(Category.class));
                        }
                        data.setValue(arrayList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure",e);
                    }
                });
    }
}

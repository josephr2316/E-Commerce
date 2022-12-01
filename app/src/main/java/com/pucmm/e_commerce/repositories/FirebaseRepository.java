package com.pucmm.e_commerce.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucmm.e_commerce.database.Category;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    /**
     * Singleton pattern
     */
    private static FirebaseRepository instance;

    private ArrayList<Category> arrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final String TAG = "FirebaseRepository";


    public static FirebaseRepository getInstance(Context context) {
        if (instance == null){
            instance = new FirebaseRepository();
        }
        return instance;
}

    public MutableLiveData<ArrayList<Category>> getCategory(){

        loadCategory();
        MutableLiveData<ArrayList<Category>> data = new MutableLiveData<>();
        data.setValue(arrayList);
        return data;
    }
    private void loadCategory(){
        firebaseFirestore.collection("Categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot : list){
                        Log.i("here",documentSnapshot.toObject(Category.class).getNombre());
                        arrayList.add(documentSnapshot.toObject(Category.class));
                        Log.i("no esta bien",arrayList.get(0).getNombre());
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure",e);
                    }
                });
    }
}

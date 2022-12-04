package com.pucmm.e_commerce.repositories;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
    private ArrayList<Product> arrayListProduct = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final String TAG = "FirebaseRepository";
    public  MutableLiveData<ArrayList<Category>> data;
    public  MutableLiveData<ArrayList<Product>> dataProduct;


    public MutableLiveData<ArrayList<Category>> getList(){
        if(data==null){
            data =new MutableLiveData<ArrayList<Category>>();
            getCategory();
        }
        return data;
    }

    public MutableLiveData<ArrayList<Product>> getProductList(){
        if(dataProduct==null){
            dataProduct =new MutableLiveData<ArrayList<Product>>();
            getProduct();
        }
        return dataProduct;
    }

    public static FirebaseRepository getInstance() {
        if (instance == null){
            instance = new FirebaseRepository();
        }
        return instance;
    }
/*    public MutableLiveData<ArrayList<Category>> getCategory(){
        loadCategory();
        MutableLiveData<ArrayList<Category>> data = new MutableLiveData<>();
        data.setValue(arrayList);
        return data;
    }*/

    public void getCategory(){
        loadCategory();

    }

   public void  getProduct(){
        loadProduct();
    }

    private void loadCategory(){
        firebaseFirestore.collection("Categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot documentSnapshot : list){
                            Log.e(TAG, "ESTA ENTRANDO" );
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
    private void loadProduct(){
        firebaseFirestore.collection("Categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot documentSnapshot : list){
                            Log.e(TAG, "ESTA ENTRANDO category" );
                            if (documentSnapshot.toObject(Category.class).getProductList()!=null) {
                                ArrayList<Product> productArrayList = new ArrayList<>();

                                int size = documentSnapshot.toObject(Category.class).getProductList().size();
                                
                                for ( int i = 0; i < size ; i++) {
                                    productArrayList.add(documentSnapshot.toObject(Category.class).getProductList().get(i));
                                }
                                Log.e(TAG, documentSnapshot.toObject(Category.class).getProductList().toString());
                                for (Product product : productArrayList) {
                                    Log.e(TAG, "ESTA ENTRANDO product list");
                                    arrayListProduct.add(product);
                                }
                                dataProduct.setValue(arrayListProduct);
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

    public void addProduct(Product product, String nameCategory){
        arrayListProduct .add(product);

        DocumentReference docRef = firebaseFirestore.collection("Categories").document(nameCategory);

        docRef.update("productList",product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("prueba", "SE EDITO");
            }
        });
    }
}

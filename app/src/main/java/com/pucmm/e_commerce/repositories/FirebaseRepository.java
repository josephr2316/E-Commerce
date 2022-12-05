package com.pucmm.e_commerce.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;
import com.pucmm.e_commerce.ui.RegisterActivity;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FirebaseRepository {

    /**
     * Singleton pattern
     */
    private static FirebaseRepository instance;

    private ArrayList<Category> arrayList = new ArrayList<>();
    private ArrayList<Product> arrayListProduct = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
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

    public void addCategory(Category category ){
        CollectionReference collectionReference = firebaseFirestore.collection("Categories");
        collectionReference.document(category.getId()).set(category).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                arrayList.add(category);
            }
        });
    }
    public void updateCategory(Category category){
        Map<String, Object> categoryUpdateData = new HashMap<>();
        categoryUpdateData.put("nombre", category.getNombre());
        firebaseFirestore.collection("Categories").document(category.getId()).update(categoryUpdateData);
    }
    public void removeCategory(Category category){
        firebaseFirestore.collection("Categories").document(category.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("Categoria", "Eliminada");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e("No se pudo eliminar","eliminando");
                    }
                });
        arrayList.remove(category);
    }


    public void getCategory(){
        loadCategory();

    }
    public void guardarImagen(String id, Bitmap image){
        String path = "images/"+id;
        StorageReference reference = firebaseStorage.getReference().child(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datos = baos.toByteArray();

        UploadTask uploadTask = reference.putBytes(datos);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            Log.i("GUARDADO", "SE GUARDO CORRECTAMENTE");
            // ...
        });
    }
    public void downloadAndSetImage(String nombreImagen, ImageView imagen){
        StorageReference reference = firebaseStorage.getReference().child("images/"+nombreImagen);
        final long ONE_MEGABYTE = 1024 * 1024;

        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imagen.setImageBitmap(Bitmap.createScaledBitmap(bmp, imagen.getWidth(), imagen.getHeight(), false));
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
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
                            Log.i(TAG, documentSnapshot.getId());
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
                            Log.e("error categoria",documentSnapshot.toObject(Category.class).getNombre());
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
        List<Product> list = new ArrayList<>();

        DocumentReference docRef = firebaseFirestore.collection("Categories").document(nameCategory);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.toObject(Category.class).getProductList()!=null)
                {
                    int size = documentSnapshot.toObject(Category.class).getProductList().size();
                    for ( int i = 0; i < size ; i++) {
                        list.add(documentSnapshot.toObject(Category.class).getProductList().get(i));
                    }
                }
                list.add(product);

                docRef.update("productList",list).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("prueba", "SE EDITO");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error",e.toString());
                    }
                });

            }
        });

    }


}

package com.pucmm.e_commerce.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaDrm;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private static final int EDITAR_PRODUCT= 0;
    private static final int BORRAR_PRODUCT = 1;
    private boolean isAdmin;
    private String nameCategory;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public ProductAdapter(){

    }

    public void setProductList (List<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }

    public void filterList (List<Product> filterList){
        this.productList = filterList;
        notifyDataSetChanged();
    }
    public void checkAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;

    }

    public void removeProductList(Product product){
        this.productList.remove(product);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.product_adapter, parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {

        Product product = productList.get(position);

        Log.i("ENTRAR","adapter product");

        holder.uuid.setText(product.getCodigo());
        holder.price.setText(product.getPrecio());
        holder.description.setText(product.getDescripcion());
        disableSetting(holder.setting);
        StorageReference reference = firebaseStorage.getReference().child("images/products/"+product.getFirstImage());
        final long ONE_MEGABYTE = 1024 * 1024;

        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.image.setImageBitmap(Bitmap.createScaledBitmap(bmp, holder.image.getWidth(),holder.image.getHeight(), false));
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections = ProductFragmentDirections.actionProductFragmentToDetailsProduct(product);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(navDirections);
            }
        });
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "This is the setting for the product adapterc", Toast.LENGTH_SHORT).show();
                dialogOptions(context, product, view);
            }
        });
        Log.e("category_adapter", "despues de la imagen");
    }

    private void dialogOptions(Context context, Product product, View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Seleccione una opcion:")
                .setItems(R.array.product_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == EDITAR_PRODUCT){
                            NavDirections navDirections = ProductFragmentDirections.actionProductFragmentToRegisterProductFragment(product);
                            NavController navController = Navigation.findNavController(view);
                            navController.navigate(navDirections);
                        } else if (which==BORRAR_PRODUCT) {
                            dialogConfirm(context, product);
                        }
                        else{
                            dialog.dismiss();
                        }
                    }
                });
        builder1.show();
    }
    private void dialogConfirm(Context context, Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirma");
        builder.setMessage("Estas seguro?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                loadProduct(product);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView uuid;
        private TextView price;
        private TextView description;
        private ImageView setting;
        private CardView cardView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_imageview);
            uuid = itemView.findViewById(R.id.uuid_tv);
            price = itemView.findViewById(R.id.price_tv);
            description = itemView.findViewById(R.id.description_tv);
            setting = itemView.findViewById(R.id.setting_imageview);
            cardView = itemView.findViewById(R.id.card);
        }
    }
    public void disableSetting(ImageView setting){
        if (!isAdmin)
            setting.setVisibility(View.GONE);
        else
            setting.setVisibility(View.VISIBLE);

    }
    private void loadProduct(Product productCheck){
        List<Product> products = new ArrayList<>();
        firebaseFirestore.collection("Categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot documentSnapshot : list){
                            if (documentSnapshot.toObject(Category.class).getProductList()!=null) {
                                ArrayList<Product> productArrayList = new ArrayList<>();

                                int size = documentSnapshot.toObject(Category.class).getProductList().size();

                                for ( int i = 0; i < size ; i++) {
                                    productArrayList.add(documentSnapshot.toObject(Category.class).getProductList().get(i));
                                }
                                for (Product productf : productArrayList) {
                                    if(productf.getCodigo().equals(productCheck.getCodigo()) || nameCategory!=null){
                                        removeProductList(productf);
                                        nameCategory = documentSnapshot.toObject(Category.class).getId();
                                        if(!productf.getCodigo().equals(productCheck.getCodigo())){
                                            products.add(productf);

                                        }
                                    }
                                }
                                if  (nameCategory!=null)
                                {
                                    DocumentReference docRef = firebaseFirestore.collection("Categories").document(nameCategory);
                                    docRef.update("productList",products).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("prueba", "SE EDITO");
                                            nameCategory = null;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ErrorDelete",e.toString());
                                        }
                                    });
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("error","onFailure",e);
                    }
                });
    }

}

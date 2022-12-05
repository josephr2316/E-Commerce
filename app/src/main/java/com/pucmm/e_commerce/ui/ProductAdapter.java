package com.pucmm.e_commerce.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaDrm;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private boolean isAdmin;

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
        Log.e("category_adapter", "despues de la imagen");

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

}

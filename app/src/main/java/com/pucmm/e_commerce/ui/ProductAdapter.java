package com.pucmm.e_commerce.ui;

import android.content.Context;
import android.media.MediaDrm;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList) {this.productList = productList;}

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
        String uuid;
        String image;
        String price;
        String description;

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private EditText uuid;
        private EditText price;
        private EditText description;
        private ImageView setting;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_imageview);
            uuid = itemView.findViewById(R.id.uuid_tv);
            price = itemView.findViewById(R.id.price_tv);
            description = itemView.findViewById(R.id.description_tv);
            setting = itemView.findViewById(R.id.setting_imageview);
        }
    }

}

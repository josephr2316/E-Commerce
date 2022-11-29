package com.pucmm.e_commerce.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> categoryList;
    private Context context;



    public CategoryAdapter(List<Category> list) {this.categoryList = list;}


    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.category_adapter, parent, false);
        return new CategoryViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {

        Category category = categoryList.get(position);
        String name;
        String image;

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public static class  CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private EditText name;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.category_iv);
            name = itemView.findViewById(R.id.setting_bt);
        }
    }

}

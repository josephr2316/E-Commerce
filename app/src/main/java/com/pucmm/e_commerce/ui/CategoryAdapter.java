package com.pucmm.e_commerce.ui;

import android.content.Context;
import android.util.Log;
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

    private  List<Category> categoryList;
    private Context context;

   // public CategoryAdapter(List<Category> list) {this.categoryList = list;}
    public CategoryAdapter( ){}

    public void setCategoryList (List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

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

        Log.i("ENTRAR", category.getNombre());
        holder.name.setText(category.getNombre());
        String image;

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public static class  CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.category_iv);
            name = itemView.findViewById(R.id.categoryName_tv);
        }
    }

}

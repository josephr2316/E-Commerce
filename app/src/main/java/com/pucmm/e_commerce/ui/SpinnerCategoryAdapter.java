package com.pucmm.e_commerce.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;

import java.util.List;

public class SpinnerCategoryAdapter extends BaseAdapter {
    private final Context context;
    private final List<Category> categoryList;

    public SpinnerCategoryAdapter(Context context, List<Category> categoryList){
        this.context = context;
        this.categoryList = categoryList;
    }
    @Override
    public int getCount() {
        return categoryList !=null ? categoryList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_category,parent,false);

        TextView textview = rootView.findViewById(R.id.name);
        ImageView image = rootView.findViewById(R.id.image);
        return rootView;
    }
}

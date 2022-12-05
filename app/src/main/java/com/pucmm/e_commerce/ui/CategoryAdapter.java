package com.pucmm.e_commerce.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pucmm.e_commerce.R;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.database.Product;
import com.pucmm.e_commerce.repositories.FirebaseRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private  List<Category> categoryList;
    private Context context;
    private FirebaseRepository firebaseRepository;
    private static final int EDITAR_CATEGORY = 0;
    private static final int BORRAR_CATEGORY = 1;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
   // public CategoryAdapter(List<Category> list) {this.categoryList = list;}
    public CategoryAdapter( ){}

    public void setCategoryList (List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public void filterList (List<Category> filterList){
        this.categoryList = filterList;
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
        holder.name.setText(category.getNombre().toString());
        StorageReference reference = firebaseStorage.getReference().child("images/"+category.getImagen());
        final long ONE_MEGABYTE = 1024 * 1024;

        reference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.image.setImageBitmap(Bitmap.createScaledBitmap(bmp, holder.image.getWidth(),holder.image.getHeight(), false));
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
        Log.e("category_adapter", "despues de la imagen");
        holder.setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions(context, category, view);
            }
        });
    }
    private void dialogOptions(Context context, Category category, View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Seleccione una opcion:")
                .setItems(R.array.category_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == EDITAR_CATEGORY){
                            NavDirections navDirections = CategoryFragmentDirections.actionCategoryFragmentToRegisterCategoryFragment(category);
                            NavController   navController = Navigation.findNavController(view);
                            navController.navigate(navDirections);
                        } else if (which==BORRAR_CATEGORY) {
                            dialogConfirm(context, category);
                        }
                        else{
                            dialog.dismiss();
                        }
                    }
                });
        builder1.show();
    }
    private void dialogConfirm(Context context, Category category){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirma");
        builder.setMessage("Estas seguro?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Log.i("error", category.getId());
                firebaseFirestore.collection("Categories").document(category.getId())
                        .delete()
                        .addOnSuccessListener(unused -> {
                            Log.i("Categoria", "Eliminada");
                            notifyDataSetChanged();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> Log.e("No se pudo eliminar","eliminando"));
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
        return categoryList.size();
    }


    public static class  CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private ImageView setting_button;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.category_iv);
            name = itemView.findViewById(R.id.categoryName_tv);
            setting_button = itemView.findViewById(R.id.setting_bt);
        }
    }

}

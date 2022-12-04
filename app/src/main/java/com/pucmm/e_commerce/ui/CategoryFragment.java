package com.pucmm.e_commerce.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pucmm.e_commerce.database.Category;
import com.pucmm.e_commerce.databinding.FragmentCategoryBinding;
import com.pucmm.e_commerce.models.CategoryViewModel;
import com.pucmm.e_commerce.repositories.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;
    private List<Category> list;
    private FirebaseFirestore firebaseFirestore;
    public CategoryAdapter categoryAdapter;
    private CategoryViewModel categoryViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater,container,false);
        View viewCategory = binding.getRoot();
        return viewCategory;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        
        int spanCount = 1;
        binding.recyclerView.setHasFixedSize(true);
        super.onViewCreated(view, savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 2;
        }
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), spanCount));
        categoryAdapter = new CategoryAdapter();


        firebaseFirestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.init();
        categoryViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                Log.i("VALOR", String.valueOf(categories.size()));
                categoryAdapter.setCategoryList(categories);
                //firebaseListener();
                binding.recyclerView.setAdapter(categoryAdapter);


            }
        });

    }
    void firebaseListener(){
        firebaseFirestore.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!= null){
                    Log.e("FireStore error",error.getMessage());
                }
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        list.add(documentChange.getDocument().toObject(Category.class));

                    }
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        //Second way

        firebaseFirestore.collection("Categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnap : documentSnapshots){
                    Category category = documentSnap.toObject(Category.class);
                    list.add(category);
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

}
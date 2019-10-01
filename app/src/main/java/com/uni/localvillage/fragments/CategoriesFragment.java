package com.uni.localvillage.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.localvillage.R;
import com.uni.localvillage.Utils;
import com.uni.localvillage.activity.ServiceProviderActivity;
import com.uni.localvillage.adapter.CategoryAdapter;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment implements CategoryAdapter.OnCategoryAdapterListener {

    private ArrayList<String> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        categoryAdapter = new CategoryAdapter(categories, this);
        recyclerView.setAdapter(categoryAdapter);

        categories.addAll(Utils.getCategories());

        return view;
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ServiceProviderActivity.class);
        intent.putExtra("key", categories.get(position));
        startActivity(intent);
    }
}

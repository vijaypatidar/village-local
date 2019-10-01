package com.uni.localvillage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.localvillage.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private ArrayList<String> categories;
    private OnCategoryAdapterListener onCategoryAdapterListener;

    public CategoryAdapter(ArrayList<String> categories, OnCategoryAdapterListener onCategoryAdapterListener) {
        this.categories = categories;
        this.onCategoryAdapterListener = onCategoryAdapterListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String category = categories.get(position);
        holder.categoryTitle.setText(category);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryAdapterListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface OnCategoryAdapterListener {
        void onItemClick(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
        }
    }
}

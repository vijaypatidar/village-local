package com.uni.localvillage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.localvillage.R;
import com.uni.localvillage.model.ServiceProvider;

import java.util.ArrayList;

public class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderAdapter.MyViewHolder> {
    private ArrayList<ServiceProvider> serviceProviders;
    private OnServiceProviderAdapterListener onServiceProviderAdapterListener;

    public ServiceProviderAdapter(ArrayList<ServiceProvider> serviceProviders, OnServiceProviderAdapterListener onServiceProviderAdapterListener) {
        this.serviceProviders = serviceProviders;
        this.onServiceProviderAdapterListener = onServiceProviderAdapterListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_provider_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onServiceProviderAdapterListener.onClickServiceCall(position);
            }
        });
        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onServiceProviderAdapterListener.onClickServiceBook(position);
            }
        });
        holder.categoryTitle.setText(serviceProviders.get(position).getName());
        ServiceProvider serv = serviceProviders.get(position);
        String address = serv.getCity() + "," + serv.getDistrict() + "," + serv.getState();
        holder.address.setText(address);

    }

    @Override
    public int getItemCount() {
        return serviceProviders.size();
    }

    public interface OnServiceProviderAdapterListener {
        void onClickServiceBook(int position);

        void onClickServiceCall(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle, address;
        Button btnCall, btnBook;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.name);
            btnBook = itemView.findViewById(R.id.btnBook);
            btnCall = itemView.findViewById(R.id.btnCall);
            address = itemView.findViewById(R.id.address);
        }
    }

}

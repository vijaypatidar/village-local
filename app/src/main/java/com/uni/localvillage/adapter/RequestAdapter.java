package com.uni.localvillage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.localvillage.R;
import com.uni.localvillage.model.Request;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    private ArrayList<Request> requests;
    private OnRequestAdapterListner onRequestAdapterListner;

    public RequestAdapter(ArrayList<Request> requests, OnRequestAdapterListner onRequestAdapterListner) {
        this.requests = requests;
        this.onRequestAdapterListner = onRequestAdapterListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_request, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Request request = requests.get(position);
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestAdapterListner.OnCall(position);
            }
        });
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestAdapterListner.OnAccept(position);
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestAdapterListner.OnReject(position);
            }
        });

        if (Request.STATUS_PENDIND != request.getStatus()) {
            holder.btnReject.setVisibility(View.INVISIBLE);
            holder.btnAccept.setVisibility(View.INVISIBLE);
        }
        holder.name.setText(request.getName());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public interface OnRequestAdapterListner {
        void OnReject(int pos);

        void OnCall(int pos);

        void OnAccept(int pos);

        void OnDelete(int pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button btnAccept, btnReject, btnCall;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }


}

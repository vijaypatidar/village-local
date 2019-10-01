package com.uni.localvillage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.localvillage.R;
import com.uni.localvillage.model.Booking;
import com.uni.localvillage.model.Request;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private ArrayList<Booking> bookings;
    private OnBookingAdapterListener onBookingAdapterListener;

    public BookingAdapter(ArrayList<Booking> bookings, OnBookingAdapterListener onBookingAdapterListener) {
        this.bookings = bookings;
        this.onBookingAdapterListener = onBookingAdapterListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_booking, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Booking booking = bookings.get(position);

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBookingAdapterListener.OnCall(position);
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBookingAdapterListener.OnCancel(position);
            }
        });


        if (Request.STATUS_PENDIND != booking.getStatus()) {
            holder.btnCancel.setVisibility(View.INVISIBLE);
            if (Request.STATUS_ACCEPT == booking.getStatus()) {
                holder.status.setText("accepted");
            } else {
                holder.status.setText("rejected");
                holder.btnCall.setVisibility(View.GONE);
            }
        }


        holder.name.setText(booking.getName());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public interface OnBookingAdapterListener {
        void OnCall(int pos);

        void OnCancel(int pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, status;
        Button btnCancel, btnCall;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }


}

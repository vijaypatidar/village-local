package com.uni.localvillage.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uni.localvillage.R;
import com.uni.localvillage.adapter.BookingAdapter;
import com.uni.localvillage.model.Booking;

import java.util.ArrayList;

public class BookingFragment extends Fragment implements BookingAdapter.OnBookingAdapterListener {
    private String UID;
    private ArrayList<Booking> bookings = new ArrayList<>();
    private BookingAdapter bookingAdapter;

    public BookingFragment() {
        UID = FirebaseAuth.getInstance().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        RecyclerView recyclerView = (RecyclerView) view;

        bookingAdapter = new BookingAdapter(bookings, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bookingAdapter);
        fetchBooking();
        return view;
    }

    private void fetchBooking() {
        FirebaseFirestore.getInstance().collection("bookings").document(UID).collection("booking")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bookings.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    Booking booking = ds.toObject(Booking.class);
                    bookings.add(booking);
                }
                bookingAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void OnCall(int pos) {
        Booking booking = bookings.get(pos);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + booking.getPhone()));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CALL_PHONE
            }, 101);
        }
    }

    @Override
    public void OnCancel(int pos) {
        Booking booking = bookings.get(pos);
        bookings.remove(booking);
        handleRequest(booking);
    }

    private void handleRequest(final Booking request) {
        ProgressDialog.Builder pb = new AlertDialog.Builder(getContext());
        pb.setTitle("Deleting...");
        pb.setCancelable(false);
        pb.setView(new ProgressBar(getContext()));

        final AlertDialog alertDialog = pb.create();
        alertDialog.show();
        FirebaseFirestore.getInstance().collection("bookings").document(UID).collection("booking")
                .document(request.getUID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseFirestore.getInstance().collection("requests").document(request.getUID())
                        .collection("request").document(UID)
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bookingAdapter.notifyDataSetChanged();
                        alertDialog.cancel();
                    }
                });

            }
        });
    }


}

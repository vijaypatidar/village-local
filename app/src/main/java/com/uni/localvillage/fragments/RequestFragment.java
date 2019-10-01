package com.uni.localvillage.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.uni.localvillage.adapter.RequestAdapter;
import com.uni.localvillage.model.Request;

import java.util.ArrayList;

public class RequestFragment extends Fragment implements RequestAdapter.OnRequestAdapterListner {
    private String UID;
    private RequestAdapter requestAdapter;
    private ArrayList<Request> requests = new ArrayList<>();
    private Context context;

    public RequestFragment() {
        UID = FirebaseAuth.getInstance().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        context = container.getContext();

        RecyclerView recyclerView = (RecyclerView) view;
        requestAdapter = new RequestAdapter(requests, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(requestAdapter);
        fetchRequest();
        return view;
    }

    private void fetchRequest() {
        FirebaseFirestore.getInstance().collection("requests").document(UID)
                .collection("request").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                requests.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    Request request = ds.toObject(Request.class);
                    requests.add(request);
                }
                requestAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void OnReject(int pos) {
        Request request = requests.get(pos);
        request.setStatus(Request.STATUS_REJECT);
        handleRequest(Request.STATUS_REJECT, request);
    }

    @Override
    public void OnCall(int pos) {
        Request request = requests.get(pos);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + request.getPhone()));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CALL_PHONE
            }, 101);
        }
    }

    @Override
    public void OnDelete(int pos) {

    }

    @Override
    public void OnAccept(int pos) {
        Request request = requests.get(pos);
        request.setStatus(Request.STATUS_ACCEPT);
        handleRequest(Request.STATUS_ACCEPT, request);
    }

    private void handleRequest(final int Status, final Request request) {
        ProgressDialog.Builder pb = new AlertDialog.Builder(getContext());
        pb.setTitle(Status == Request.STATUS_ACCEPT ? "accepting..." : "rejecting...");
        pb.setCancelable(false);
        pb.setView(new ProgressBar(getContext()));

        final AlertDialog alertDialog = pb.create();
        alertDialog.show();
        FirebaseFirestore.getInstance().collection("bookings").document(request.getUID()).collection("booking")
                .document(UID).update("status", Status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseFirestore.getInstance().collection("requests").document(UID)
                        .collection("request").document(request.getUID())
                        .update("status", Status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        requestAdapter.notifyDataSetChanged();
                        alertDialog.cancel();
                    }
                });

            }
        });
    }


}

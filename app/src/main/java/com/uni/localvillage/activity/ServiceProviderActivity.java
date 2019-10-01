package com.uni.localvillage.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uni.localvillage.R;
import com.uni.localvillage.Utils;
import com.uni.localvillage.adapter.ServiceProviderAdapter;
import com.uni.localvillage.model.Booking;
import com.uni.localvillage.model.Request;
import com.uni.localvillage.model.ServiceProvider;

import java.util.ArrayList;
import java.util.Date;

public class ServiceProviderActivity extends AppCompatActivity implements ServiceProviderAdapter.OnServiceProviderAdapterListener {

    private ArrayList<ServiceProvider> serviceProviders = new ArrayList<>();
    private ArrayList<ServiceProvider> selected = new ArrayList<>();

    private ServiceProviderAdapter serviceProviderAdapter;
    private String key;
    private String UID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);

        key = getIntent().getStringExtra("key");
        UID = FirebaseAuth.getInstance().getUid();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceProviderAdapter = new ServiceProviderAdapter(selected, this);
        recyclerView.setAdapter(serviceProviderAdapter);

        fetchServiceProvider();

        EditText editSearch = findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String keys = charSequence.toString().trim();
                selected.clear();
                for (ServiceProvider sp : serviceProviders) {
                    if (sp.getState().contains(keys) || sp.getDistrict().contains(keys) ||
                            sp.getCity().contains(keys)) {
                        selected.add(sp);
                    }
                }
                serviceProviderAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void fetchServiceProvider() {
        FirebaseFirestore.getInstance().collection("serviceProviders").whereEqualTo("category", key)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                serviceProviders.clear();
                selected.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                    ServiceProvider serviceProvider = ds.toObject(ServiceProvider.class);
                    serviceProvider.setUID(ds.getId());
                    serviceProviders.add(serviceProvider);
                    selected.add(serviceProvider);
                }
                serviceProviderAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClickServiceBook(int position) {
        if (UID != null) {
            ProgressDialog.Builder pb = new AlertDialog.Builder(this);
            pb.setTitle("Progressing request...");
            pb.setCancelable(false);
            pb.setView(new ProgressBar(this));

            final AlertDialog alertDialog = pb.create();
            alertDialog.show();
            final ServiceProvider serviceProvider = serviceProviders.get(position);

            String name = Utils.getValue(this, Utils.NAME);
            String phone = Utils.getValue(this, Utils.PHONE);

            Booking booking = new Booking(serviceProvider.getUID(), serviceProvider.getName(),
                    serviceProvider.getMobile(), new Date().toString(), Request.STATUS_PENDIND);


            final Request request = new Request(UID, name, phone, new Date().toString(), Request.STATUS_PENDIND);


            FirebaseFirestore.getInstance().collection("bookings").document(UID).collection("booking").document(serviceProvider.getUID())
                    .set(booking)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseFirestore.getInstance().collection("requests").document(serviceProvider.getUID())
                                    .collection("request").document(UID)
                                    .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    alertDialog.cancel();
                                    Utils.showSuccessMessage(ServiceProviderActivity.this,
                                            "Request sent");
                                }
                            });
                        }
                    });

        } else {
            Toast.makeText(this, "login to use this feature", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClickServiceCall(int position) {
        ServiceProvider serviceProvider = serviceProviders.get(position);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + serviceProvider.getMobile()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CALL_PHONE
            }, 101);
        }
    }


}

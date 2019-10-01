package com.uni.localvillage.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uni.localvillage.R;
import com.uni.localvillage.Utils;
import com.uni.localvillage.model.ServiceProvider;
import com.uni.localvillage.model.User;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;

public class ProfileActivity extends AppCompatActivity {

    private boolean isProvider;
    private Button btnSaveChange;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private EditText editName, editEmail, editPhone, editCountry, editDistrict, editState, editCity;
    private String UID;
    private LinearLayout layout;
    private CheckBox checkboxIsProvider;
    private int selectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        btnSaveChange = findViewById(R.id.btnSaveChange);
        btnSaveChange.setVisibility(GONE);

        layout = findViewById(R.id.layout_services);
        layout.setVisibility(GONE);
        checkboxIsProvider = findViewById(R.id.checkboxIsProvider);
        checkboxIsProvider.setEnabled(false);
        editCountry = findViewById(R.id.editCountry);
        editState = findViewById(R.id.editState);
        editDistrict = findViewById(R.id.editDistrict);
        editCity = findViewById(R.id.editVillage);

        final Spinner spinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(adapter);

        arrayList.addAll(Utils.getCategories());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String country = editCountry.getText().toString();
                String state = editState.getText().toString();
                String district = editDistrict.getText().toString();
                String city = editCity.getText().toString();

                isProvider = checkboxIsProvider.isChecked();
                if (name.isEmpty()) {
                    editName.setError("require");
                    return;
                }
                if (email.isEmpty()) {
                    editEmail.setError("require");
                    return;
                }
                if (phone.isEmpty() || phone.length() < 10) {
                    editPhone.setError("require");
                    return;
                }

                final User user = new User(name, email, phone, isProvider);
                Utils.setUser(Objects.requireNonNull(ProfileActivity.this), user);

                final ServiceProvider serviceProvider = new ServiceProvider();
                if (isProvider) {
                    if (country.isEmpty()) {
                        editCountry.setText(getString(R.string.alert_require));
                        return;
                    }
                    if (state.isEmpty()) {
                        editState.setText(getString(R.string.alert_require));
                        return;
                    }
                    if (district.isEmpty()) {
                        editDistrict.setText(getString(R.string.alert_require));
                        return;
                    }
                    if (city.isEmpty()) {
                        editCity.setText(getString(R.string.alert_require));
                        return;
                    }
                    serviceProvider.setCountry(country);
                    serviceProvider.setState(state);
                    serviceProvider.setDistrict(district);
                    serviceProvider.setCity(city);
                    serviceProvider.setName(name);
                    serviceProvider.setMobile(phone);
                    serviceProvider.setCategory(arrayList.get(selectedIndex));
                }

                if (isProvider) {
                    saveServiceDetail(serviceProvider, user);
                } else {
                    setUser(user);
                }
                btnSaveChange.setVisibility(GONE);
            }
        });

        getUser();

    }

    private void setUser(User user) {
        FirebaseFirestore.getInstance().collection("users").document(UID)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    private void saveServiceDetail(ServiceProvider serviceProvider, final User user) {
        FirebaseFirestore.getInstance().collection("serviceProviders").document(UID).
                set(serviceProvider).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setUser(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnSaveChange.setVisibility(GONE);
                Toast.makeText(ProfileActivity.this, "updated", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void fetchServiceDetail() {
        FirebaseFirestore.getInstance().collection("serviceProviders").document(UID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            ServiceProvider serviceProvider = documentSnapshot.toObject(ServiceProvider.class);
                            if (serviceProvider != null) {
                                editCountry.setText(serviceProvider.getCountry());
                                editState.setText(serviceProvider.getState());
                                editCity.setText(serviceProvider.getCity());
                                editDistrict.setText(serviceProvider.getDistrict());
                            }
                        }
                        btnSaveChange.setVisibility(View.VISIBLE);
                    }
                });

    }

    private void getUser() {
        FirebaseFirestore.getInstance().collection("users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    editEmail.setText(user.getEmail());
                    editName.setText(user.getName());
                    editPhone.setText(user.getPhone());
                    if (user.isProvider()) {

                        adapter.notifyDataSetChanged();
                        checkboxIsProvider.setChecked(true);
                        layout.setVisibility(View.VISIBLE);
                        fetchServiceDetail();
                    } else {
                        btnSaveChange.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}

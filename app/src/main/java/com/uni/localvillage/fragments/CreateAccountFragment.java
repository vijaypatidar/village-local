package com.uni.localvillage.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uni.localvillage.R;
import com.uni.localvillage.Utils;
import com.uni.localvillage.model.ServiceProvider;
import com.uni.localvillage.model.User;

import java.util.ArrayList;
import java.util.Objects;

public class CreateAccountFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private Button btnLogin;
    private boolean isProvider;
    private Button btnCreateAccount;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        final EditText editName = view.findViewById(R.id.editName);
        final EditText editEmail = view.findViewById(R.id.editEmail);
        final EditText editPass = view.findViewById(R.id.editPass);
        editPass.setVisibility(View.VISIBLE);
        final EditText editPhone = view.findViewById(R.id.editPhone);
        btnLogin = view.findViewById(R.id.btnLogin);
        final CheckBox checkboxIsProvider = view.findViewById(R.id.checkboxIsProvider);
        final View layout = view.findViewById(R.id.layout_services);
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        final EditText editCountry = view.findViewById(R.id.editCountry);
        final EditText editState = view.findViewById(R.id.editState);
        final EditText editDistrict = view.findViewById(R.id.editDistrict);
        final EditText editCity = view.findViewById(R.id.editVillage);
        final Spinner spinner = view.findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(container.getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(adapter);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment()).commit();
            }
        });

        checkboxIsProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkboxIsProvider.isChecked()) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
            }
        });
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String pass = editPass.getText().toString();
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
                if (pass.isEmpty() || pass.length() < 8) {
                    editPass.setError("require min length 8");
                    return;
                }
                final User user = new User(name, email, phone, isProvider);
                Utils.setUser(Objects.requireNonNull(getContext()), user);

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

                }

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("", "onSuccess: ============================");
                        if (authResult.getUser() != null) {
                            if (isProvider) {
                                serviceProvider.setUID(authResult.getUser().getUid());
                                saveServiceDetail(authResult.getUser().getUid(), serviceProvider, user);
                            } else {
                                setUser(authResult.getUser().getUid(), user);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnCreateAccount.setVisibility(View.VISIBLE);
                    }
                });
                btnCreateAccount.setVisibility(View.GONE);
            }
        });

        arrayList.addAll(Utils.getCategories());
        adapter.notifyDataSetChanged();

        return view;
    }

    private void setUser(final String uid, User user) {
        FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getActivity().finish();
            }
        });
    }

    private void saveServiceDetail(final String UID, ServiceProvider serviceProvider, final User user) {
        FirebaseFirestore.getInstance().collection("serviceProviders").document(UID).
                set(serviceProvider).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setUser(UID, user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnCreateAccount.setVisibility(View.GONE);
                Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

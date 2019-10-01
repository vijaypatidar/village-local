package com.uni.localvillage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uni.localvillage.R;
import com.uni.localvillage.Utils;
import com.uni.localvillage.model.User;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText editEmail = view.findViewById(R.id.editEmail);
        final EditText editPass = view.findViewById(R.id.editPass);
        final Button btnLogin = view.findViewById(R.id.btnLogin);
        Button btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.nav_host_fragment, new CreateAccountFragment()).commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String pass = editPass.getText().toString();
                if (email.isEmpty()) {
                    editEmail.setError("require");
                    return;
                }
                if (pass.isEmpty()) {
                    editPass.setError("require");
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        getUser(Objects.requireNonNull(authResult.getUser()).getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        editPass.setError("wrong email or password!");
                        btnLogin.setVisibility(View.VISIBLE);
                    }
                });
                btnLogin.setVisibility(View.GONE);

            }
        });
        return view;
    }

    private void getUser(final String uid) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    Utils.setUser(Objects.requireNonNull(getContext()), user);
                }
                getActivity().finish();
            }
        });
    }
}

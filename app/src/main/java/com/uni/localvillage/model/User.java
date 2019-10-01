package com.uni.localvillage.model;

import com.google.firebase.firestore.DocumentReference;


public class User {
    private String name, email, phone;
    private boolean isProvider;
    private DocumentReference documentReference;

    public User(String name, String email, String phone, boolean isProvider) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isProvider = isProvider;
    }

    public User() {
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isProvider() {
        return isProvider;
    }

    public void setProvider(boolean provider) {
        isProvider = provider;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


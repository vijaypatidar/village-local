package com.uni.localvillage.model;

public class Booking {
    private String UID, name, phone, date;
    private int status;

    public Booking(String UID, String name, String phone, String date, int status) {
        this.UID = UID;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.status = status;
    }

    public Booking() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}

package com.uni.localvillage.model;

public class Request {
    public static final int STATUS_PENDIND = 1;
    public static final int STATUS_REJECT = 2;
    public static final int STATUS_ACCEPT = 3;
    private String UID, name, phone, date;
    private int status;

    public Request(String UID, String name, String phone, String date, int status) {
        this.UID = UID;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.status = status;
    }

    public Request() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

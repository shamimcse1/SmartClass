package com.codercamp.smartclass.model;

public class UserModel {
    private  String name,email,number,batch,password,date;

    public UserModel() {
    }

    public UserModel(String name, String email, String number, String batch, String password, String date) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.batch = batch;
        this.password = password;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getBatch() {
        return batch;
    }

    public String getPassword() {
        return password;
    }

    public String getDate() {
        return date;
    }
}

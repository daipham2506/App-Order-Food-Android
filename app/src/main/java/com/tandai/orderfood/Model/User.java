package com.tandai.orderfood.Model;

public class User {
    private String email;
    private String pass;
    private String name;
    private String phone;
    private String address;
    private  String userType;
    private  String image;
    public User(){
        //default
    }

    public User(String email, String pass, String name, String phone, String address, String type) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.userType = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}





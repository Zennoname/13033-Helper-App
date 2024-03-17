package com.example.uni13033;

public class User {

    String name, address;

    public User(String name, String address){
        this.name = name;
        this.address = address;
    }

    public User(){
        this.name = "";
        this.address = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

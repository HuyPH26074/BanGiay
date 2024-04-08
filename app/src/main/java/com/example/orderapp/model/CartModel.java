package com.example.orderapp.model;

import java.io.Serializable;

public class CartModel implements Serializable {

    String image;
    String name;
    String description;
    Long price;
    String address;
    String documentID;

    int amount;

    boolean isCheck;
    int id;
    int loaiGiay;
    int foodID;


    public CartModel() {
    }

    public CartModel(int id, String image, String name, String description, Long price, int amount, int loaiGiay,int foodID) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.loaiGiay = loaiGiay;
        this.foodID = foodID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getLoaiGiay() {
        return loaiGiay;
    }

    public void setLoaiGiay(int loaiGiay) {
        this.loaiGiay = loaiGiay;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }
}

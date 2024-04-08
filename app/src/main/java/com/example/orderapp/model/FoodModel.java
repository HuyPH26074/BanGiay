package com.example.orderapp.model;

import java.io.Serializable;

public class FoodModel implements Serializable {
    String image;
    String name;
    String description;
    Long price;
    int id;
    int loaiGiay;

    int soluong;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FoodModel() {
    }


    public FoodModel(String image, String name, String description, Long price, int loaiGiay) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.loaiGiay = loaiGiay;
    }

    public FoodModel(String image, String name, String description, Long price, int id, int loaiGiay) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.id = id;
        this.loaiGiay = loaiGiay;
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

    public int getLoaiGiay() {
        return loaiGiay;
    }

    public void setLoaiGiay(int loaiGiay) {
        this.loaiGiay = loaiGiay;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}

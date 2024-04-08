package com.example.orderapp.model;

import java.util.List;

public class OrderHistoryModel {
    String documentID;
    String dateOrder;
    String totalPrice;
    int id;
    int phuongthucthanhtoan;
    List<CartModel> foods;
    String diachinhanhang;

    int trangthaidonhang;

    public OrderHistoryModel() {
    }

    public OrderHistoryModel(int id, int phuongthucthanhtoan, String dateOrder, String diachinhanhang, List<CartModel> foods, int trangthaidonhang) {
        this.id = id;
        this.dateOrder = dateOrder;
        this.foods = foods;
        this.phuongthucthanhtoan = phuongthucthanhtoan;
        this.diachinhanhang = diachinhanhang;
        this.trangthaidonhang = trangthaidonhang;
    }

    public String getDocumentID() {
        return documentID;
    }

    public int getId() {
        return id;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartModel> getFoods() {
        return foods;
    }

    public void setFoods(List<CartModel> foods) {
        this.foods = foods;
    }

    public int getPhuongthucthanhtoan() {
        return phuongthucthanhtoan;
    }

    public void setPhuongthucthanhtoan(int phuongthucthanhtoan) {
        this.phuongthucthanhtoan = phuongthucthanhtoan;
    }

    public String getDiachinhanhang() {
        return diachinhanhang;
    }

    public void setDiachinhanhang(String diachinhanhang) {
        this.diachinhanhang = diachinhanhang;
    }

    public int getTrangthaidonhang() {
        return trangthaidonhang;
    }

    public void setTrangthaidonhang(int trangthaidonhang) {
        this.trangthaidonhang = trangthaidonhang;
    }
}

package com.example.giovanni.baoovero;

public class Portrait_Dog {
    private String dogName;
    private int dogImage;

    public Portrait_Dog(String dogName, int dogImage) {
        this.dogName = dogName;
        this.dogImage = dogImage;
    }

    public String getDogName() {
        return dogName;
    }

    public int getDogImage() {
        return dogImage;
    }
}
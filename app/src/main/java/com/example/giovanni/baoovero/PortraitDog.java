package com.example.giovanni.baoovero;

public class PortraitDog {
    private String dogName;
    private int dogImage;


    public PortraitDog() {
    }
    public PortraitDog(String dogName, int dogImage) {
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
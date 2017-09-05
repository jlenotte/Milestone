package com.ovh.milestone.conversion;

public class ForexRate {

    // Attributes
    private String date;
    private Double forex;



    // Constructor
    public ForexRate() {
        this.date = null;
        this.forex = null;
    }



    // Constructor
    public ForexRate(String date, Double forex) {
        this.date = date;
        this.forex = forex;
    }



    // Getters & Setters
    public String getDate() {
        return date;
    }



    public void setDate(String date) {
        this.date = date;
    }



    public Double getForex() {
        return forex;
    }



    public void setForex(Double forex) {
        this.forex = forex;
    }
}

package com.example.financeapp;

import java.util.HashMap;

public class Data {
    private double price;
    private String date;
    public Data(double price, String date)
    {
        this.date =date;
        this.price = price;
    }

    @Override
    public String toString()
    {
        return (  " DATE: " + date + " PRICE: " + price);
    }
    public static int getMonth(String x)
    {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Jan", 1);

        map.put("Feb", 2);

        map.put("Mar", 3);

        map.put("Apr", 4);
        map.put("May", 5);
        map.put("Jun", 6);
        map.put("Jul", 7);
        map.put("Aug", 8);
        map.put("Sep", 9);
        map.put("Oct", 10);
        map.put("Nov", 11);
        map.put("Dec", 12);
        return map.get(x);
    }
}

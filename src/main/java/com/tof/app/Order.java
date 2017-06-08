package com.tof.app;

public class Order implements Comparable<Order> {

    private String id;
    private int start;
    private int duration;
    private double price;

    public static final String CSV_SEPARATOR = ";";

    public Order(String csvLine) {
        String[] champs = csvLine.split(CSV_SEPARATOR);
        this.id = champs[0];
        this.start = Integer.parseInt(champs[1]);
        this.duration = Integer.parseInt(champs[2]);
        this.price = Double.parseDouble(champs[3]);
    }

    public Order(String id, int start, int duration, double price) {
        this.id = id;
        this.start = start;
        this.duration = duration;
        this.price = price;
    }

    public int compareTo(Order other) {
        return this.start - other.getStart();
    }

    public String toCsvLine() {
        return String.join(
                CSV_SEPARATOR,
                this.getId(), Integer.toString(this.getStart()),
                Integer.toString(this.getDuration()), Double.toString(this.getPrice()));
    }

    public String getId() {
        return this.id;
    }

    public int getStart() {
        return this.start;
    }

    public int getDuration() {
        return this.duration;
    }

    public double getPrice() {
        return this.price;
    }

}

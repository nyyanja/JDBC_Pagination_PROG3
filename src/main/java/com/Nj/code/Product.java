package com.Nj.code;

import java.time.Instant;

public class Product {
    private final int id;
    private final String name;
    private final double price;
    private final Instant creationDatetime;

    public Product(int id, String name, double price, Instant creationDatetime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationDatetime = creationDatetime;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Instant getCreationDatetime() { return creationDatetime; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price +
                ", creation=" + creationDatetime + "}";
    }
}


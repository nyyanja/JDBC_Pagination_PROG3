package com.Nj;

import com.Nj.code.Category;
import com.Nj.code.DataRetriever;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();

        System.out.println("=== All categories ===");
        List<Category> cats = dr.getAllCategories();
        cats.forEach(System.out::println);

        System.out.println("\n=== First page of products (page 1, size 5) ===");
        dr.getProductList(1, 5).forEach(System.out::println);

        System.out.println("\n=== Filter: product contains 'Laptop' ===");
        dr.getProductsByCriteria("Laptop", null, null, null).forEach(System.out::println);

        System.out.println("\n=== Filter: category contains 'Television' ===");
        dr.getProductsByCriteria(null, "Television", null, null).forEach(System.out::println);

        System.out.println("\n=== Filter + date range (after 2024-02-01) ===");
        Instant after = LocalDateTime.of(2024,2,1,0,0).toInstant(ZoneOffset.UTC);
        dr.getProductsByCriteria(null, null, after, null).forEach(System.out::println);

        System.out.println("\n=== Filter + pagination: Smartphone in page 1 size 2 ===");
        dr.getProductsByCriteria(null, "Smartphone", null, null, 1, 2).forEach(System.out::println);
    }
}

package com.Nj.code;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final DBConnection db;

    public DataRetriever() {
        this.db = new DBConnection();
    }

    // Lire les catégories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM product_category";

        try (Connection conn = db.getDBConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Pagination: lire liste de produits
    public List<Product> getProductList(int page, int size) {
        List<Product> products = new ArrayList<>();
        int offset = (Math.max(1, page) - 1) * Math.max(1, size); // sécurité: page>=1, size>=1

        String sql = "SELECT id, name, price, creation_datetime FROM product ORDER BY id LIMIT ? OFFSET ?";

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // 3) Filtre sans pagination
    public List<Product> getProductsByCriteria(String productName,
                                               String categoryName,
                                               Instant creationMin,
                                               Instant creationMax) {
        List<Product> products = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.id, p.name, p.price, p.creation_datetime " +
                        "FROM product p " +
                        "JOIN product_category c ON p.id = c.product_id " +
                        "WHERE 1=1 "
        );

        if (productName != null) sql.append("AND p.name ILIKE ? ");
        if (categoryName != null) sql.append("AND c.name ILIKE ? ");
        if (creationMin != null) sql.append("AND p.creation_datetime >= ? ");
        if (creationMax != null) sql.append("AND p.creation_datetime <= ? ");

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (productName != null) ps.setString(index++, "%" + productName + "%");
            if (categoryName != null) ps.setString(index++, "%" + categoryName + "%");
            if (creationMin != null) ps.setTimestamp(index++, Timestamp.from(creationMin));
            if (creationMax != null) ps.setTimestamp(index++, Timestamp.from(creationMax));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // 4) Filtre + pagination : on filtre d'abord, puis on applique LIMIT/OFFSET
    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax,
            int page,
            int size) {

        List<Product> products = new ArrayList<>();
        int safePage = Math.max(1, page);
        int safeSize = Math.max(1, size);
        int offset = (safePage - 1) * safeSize;

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.id, p.name, p.price, p.creation_datetime " +
                        "FROM product p " +
                        "JOIN product_category c ON p.id = c.product_id " +
                        "WHERE 1=1 "
        );

        if (productName != null) sql.append("AND p.name ILIKE ? ");
        if (categoryName != null) sql.append("AND c.name ILIKE ? ");
        if (creationMin != null) sql.append("AND p.creation_datetime >= ? ");
        if (creationMax != null) sql.append("AND p.creation_datetime <= ? ");

        sql.append("ORDER BY p.id ");
        sql.append("LIMIT ? OFFSET ?");

        try (Connection conn = db.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (productName != null) ps.setString(index++, "%" + productName + "%");
            if (categoryName != null) ps.setString(index++, "%" + categoryName + "%");
            if (creationMin != null) ps.setTimestamp(index++, Timestamp.from(creationMin));
            if (creationMax != null) ps.setTimestamp(index++, Timestamp.from(creationMax));

            ps.setInt(index++, safeSize);
            ps.setInt(index, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    // convertir ResultSet en Product
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        Timestamp ts = rs.getTimestamp("creation_datetime");
        Instant created = ts != null ? ts.toInstant() : null;
        return new Product(id, name, price, created);
    }
}


package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import services.MySQLConnection;

public class Manager {
    private String username;
    private String password;
    private static Connection connection;


    public Manager(String username, String password) {
        this.username = username;
        this.password = password;
        try {
            connection = new MySQLConnection().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public List<Manager> getUsers() {
        List<Manager> users = new ArrayList<>();
        String sql = "SELECT id, username, password FROM User";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Manager manager = new Manager(
                    rs.getString("username"),
                    rs.getString("password")
                );
                users.add(manager);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static int getNextProductId() {
        String sqlMaxId = "SELECT COALESCE(MAX(id), 0) FROM products";
        try (PreparedStatement stmt = connection.prepareStatement(sqlMaxId);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; 
    }

    public void addDigitalVideoDisc(DVD dvd) {
        String sqlInsert = "INSERT INTO products (id, title, category, director, length, cost) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            int newId = getNextProductId();
            try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert)) {
                stmtInsert.setInt(1, newId);
                stmtInsert.setString(2, dvd.getTitle());
                stmtInsert.setString(3, dvd.getCategory());
                stmtInsert.setString(4, dvd.getDirector());
                stmtInsert.setDouble(5, dvd.getLength());
                stmtInsert.setDouble(6, dvd.getCost());
                stmtInsert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeProduct(String productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

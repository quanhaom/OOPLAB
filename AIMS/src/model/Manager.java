package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import services.MySQLConnection;
import services.JsonParser;
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


    public void addMedia(DVD dvd) {
        String sqlInsert = "INSERT INTO products (id, title, category, director, length, cost) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            String newId = Media.getNextmediaId("DVD");
            try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert)) {
                stmtInsert.setString(1, newId);
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

}

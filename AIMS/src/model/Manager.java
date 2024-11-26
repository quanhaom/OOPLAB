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
    public void addMedia(Book book) {
        String sqlInsert = "INSERT INTO books (id, title, category, authors, cost) VALUES (?, ?, ?, ?, ?)";
        try {
            String newId = Media.getNextmediaId("B");

            String authorsJson = JsonParser.toJsonArray(book.getAuthors());
            try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert)) {
                stmtInsert.setString(1, newId);
                stmtInsert.setString(2, book.getTitle());
                stmtInsert.setString(3, book.getCategory());
                stmtInsert.setString(4, authorsJson);
                stmtInsert.setDouble(5, book.getCost());
                stmtInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addMedia(CD cd) {
        String sqlInsert = "INSERT INTO cd (id, title, category, price, artist, director) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            String newId = Media.getNextmediaId("CD");
            try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert)) {
                stmtInsert.setString(1, newId);
                stmtInsert.setString(2, cd.getTitle());
                stmtInsert.setString(3, cd.getCategory());
                stmtInsert.setDouble(4, cd.getCost());
                stmtInsert.setString(5, cd.getArtists());
                stmtInsert.setString(6, cd.getDirector());
                stmtInsert.executeUpdate();
            }

            addTracks(newId, cd.getTracks());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addTracks(String cdId, List<Track> tracks) {
        String sqlInsertTrack = "INSERT INTO track (cd_id, title, length) VALUES (?, ?, ?)";

        try (PreparedStatement stmtInsertTrack = connection.prepareStatement(sqlInsertTrack)) {
            for (Track track : tracks) {
                stmtInsertTrack.setString(1, cdId);
                stmtInsertTrack.setString(2, track.getTitle()); 
                stmtInsertTrack.setInt(3, track.getLength()); 
                stmtInsertTrack.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeMedia(String mediaId) {
    	String db = mediaId.startsWith("DVD") ? "products" : 
            (mediaId.startsWith("B") ? "books" : 
            (mediaId.startsWith("CD") ? "cd" : ""));
        String sql = "DELETE FROM " + db + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mediaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void removeTrack(JTable trackTable) {
        int selectedRow = trackTable.getSelectedRow();
        if (selectedRow != -1) {
            String trackId = (String) trackTable.getValueAt(selectedRow, 0); 
            int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to remove the selected track?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
            );
            if (confirmation == JOptionPane.YES_OPTION) {
                boolean success = Track.removeTrack(trackId);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Track removed successfully.");
                    ((DefaultTableModel) trackTable.getModel()).removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to remove track.");
                }
            }
        } else {
}

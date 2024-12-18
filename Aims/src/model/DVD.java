package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import frame.PlaybackDialog;
import services.MySQLConnection;

public class DVD extends Media implements Playable{
	 private String director;
	 private int length;
	 private static Connection connection;
	    public DVD() {
	        this.director = "";
	        this.length = 0;
	        try {
	            connection = new MySQLConnection().getConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public DVD(String id, String title, String category, String director,int length,double cost) {
		 super(id,title,category,cost);
		 this.director = director;
		 this.length = length;
	 }
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
    public List<DVD> getDVDs() {
        List<DVD> dvds = new ArrayList<>();
        String sql = "SELECT * FROM products"; 

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DVD dvd = new DVD();
                dvd.setId(rs.getString("id"));
                dvd.setTitle(rs.getString("title"));
                dvd.setCategory(rs.getString("category"));
                dvd.setDirector(rs.getString("director"));
                dvd.setLength(rs.getInt("length"));
                dvd.setCost(rs.getFloat("cost"));
                dvds.add(dvd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dvds;
    }

    public static DVD getDVDById(String id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        DVD dvd = new DVD();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    dvd.setId(rs.getString("id"));
                    dvd.setTitle(rs.getString("title"));
                    dvd.setCategory(rs.getString("category"));
                    dvd.setDirector(rs.getString("director"));
                    dvd.setLength(rs.getInt("length"));
                    dvd.setCost(rs.getFloat("cost"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return dvd;     
}	
    @Override
    public void play(String Id) {
    	DVD dvd = getDVDById(Id);
    	int totalLength = dvd.getLength()*60;
    	new PlaybackDialog(dvd.getTitle(),totalLength);
    }
}

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import services.MySQLConnection;

public class DVD {
	 private String id;
	private String title;
	 private String category;
	 private String director;
	 private double length;
	 private double cost;
	 private Connection connection;
	    public DVD() {
	        this.id = "";
	        this.title = "";
	        this.category = "";
	        this.director = "";
	        this.length = 0.0;
	        this.cost = 0.0;
	        try {
	            connection = new MySQLConnection().getConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public DVD(String id, String title, String category, String director,double length,double cost) {
		 this.id=id;
		 this.title = title;
		 this.category = category;
		 this.director = director;
		 this.length = length;
		 this.cost = cost;
	 }
	public String getTitle() {
		return title;
	}
	 public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
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
                dvd.setLength(rs.getFloat("length"));
                dvd.setCost(rs.getFloat("cost"));
                dvds.add(dvd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dvds;
    }

    public DVD getDVDById(String dvdId) {
        DVD dvd = null; 
        String sql = "SELECT * FROM products WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dvdId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { 
                    dvd = new DVD();
                    dvd.setId(rs.getString("id"));
                    dvd.setTitle(rs.getString("title"));
                    dvd.setCategory(rs.getString("category"));
                    dvd.setDirector(rs.getString("director"));
                    dvd.setLength(rs.getFloat("length"));
                    dvd.setCost(rs.getFloat("cost"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dvd; 
    }

    public List<DVD> searchByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ?";
        return searchProductsByAttribute(sql, category);
    }

    public List<DVD> searchByLength(int length) {
        String sql = "SELECT * FROM products WHERE length = ?";
        return searchProductsByAttribute(sql, String.valueOf(length));
    }

    public List<DVD> searchByCost(double cost) {
        String sql = "SELECT * FROM products WHERE cost = ?";
        return searchProductsByAttribute(sql, String.valueOf(cost));
    }

    public List<DVD> searchByDirector(String director) {
        String sql = "SELECT * FROM products WHERE director = ?";
        return searchProductsByAttribute(sql, director);
    }

    private List<DVD> searchProductsByAttribute(String sql, String parameter) {
        List<DVD> productList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, parameter);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DVD dvd = extractProductFromResultSet(rs);
                productList.add(dvd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    private DVD extractProductFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String title = rs.getString("title");
        String category = rs.getString("category");
        double length = rs.getDouble("length");
        double cost = rs.getDouble("cost");
        String director = rs.getString("director");

        return new DVD(id, title, category, director, length, cost);
    }

	
}

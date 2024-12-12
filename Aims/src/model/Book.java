package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import services.JsonParser;
import services.MySQLConnection;

public class Book extends Media{
	 private List<String> authors;
	 private static Connection connection;
	    public Book() {
	        this.authors = new ArrayList<String>();
	        try {
	            connection = new MySQLConnection().getConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 public Book(String id, String title, String category,double cost,List<String> authors) {
		 super(id,title,category,cost);
		 this.authors = authors;
	 }


	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public Connection getConnection() {
		return connection;
	}
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getString("id"));
                book.setTitle(rs.getString("title"));
                book.setCategory(rs.getString("category"));
                book.setCost(rs.getDouble("cost"));

                String authorsJson = rs.getString("authors"); 
                List<String> authors = JsonParser.parseJsonArray(authorsJson); 
                book.setAuthors(authors);

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
    public static Book getBookById(String id) {
            String sql = "SELECT * FROM books WHERE id = ?";
            Book book = new Book();
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {

                        book.setId(rs.getString("id"));
                        book.setTitle(rs.getString("title"));
                        book.setCategory(rs.getString("category"));
                        
                        String authorsJson = rs.getString("authors"); 
                        List<String> authors = JsonParser.parseJsonArray(authorsJson); 
                        book.setAuthors(authors);
                        book.setCost(rs.getFloat("cost"));

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
			return book;     
    }
}

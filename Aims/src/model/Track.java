package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import frame.PlaybackDialog;
import services.MySQLConnection;

public class Track implements Playable {
    private String title;
    private String id ;
    private int length; 
    private static Connection connection;
    public Track() {
    	this.title="";
    	this.id="";
    	this.length=0;
    }

    public Track(String id,String title, int length) {
        this.title = title;
        this.id = id;
        this.length = length;
        try {
            connection = new MySQLConnection().getConnection();
        } catch (SQLException e) {
                 e.printStackTrace();
        }
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

	public void setLength(int length) {
		this.length = length;
	}

	public String getTitle() {
        return title;
    }

    public int getLength() {
        return length;
    }

    public String getFormattedLength() {
        int minutes = length / 60;
        int seconds = length % 60;
        return minutes + ":" + String.format("%02d", seconds);
    }    
	public static Connection getConnection() {
		return connection;
	}

    
    @Override
    public void play(String id) {
    	String sql = "SELECT * FROM track WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    String cdId =(rs.getString("cd_id"));
                    CD cd = CD.getCDById(cdId);
                    String title = (rs.getString("title"));
                    int length=(rs.getInt("length"));
                    String newtitle = title + "( " + cd.getTitle() + " )";
                    new PlaybackDialog(newtitle, length);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Track getTrackById(String id) {
        String sql = "SELECT * FROM track WHERE id = ?";
        Track track = new Track();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    track.setId(rs.getString("id"));
                    track.setTitle(rs.getString("title"));
                    track.setLength(rs.getInt("length"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return track;     
}
    public static boolean addTrackToCD(String cdId, String trackId, String title, int length) {
        String sql = "INSERT INTO track (id, title, length, cd_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, trackId);
            stmt.setString(2, title);
            stmt.setInt(3, length);
            stmt.setString(4, cdId);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean removeTrack(String trackId) {
        String sql = "DELETE FROM track WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, trackId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Track)) return false;
        Track other = (Track) obj;
        return super.equals(other) && this.length == other.length;
    }


    @Override
    public String toString() {
        return title + " (" + getFormattedLength() + ")";
    }
    
}

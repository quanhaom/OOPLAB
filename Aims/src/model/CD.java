package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import frame.PlaybackDialog;
import services.MySQLConnection;

public class CD extends Media implements Playable {
    private List<Track> tracks;
    private String artists;
    private String director;
    private static Connection connection;

    public CD() {
        this.tracks = new ArrayList<>();
        this.artists = "";
        this.director = "";
        try {
            connection = new MySQLConnection().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CD(String id, String title, String category, String director, String artists, double cost, List<Track> tracks) {
        super(id, title, category, cost);
        this.tracks = tracks;
        this.artists = artists;
        this.director = director;
    }

    public List<Track> getTracks() {
        return tracks;
    }
    public String getTotalLength() {
        int totalLength = 0;
        for (Track track : tracks) {
            totalLength += track.getLength()
            		;
        }
        int minutes = totalLength / 60;
        int seconds = totalLength % 60;
        return minutes + ":" + String.format("%02d", seconds);
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Connection getConnection() {
        return connection;
    }


    public List<CD> getCDs() {
        List<CD> cds = new ArrayList<>();
        String sql = "SELECT * FROM cd";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CD cd = new CD();
                cd.setId(rs.getString("id"));
                cd.setTitle(rs.getString("title"));
                cd.setCategory(rs.getString("category"));
                cd.setCost(rs.getDouble("price"));
                cd.setArtists(rs.getString("artist"));
                cd.setDirector(rs.getString("director"));
                cd.setTracks(getTracksByCDId(cd.getId()));

                cds.add(cd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cds;
    }

    public static List<Track> getTracksByCDId(String cdId) {
        List<Track> tracks = new ArrayList<>();
        String sql = "SELECT * FROM track WHERE cd_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cdId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Track track = new Track(null,null,0);
                    track.setId(rs.getString("id"));
                    track.setTitle(rs.getString("title"));
                    track.setLength(rs.getInt("length"));
                    tracks.add(track);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }

    public static CD getCDById(String id) {
        String sql = "SELECT * FROM cd WHERE id = ?";
        CD cd = new CD();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    cd.setId(rs.getString("id"));
                    cd.setTitle(rs.getString("title"));
                    cd.setCategory(rs.getString("category"));
                    cd.setCost(rs.getDouble("price"));
                    cd.setArtists(rs.getString("artist"));
                    cd.setDirector(rs.getString("director"));
                    cd.setTracks(getTracksByCDId(id));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return cd;     
    }
    @Override
    public void play(String Id) {
    	CD cd = getCDById(Id);
        List<Track> tracks = getTracksByCDId(Id);
        playTrack(tracks, 0,cd.getTitle());
    }

    private void playTrack(List<Track> tracks, int index,String cdname) {
        if (index < tracks.size()) {
            Track track = tracks.get(index);

            String trackName = track.getTitle();
            trackName = trackName + "( " + cdname + " )";
            int trackLength = track.getLength(); 
            PlaybackDialog playbackDialog = new PlaybackDialog(trackName, trackLength);

            // Start a new thread to check if the track has finished playing
            new Thread(() -> {
                while (!playbackDialog.isComplete()) {
                    try {
                        Thread.sleep(100); // Check every 100ms if the track has finished
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Once the track is complete, play the next track
                playTrack(tracks, index + 1,cdname);
            }).start();

        } else {
            // All tracks have finished
            System.out.println("All tracks completed!");
        }
    }


}

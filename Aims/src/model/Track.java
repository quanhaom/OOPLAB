package model;

public class Track {
    private String title;
    private String id ;
    private int length; // Length in seconds

    // Constructor
    public Track(String id,String title, int length) {
        this.title = title;
        this.id = id;
        this.length = length;
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

    @Override
    public String toString() {
        return title + " (" + getFormattedLength() + ")";
    }
}
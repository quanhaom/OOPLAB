package passingparameters;



public class DVD {
	 private String id;
	private String title;
	 private String category;
	 private String director;
	 private double length;
	 private double cost;
	    public DVD() {
	        this.id = "";
	        this.title = "";
	        this.category = "";
	        this.director = "";
	        this.length = 0.0;
	        this.cost = 0.0;}
	 
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
    public String toString() {
        return "DigitalVideoDisc{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", director='" + director + '\'' +
                ", length=" + length +
                ", cost=" + cost +
                '}';
    }
}
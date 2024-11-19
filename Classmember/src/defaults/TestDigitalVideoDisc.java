package defaults;

public class TestDigitalVideoDisc {
    public static void main(String[] args) {
        DigitalVideoDisc dvd1 = new DigitalVideoDisc("Movie A", "Action", "Director A", 120, 19.99f);
        DigitalVideoDisc dvd2 = new DigitalVideoDisc("Movie B", "Drama", "Director B", 90, 14.99f);
        
        System.out.println(dvd1);
        System.out.println(dvd2);
        
        System.out.println("Total DVDs created: " + DigitalVideoDisc.getNbDigitalVideoDiscs());
    }
}

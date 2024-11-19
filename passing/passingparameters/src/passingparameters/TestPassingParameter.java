package passingparameters;

public class TestPassingParameter {

    public static void main(String[] args) {
        DVD jungleDVD = new DVD(null, "Jungle", null, null, 0, 0);
        DVD cinderellaDVD = new DVD(null, "Cinderella", null, null, 0, 0);

        DVD[] dvds = {jungleDVD, cinderellaDVD};

        swap(dvds);
        System.out.println("Swap:");
        System.out.println("jungle dvd title: " + dvds[0].getTitle());
        System.out.println("cinderella dvd title: " + dvds[1].getTitle());

        changeTitle(dvds[0], dvds[1].getTitle());
        System.out.println("changeTitle:");
        System.out.println("jungle dvd title: " + dvds[0].getTitle());
    }

    public static void swap(DVD[] dvds) {
        DVD tmp = dvds[0];
        dvds[0] = dvds[1];
        dvds[1] = tmp;
    }

    public static void changeTitle(DVD dvd, String title) {
        String oldTitle = dvd.getTitle();
        dvd.setTitle(title);
        dvd = new DVD(null, oldTitle, null, null, 0, 0);
    }
}

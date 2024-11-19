package frame;

import model.DVD;

public class Main {
    public static void main(String[] args) {
        DVD dvd = new DVD();
        RoleSelectionFrame roleSelectionFrame = new RoleSelectionFrame(dvd);
        roleSelectionFrame.setVisible(true);
        TestPassingParameter.Main(args);
    }
}

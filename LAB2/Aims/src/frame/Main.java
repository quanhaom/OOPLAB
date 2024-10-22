package frame;

import services.Store;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();

        // Show RoleSelectionFrame first
        RoleSelectionFrame roleSelectionFrame = new RoleSelectionFrame(store);
        roleSelectionFrame.setVisible(true);
    }
}

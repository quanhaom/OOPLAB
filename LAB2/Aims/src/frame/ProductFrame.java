package frame;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.DVD;
import model.Manager;

public class ProductFrame extends BaseFrame {
	private Manager manager = new Manager(null,null);
    protected static final int WIDTH = 1385;
    protected static final int HEIGHT = 900;
    

    public ProductFrame(DVD dvd) {
        super(dvd);
        setSize(WIDTH, HEIGHT);
        setTitle("Employee Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel buttonPanel = new JPanel();
        JButton addProductButton = new JButton("Add Product");
        JButton backButton = new JButton("Back");
        JButton removeProductButton = new JButton("Remove Product");

        buttonPanel.add(addProductButton);
        buttonPanel.add(backButton);
        buttonPanel.add(removeProductButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.SOUTH);

        addProductButton.addActionListener(e -> addProduct());
        backButton.addActionListener(e -> back());
        removeProductButton.addActionListener(e -> removeProduct());

        loadProducts();
    }
    private void addProduct() {
        String title = JOptionPane.showInputDialog(this, "Enter DVD Title:");
        String category = JOptionPane.showInputDialog(this, "Enter DVD Category:");
        String director = JOptionPane.showInputDialog(this, "Enter DVD Director:");
        String lengthStr = JOptionPane.showInputDialog(this, "Enter DVD Length (in minutes):");
        String costStr = JOptionPane.showInputDialog(this, "Enter DVD Cost:");

        if (title != null && category != null && director != null && lengthStr != null && costStr != null ) {
            try {
                double length = Integer.parseInt(lengthStr);
                double cost = Double.parseDouble(costStr);                
                int id = Manager.getNextProductId();

                DVD dvd = new DVD(String.valueOf(id), title, category, director, length, cost);
                
                manager.addProduct(dvd);
                loadProducts();
                JOptionPane.showMessageDialog(this, "DVD added successfully.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input for length, cost, or quantity.");
            }
        }
    }
    private void removeProduct() {
        int selectedRow = productTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to remove.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 2);

        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + name + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            manager.removeProduct(id);
            loadProducts();
            JOptionPane.showMessageDialog(this, name + " removed successfully.");
        }
    }

    private void back() {
    	DVD dvd = new DVD();
        ManagerFrame managerFrame = new ManagerFrame(dvd);
		managerFrame.setVisible(true);
        dispose();
    }

    private void loadProducts() {
        displayAllProducts();
    }

}

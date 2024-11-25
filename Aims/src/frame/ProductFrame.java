package frame;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Book;
import model.CD;
import model.DVD;
import model.Track;
import model.Manager;
import services.JsonParser;

public class ProductFrame extends BaseFrame {
	private Manager manager = new Manager(null,null);
    

    public ProductFrame(DVD dvd,Book book,CD cd) {
        super(dvd,book,cd);
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

        addProductButton.addActionListener(e -> addMedia());
        backButton.addActionListener(e -> back());
        removeProductButton.addActionListener(e -> removeProduct());

        displayAllProducts();
    }
    private void addMedia() {
        String selectedProductType = (String) productTypeComboBox.getSelectedItem();
        String title = JOptionPane.showInputDialog(this, "Enter Title:");
        
        if (title == null) return;

        String category = JOptionPane.showInputDialog(this, "Enter Category:");
        if (category == null) return; 

        String costStr = JOptionPane.showInputDialog(this, "Enter Cost:");
        if (costStr == null) return;
        
        if (title != null && category != null && costStr != null) {
            try {
                double cost = Double.parseDouble(costStr);

                if ("DVD".equals(selectedProductType)) {
                    String director = JOptionPane.showInputDialog(this, "Enter Director:");
                    if (director == null) return;
                    
                    String lengthStr = JOptionPane.showInputDialog(this, "Enter Length (in minutes):");
                    if (lengthStr == null) return; 

                    double length = Double.parseDouble(lengthStr);
                    DVD dvd = new DVD("", title, category, director, length, cost);
                    manager.addMedia(dvd);
                    displayAllProducts();
                    JOptionPane.showMessageDialog(this, "DVD added successfully.");

                // Book
                } else if ("Book".equals(selectedProductType)) {
                    String authorsJson = JOptionPane.showInputDialog(this, "Enter Authors (comma separated):");
                    if (authorsJson == null) return;

                    if (authorsJson != null && !authorsJson.trim().isEmpty()) {
                        List<String> authors = JsonParser.parseJsonArray(authorsJson);
                        Book book = new Book("", title, category, cost, authors);
                        manager.addMedia(book);
                        displayAllProducts();
                        JOptionPane.showMessageDialog(this, "Book added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Authors input is missing.");
                    }

                // CD
                } else if ("CD".equals(selectedProductType)) {
                    String director = JOptionPane.showInputDialog(this, "Enter Director:");
                    if (director == null) return; 
                    
                    String artist = JOptionPane.showInputDialog(this, "Enter Artist:");
                    if (artist == null) return;

                    String tracksStr = JOptionPane.showInputDialog(this, "Enter Track Titles and Lengths (comma separated, e.g., Track1:180,Track2:240):");
                    if (tracksStr == null) return; 

                    if (director != null && artist != null && tracksStr != null && !tracksStr.trim().isEmpty()) {
                        List<Track> tracks = new ArrayList<>();
                        String[] trackDetails = tracksStr.split(",");
                        for (String trackDetail : trackDetails) {
                            String[] trackParts = trackDetail.split(":");

                            if (trackParts.length == 2) {
                                try {
                                    String trackTitle = trackParts[0].trim();
                                    int trackLength = Integer.parseInt(trackParts[1].trim());

                                    if (!trackTitle.isEmpty()) {
                                        Track track = new Track(String.valueOf(tracks.size() + 1), trackTitle, trackLength);
                                        tracks.add(track);
                                    } else {
                                        JOptionPane.showMessageDialog(this, "Track title is empty.");
                                        return;
                                    }
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(this, "Invalid track length. Please enter a valid number.");
                                    return;
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "Invalid format for track entry. Please use the format: Title:Length");
                                return;
                            }
                        }

                        CD cd = new CD("", title, category, director, artist, cost, tracks);
                        manager.addMedia(cd);
                        displayAllProducts();
                        JOptionPane.showMessageDialog(this, "CD added successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Artist or Track Titles are missing.");
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input for cost.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Title, Category, or Cost is missing.");
        }
    }

        
    private void removeProduct() {
        int selectedRow = productTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to remove.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove " + name + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            manager.removeProduct(id);
            displayAllProducts();
            JOptionPane.showMessageDialog(this, name + " removed successfully.");
        }
    }

    private void back() {
        ManagerFrame managerFrame = new ManagerFrame(dvd,book,cd);
		managerFrame.setVisible(true);
        dispose();
    }


}
package frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Book;
import model.CD;
import model.DVD;
import model.Track;
import model.Manager;
import services.JsonParser;

public class ProductFrame extends BaseFrame {
	private Manager manager = new Manager(null,null);
    
    public ProductFrame(DVD dvd,Book book,CD cd ) {
        super(dvd,book,cd);
        setTitle("Employee Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel buttonPanel = new JPanel();
        JButton addProductButton = new JButton("Add Product");
        JButton backButton = new JButton("Back");
        JButton removeProductButton = new JButton("Remove Product");
        JButton addTrackButton = new JButton("Add Track");

        buttonPanel.add(addTrackButton);
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
        addTrackButton.addActionListener(e -> addTrack());
        
        addTrackButton.setVisible(false);

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

                    int length = Integer.parseInt(lengthStr);

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
    
	
	
	public void viewTrack() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String Id = (String) tableModel.getValueAt(selectedRow, 0);
            List<Track> tracks = CD.getTracksByCDId(Id);
            CD cd = CD.getCDById(Id);

            String[] columns = {"Track Id", "Title", "Length", "CD name"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

            for (Track track : tracks) {
                Object[] row = {track.getId(), track.getTitle(), track.getFormattedLength(), cd.getTitle()};
                tableModel.addRow(row);
            }

            JTable table = new JTable(tableModel);

            JFrame frame = new JFrame("CD Tracks");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close this frame
            frame.setSize(600, 300);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            JButton button1 = new JButton("Remove Track");
            button1.addActionListener(e -> Manager.removeTrack(table));

            buttonPanel.add(button1);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            frame.add(panel);
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a valid CD to view.");
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
            manager.removeMedia(id);
            displayAllProducts();
            JOptionPane.showMessageDialog(this, name + " removed successfully.");
        }
    }
    private void addTrack() {
        int selectedRow = productTable.getSelectedRow(); // productTable is the JTable showing CDs
        if (selectedRow != -1) {
            String cdId = (String) tableModel.getValueAt(selectedRow, 0); // Get the selected CD's ID

            // Prompt for track details
            String trackId = JOptionPane.showInputDialog("Enter Track ID:");
            if (trackId == null || trackId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Track ID cannot be empty.");
                return;
            }

            String title = JOptionPane.showInputDialog("Enter Track Title:");
            if (title == null || title.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Track title cannot be empty.");
                return;
            }

            String lengthStr = JOptionPane.showInputDialog("Enter Track Length (in seconds):");
            int length;
            try {
                length = Integer.parseInt(lengthStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid length. Please enter a number.");
                return;
            }

            boolean success = Track.addTrackToCD(cdId, trackId, title, length);
            if (success) {
                JOptionPane.showMessageDialog(this, "Track added successfully to CD: " + cdId);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add track to CD: " + cdId);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a CD to add a track.");
        }
    }



    private void back() {
        ManagerFrame managerFrame = new ManagerFrame(dvd,book,cd);
		managerFrame.setVisible(true);
        dispose();
    }


}

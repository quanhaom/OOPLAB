package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.DVD;
import model.Track;
import model.Book;
import model.CD;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseFrame extends JFrame {
    protected static JTable productTable;
    protected static DefaultTableModel tableModel;
    protected JTextField searchField;
    protected JLabel suggestionLabel;
    protected JLabel timeLabel;
    protected JComboBox<String> productTypeComboBox;
    protected DVD dvd;
    protected Book book;
    protected CD cd;
    private JButton playButton;
    private JButton viewTrack;

    protected static final int WIDTH = 900;
    protected static final int HEIGHT = 650;

    public BaseFrame(DVD dvd, Book book, CD cd ) {
        this.dvd = dvd;
        this.book = book;
        this.cd = cd;

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Search:"), gbc);

        gbc.gridx = 1;
        searchField = new JTextField(20);
        searchPanel.add(searchField, gbc);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel productTypeLabel = new JLabel("Select Product Type:");
        topPanel.add(productTypeLabel);

        productTypeComboBox = new JComboBox<>(new String[] { "DVD", "Book", "CD" });
        productTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllProducts();
            }
        });
        topPanel.add(productTypeComboBox);

        timeLabel = new JLabel("Current Time: ");
        topPanel.add(timeLabel);

        playButton = new JButton("Play");
        viewTrack = new JButton("View Tracks");
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playMedia();
            }
        });

        viewTrack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTrack();
            }
        });
     

        playButton.setVisible(false);
        viewTrack.setVisible(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(playButton);
        buttonPanel.add(viewTrack);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        productTable = new JTable(tableModel);

        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        productTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        add(tableScrollPane, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSearchSuggestions();
            }
        });

        startTimer();

        displayAllProducts();
    }


    private void startTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCurrentTime();
            }
        }, 0, 1000);
    }

    private void updateCurrentTime() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentTime = sdf.format(new Date());
                timeLabel.setText("Current Time: " + currentTime);
            }
        });
    }

    protected void displayAllProducts() {
        tableModel.setRowCount(0);  
        String selectedProductType = (String) productTypeComboBox.getSelectedItem();
        List<?> products = null;
        
        playButton.setVisible(false);
        viewTrack.setVisible(false);

        if ("DVD".equals(selectedProductType)) {
        	tableModel.setColumnIdentifiers(new String[] {"ID", "Title", "Category", "Director", "Length (minutes)", "Cost"});
            products = dvd.getDVDs();
            setColumnWidthsForDVD();
            playButton.setVisible(true);
        } else if ("Book".equals(selectedProductType)) {
        	tableModel.setColumnIdentifiers((new String[] {"ID", "Title", "Category", "Author List", "Cost"}));
            products = book.getBooks();
            setColumnWidthsForBook();
        } else if ("CD".equals(selectedProductType)) {
        	tableModel.setColumnIdentifiers((new String[] {"ID", "Title", "Category", "Artist", "Director", "Length (minutes)", "Cost","Tracks"}));
            products = cd.getCDs();
            setColumnWidthsForCD();
            playButton.setVisible(true);
            viewTrack.setVisible(true);
        }


        if (products != null) {
            for (Object product : products) {
                String[] rowData = getProductRowData(product);
                tableModel.addRow(rowData);
            }
        }
    }
    public void playMedia() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String Id = (String) tableModel.getValueAt(selectedRow, 0);

            if (Id.startsWith("DVD")) {
                dvd.play(Id);
            } else if (Id.startsWith("CD")) {
            	cd.play(Id);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid Media to play.");
        }
    }

    protected String[] getProductRowData(Object product) {
        if (product instanceof DVD) {
            DVD dvd = (DVD) product;
            return new String[] {
                dvd.getId(),
                dvd.getTitle(),
                dvd.getCategory(),
                dvd.getDirector(),
                String.valueOf(dvd.getLength()),
                String.format("%.2f", dvd.getCost())
            };
        } else if (product instanceof Book) {
            Book book = (Book) product;
            return new String[] {
                book.getId(),
                book.getTitle(),
                book.getCategory(),
                String.join(", ", book.getAuthors()),
                String.format("%.2f", book.getCost())
            };
        } else if (product instanceof CD) {
            CD cd = (CD) product;
            StringBuilder trackInfo = new StringBuilder();
            for (Track track : cd.getTracks()) {
                trackInfo.append(track).append(", ");
            }
            if (trackInfo.length() > 0) {
                trackInfo.setLength(trackInfo.length() - 2);
            }

            return new String[] {
                cd.getId(),
                cd.getTitle(),
                cd.getCategory(),
                cd.getArtists(),
                cd.getDirector(),
                String.valueOf(cd.getTotalLength()),
                String.valueOf(cd.getCost()),
                String.format( "Tracks: " + trackInfo.toString())
            };
        }
        return new String[0];
    }

    protected void updateSearchSuggestions() {
        String query = searchField.getText().trim();
        tableModel.setRowCount(0); 

        if (query.isEmpty()) {
            displayAllProducts(); 
            return;
        }

        String selectedProductType = (String) productTypeComboBox.getSelectedItem();
        List<?> products = null;

        if ("DVD".equals(selectedProductType)) {
            products = dvd.getDVDs();
        } else if ("Book".equals(selectedProductType)) {
            products = book.getBooks();
        } else if ("CD".equals(selectedProductType)) {
            products = cd.getCDs();
        }

        if (products != null) {
            for (Object product : products) {
                if (productMatchesQuery(product, query)) {
                    String[] rowData = getProductRowData(product);
                    tableModel.addRow(rowData);
                }
            }
        }
    }

    private boolean productMatchesQuery(Object product, String query) {
        query = query.toLowerCase();
        if (product instanceof DVD) {
            DVD dvd = (DVD) product;
            return dvd.getId().toLowerCase().contains(query) || 
                   dvd.getTitle().toLowerCase().contains(query) || 
                   dvd.getDirector().toLowerCase().contains(query) || 
                   dvd.getCategory().toLowerCase().contains(query) || 
                   String.valueOf(dvd.getLength()).contains(query) || 
                   String.valueOf(dvd.getCost()).contains(query);
        } else if (product instanceof Book) {
            Book book = (Book) product;
            boolean authorMatches = false;
            for (String author : book.getAuthors()) {
                if (author.toLowerCase().contains(query)) {
                    authorMatches = true;
                    break;
                }
            }
            return book.getId().toLowerCase().contains(query) || 
            	       book.getTitle().toLowerCase().contains(query) || 
            	       authorMatches || 
            	       book.getCategory().toLowerCase().contains(query) || 
            	       String.valueOf(book.getCost()).contains(query);

        } else if (product instanceof CD) {
            CD cd = (CD) product;
            boolean trackMatches = false;
            for (Track track : cd.getTracks()) {
                if (track.getTitle().toLowerCase().contains(query)) {
                    trackMatches = true;
                    break;
                }
            }
            return cd.getId().toLowerCase().contains(query) || 
                   cd.getTitle().toLowerCase().contains(query) || 
                   cd.getArtists().toLowerCase().contains(query) || 
                   cd.getDirector().toLowerCase().contains(query)||
                   trackMatches||
                   cd.getCategory().toLowerCase().contains(query) || 
                   String.valueOf(cd.getTotalLength()).contains(query) || 
                   String.valueOf(cd.getCost()).contains(query);
        }
        return false;
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

            JButton button1 = new JButton("Play");
            button1.addActionListener(e -> playTrack(table));

            buttonPanel.add(button1);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            frame.add(panel);
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a valid CD to view.");
        }
    }

    
    private static void playTrack(JTable table) {
    	int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String Id = (String) table.getValueAt(selectedRow, 0);
            Track track = Track.getTrackById(Id);
            track.play(Id);
            }else {
            	JOptionPane.showMessageDialog(null, "Please select a track to play.");
            }
    }
    private void setColumnWidthsForCD() {
        TableColumn column = productTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column = productTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(200);
        column = productTable.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);
        column = productTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(150);
        column = productTable.getColumnModel().getColumn(4);
        column.setPreferredWidth(150);
        column = productTable.getColumnModel().getColumn(5);
        column.setPreferredWidth(100);
        column = productTable.getColumnModel().getColumn(6);
        column.setPreferredWidth(50);
        column = productTable.getColumnModel().getColumn(7);
        column.setPreferredWidth(300);
    }
    private void setColumnWidthsForBook() {
        TableColumn column = productTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column = productTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(250);
        column = productTable.getColumnModel().getColumn(2);
        column.setPreferredWidth(150);
        column = productTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(300);
        column = productTable.getColumnModel().getColumn(4); 
        column.setPreferredWidth(100);
    }
    private void setColumnWidthsForDVD() {
        TableColumn column = productTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column = productTable.getColumnModel().getColumn(1); 
        column.setPreferredWidth(250);
        column = productTable.getColumnModel().getColumn(2);
        column.setPreferredWidth(150);
        column = productTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(200);
        column = productTable.getColumnModel().getColumn(4);
        column.setPreferredWidth(100);
        column = productTable.getColumnModel().getColumn(5); 
        column.setPreferredWidth(100);
    }


}

package frame;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import model.DVD;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseFrame extends JFrame {
    protected JTable productTable;
    protected DefaultTableModel tableModel;
    protected JTextField searchField;
    protected JLabel suggestionLabel;
    protected JComboBox<String> sortOptions;
    protected JLabel timeLabel;
	private DVD DVD; 
    protected static final int WIDTH = 900;
    protected static final int HEIGHT = 650;

    public BaseFrame(DVD dvd) {
        this.DVD = new DVD();
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
        searchField = new JTextField(15);
        searchPanel.add(searchField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        suggestionLabel = new JLabel();
        searchPanel.add(suggestionLabel, gbc);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);

        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(timeLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = { "ID", "Title", "Category", "Director", "Length(minutes)", "Cost" };
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        productTable.setFillsViewportHeight(true); 

        int[] columnWidths = {20, 360, 150, 250, 50, 50}; 
        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = productTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]); 
            column.setResizable(true); 
        }

        add(new JScrollPane(productTable), BorderLayout.CENTER);

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
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCurrentTime();
            }
        }, 0, 1000);

        displayAllProducts();
    }

    private void updateCurrentTime() {
        SwingUtilities.invokeLater(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            timeLabel.setText("Current Time: " + sdf.format(new Date()));
        });
    }

    protected void displayAllProducts() {
        tableModel.setRowCount(0);  

        List<DVD> DVDs = DVD.getDVDs(); 

        for (DVD dvd : DVDs) {
            String[] rowData = getProductRowData(dvd); 
            tableModel.addRow(rowData); 
        }
    }

    protected String[] getProductRowData(DVD dvd) {
        return new String[] {
            dvd.getId(),                           
            dvd.getTitle(),                        
            dvd.getCategory(),                     
            dvd.getDirector(),                     
            String.valueOf(dvd.getLength()),      
            String.format("%.2f", dvd.getCost())  
        };
    }

    protected void updateSearchSuggestions() {
        String query = searchField.getText().trim();
        tableModel.setRowCount(0); 

        if (query.isEmpty()) {
            suggestionLabel.setText(""); 
            displayAllProducts(); 
            return;
        }

        List<DVD> dvds = DVD.getDVDs(); 
        List<DVD> filteredDVDs = dvds.stream()
            .filter(dvd -> dvdMatchesQuery(dvd, query)) 
            .collect(Collectors.toList());

        if (!filteredDVDs.isEmpty()) {
            for (DVD dvd : filteredDVDs) {
                String[] rowData = getProductRowData(dvd); 
                tableModel.addRow(rowData); 
            }
        }
    }

    private boolean dvdMatchesQuery(DVD dvd, String query) {
        query = query.toLowerCase();
        return dvd.getId().toLowerCase().contains(query) || 
               dvd.getTitle().toLowerCase().contains(query) || 
               dvd.getDirector().toLowerCase().contains(query) || 
               dvd.getCategory().toLowerCase().contains(query) || 
               String.valueOf(dvd.getLength()).contains(query) || 
               String.valueOf(dvd.getCost()).contains(query);
    }
}

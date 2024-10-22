package frame;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
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
import model.Book;
import model.Product;
import model.Stationery;
import services.Store;
import model.Toy;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseFrame extends JFrame {
    protected Store store;
    protected JTable productTable;
    protected DefaultTableModel tableModel;
    protected JTextField searchField;
    protected JLabel suggestionLabel;
    protected JComboBox<String> sortOptions;
    protected JLabel timeLabel; 
    protected static final int WIDTH = 1285;
    protected static final int HEIGHT = 900;

    public BaseFrame(Store store) {
        this.store = store;
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        URL url = getClass().getClassLoader().getResource("icon.png");
        if (url != null) {
            Image icon = Toolkit.getDefaultToolkit().getImage(url);
            setIconImage(icon);
        } else {
            System.err.println("Icon not found!");
        }
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

        String[] sortingCriteria = { "ID", "Price", "Quantity", "Product Type" };
        sortOptions = new JComboBox<>(sortingCriteria);
        sortOptions.addActionListener(e -> updateSortedProductDisplay());
        gbc.gridx = 2;
        searchPanel.add(sortOptions, gbc);

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



        String[] columnNames = { "ID", "Type", "Name", "Price", "Quantity","Input Price", "Brand", "Suit Age", "Material", "Author", "ISBN", "Publication Year", "Publisher" };
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        productTable.setFillsViewportHeight(true); 

        int[] columnWidths = {20, 60, 300, 50, 50,0, 100, 60, 75, 100, 100, 125, 225}; 
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

        List<Product> products = store.getProducts();
        products = sortProducts(products);

        for (Product product : products) {
            String[] rowData = getProductRowData(product);
            tableModel.addRow(rowData);
        }
        TableColumn inputPriceColumn = productTable.getColumnModel().getColumn(5);
        inputPriceColumn.setMinWidth(0);
        inputPriceColumn.setMaxWidth(0);
        inputPriceColumn.setPreferredWidth(0);
    }

    protected String[] getProductRowData(Product product) {
        String type = getProductType(product);

        if (product instanceof Book) {
            Book book = (Book) product;
            return new String[] {
                book.getId(), type, book.getName(), String.format("%.2f", book.getPrice()), String.valueOf(book.getQuantity()), String.valueOf(book.getInputPrice()),
                "", "", "", book.getAuthor(), book.getIsbn(), String.valueOf(book.getPublicationYear()), book.getPublisher()
            };
        } else if (product instanceof Toy) {
            Toy toy = (Toy) product;
            return new String[] {
                toy.getId(), type, toy.getName(), String.format("%.2f", toy.getPrice()), String.valueOf(toy.getQuantity()), String.valueOf(toy.getInputPrice()),
                toy.getBrand(), String.valueOf(toy.getSuitAge()), toy.getMaterial(), "", "", ""
            };
        } else if (product instanceof Stationery) {
            Stationery stationery = (Stationery) product;
            return new String[] {
                stationery.getId(), type, stationery.getName(), String.format("%.2f", stationery.getPrice()), String.valueOf(stationery.getQuantity()), String.valueOf(stationery.getInputPrice()),
                stationery.getBrand(), "", stationery.getMaterial(), "", "", ""
            };
        }
        return new String[] { "Unknown", "", "", "", "", "", "", "", "", "", "", "", "" };
    }

    protected List<Product> sortProducts(List<Product> products) {
        String selectedCriteria = (String) sortOptions.getSelectedItem();

        return products.stream()
                .sorted((p1, p2) -> {
                    switch (selectedCriteria) {
                        case "ID":
                            return Integer.compare(Integer.parseInt(p1.getId()), Integer.parseInt(p2.getId()));
                        case "Price":
                            return Double.compare(p1.getPrice(), p2.getPrice());
                        case "Quantity":
                            return Integer.compare(p1.getQuantity(), p2.getQuantity());
                        case "Product Type":
                            return getProductType(p1).compareTo(getProductType(p2));
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    private String getProductType(Product product) {
        if (product instanceof Book) {
            return "Book";
        } else if (product instanceof Toy) {
            return "Toy";
        } else if (product instanceof Stationery) {
            return "Stationery";
        }
        return "";
    }

    private void updateSortedProductDisplay() {
        displayAllProducts();
    }

    protected void updateSearchSuggestions() {
        String query = searchField.getText().trim();
        tableModel.setRowCount(0);

        if (query.isEmpty()) {
            suggestionLabel.setText("");
            displayAllProducts();
            return;
        }

        List<Product> products = store.getProducts();
        List<Product> filteredProducts = products.stream()
            .filter(product -> productMatchesQuery(product, query))
            .collect(Collectors.toList());

        if (!filteredProducts.isEmpty()) {
            for (Product product : filteredProducts) {
                String[] rowData = getProductRowData(product);
                tableModel.addRow(rowData);
            }
        }
    }

    private boolean productMatchesQuery(Product product, String query) {
        query = query.toLowerCase();

        if (product.getId().toLowerCase().contains(query) || 
            product.getName().toLowerCase().contains(query) || 
            String.valueOf(product.getPrice()).contains(query) ||
            String.valueOf(product.getQuantity()).contains(query) || 
            getProductType(product).toLowerCase().contains(query)) {
            return true;
        }

        if (product instanceof Book) {
            Book book = (Book) product;
            return book.getAuthor().toLowerCase().contains(query) || 
                   book.getIsbn().toLowerCase().contains(query) ||
                   String.valueOf(book.getPublicationYear()).contains(query) ||
                   book.getPublisher().contains(query);
        } else if (product instanceof Toy) {
            Toy toy = (Toy) product;
            return toy.getBrand().toLowerCase().contains(query) || 
                   String.valueOf(toy.getSuitAge()).contains(query) || 
                   toy.getMaterial().toLowerCase().contains(query);
        } else if (product instanceof Stationery) {
            Stationery stationery = (Stationery) product;
            return stationery.getBrand().toLowerCase().contains(query) || 
                   stationery.getMaterial().toLowerCase().contains(query);
        }

        return false;
    }
}

package frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import services.MySQLConnection;
import model.CD;
import model.Book;
import model.DVD;
import model.Media;

public class CustomerFrame extends BaseFrame {
    private Map<Media, Integer> cart;
    protected Media media;
    protected DVD dvd;
    protected Book book;
    protected CD cd;

    public CustomerFrame(DVD dvd, Book book,CD cd) { 
        super(dvd, book, cd); 
        this.dvd=dvd;
        this.book = book;
        this.cd=cd;
        this.cart = new HashMap<>();  
        setTitle("Customer Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");
        JButton viewCartButton = new JButton("View Cart");
        JButton checkoutButton = new JButton("Check out");
        JButton logoutButton = new JButton("Exit");

        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(logoutButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.SOUTH);

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMediaToCart();
            }
        });

        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCart();
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        displayAllProducts();
    }
    private void addMediaToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String addId = (String) tableModel.getValueAt(selectedRow, 0);
            Media addmedia = null;

            if (addId.startsWith("DVD")) {
                addmedia = DVD.getDVDById(addId);
            } else if (addId.startsWith("B")) {
                addmedia = Book.getBookById(addId);
            } else if (addId.startsWith("CD")) {
                addmedia = CD.getCDById(addId); 
            }

            if (addmedia != null) {
                boolean found = false;
                for (Media mediaInCart : cart.keySet()) {
                    if (mediaInCart.getTitle().equals(addmedia.getTitle())) { 
                        cart.put(mediaInCart, cart.get(mediaInCart) + 1);
                        JOptionPane.showMessageDialog(this, addmedia.getTitle() + " quantity increased to " + cart.get(mediaInCart));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    cart.put(addmedia, 1);
                    JOptionPane.showMessageDialog(this, addmedia.getTitle() + " has been added to your cart.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid Media to add to cart.");
        }
    }

    private void viewCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(cart.size() + 2, 4));
        panel.add(new JLabel("Media Title"));
        panel.add(new JLabel("Price"));
        panel.add(new JLabel("Quantity"));
        panel.add(new JLabel("Total Price"));

        List<Media> cartList = new ArrayList<>(cart.keySet());
        double totalCost = 0.0;
        for (Media media : cartList) {
            int quantity = cart.get(media);
            double itemTotalPrice = media.getCost() * quantity;
            totalCost += itemTotalPrice;

            JTextField titleField = new JTextField(media.getTitle());
            titleField.setEditable(false);
            JTextField priceField = new JTextField(String.format("%.2f", media.getCost()));
            priceField.setEditable(false);
            JTextField quantityField = new JTextField(String.valueOf(quantity));
            double totalPrice = media.getCost() * quantity;
            JTextField totalPriceField = new JTextField(String.format("%.2f", totalPrice));
            totalPriceField.setEditable(false);

            panel.add(titleField);
            panel.add(priceField);
            panel.add(quantityField);
            panel.add(totalPriceField);
        }
        panel.add(new JLabel("Total Cost:"));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        JTextField totalCostField = new JTextField(String.format("%.2f", totalCost));
        totalCostField.setEditable(false);
        panel.add(totalCostField);

        int result = JOptionPane.showConfirmDialog(this, panel, "View Cart", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            for (int index = 0; index < cartList.size(); index++) {
                JTextField quantityField = (JTextField) panel.getComponent(4 + (index * 4) + 2);
                String quantityText = quantityField.getText().trim();

                try {
                    int newQuantity = Integer.parseInt(quantityText);
                    if (newQuantity <= 0) {
                        cart.remove(cartList.get(index));
                        JOptionPane.showMessageDialog(this, cartList.get(index).getTitle() + " removed from cart.");
                    }else if (newQuantity >= 20) {
                    	JOptionPane.showMessageDialog(this, "You cannot purchase more than 20 items at once. ");
                    }
                    else {
                        cart.put(cartList.get(index), newQuantity);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity for " + cartList.get(index).getTitle());
                }
            }
        }
    }


    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        String deliveryAddress = JOptionPane.showInputDialog(this, "Enter your delivery address:");
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Delivery address is required.");
            return;
        }

        String deliveryInstructions = JOptionPane.showInputDialog(this, "Enter delivery instructions (if any):");
        if (deliveryInstructions == null || deliveryInstructions.trim().isEmpty()) {
            deliveryInstructions = "";
        }

        double totalCostBeforeVAT = 0.0;
        double totalMass = 0.0;
        int totalQuantity = 0;
        List<Media> cartList = new ArrayList<>(cart.keySet());

        for (Media media : cartList) {
            int quantity = cart.get(media);
            totalCostBeforeVAT += media.getCost() * quantity;
            totalMass += 5 * quantity; 
            totalQuantity += quantity;
        }

        double vat = totalCostBeforeVAT * 0.15;
        double deliveryFee = 5 * totalMass;
        double totalCostAfterVAT = totalCostBeforeVAT + vat + deliveryFee;

        Media freeItem = (totalQuantity > 5) ? selectRandomFreeItem(cartList) : null;

        if (freeItem != null) {
            totalCostAfterVAT -= freeItem.getCost() * 1.15;
            totalCostBeforeVAT -= freeItem.getCost();
        }

        String orderId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        StringBuilder invoice = new StringBuilder();
        invoice.append("Invoice:\n");
        invoice.append("Order ID: ").append(orderId).append("\n");
        invoice.append("Delivery Address: ").append(deliveryAddress).append("\n");
        invoice.append("Delivery Instructions: ").append(deliveryInstructions).append("\n\n");
        invoice.append("Items Purchased:\n");

        for (Map.Entry<Media, Integer> entry : cart.entrySet()) {
            Media media = entry.getKey();
            int quantity = entry.getValue();
            
            if (media.equals(freeItem)) {
                invoice.append(String.format("%s - Price: 0.00 (Free Item)\n", media.getTitle()));
                if (quantity > 1) {
                    invoice.append(String.format("%s - Price: %.2f - Quantity: %d\n", 
                            media.getTitle(), media.getCost(), quantity - 1));
                }
            } else {
                invoice.append(String.format("%s - Price: %.2f - Quantity: %d\n", 
                        media.getTitle(), media.getCost(), quantity));
            }
        }

        invoice.append(String.format("\nTotal Cost Before VAT: %.2f\n", totalCostBeforeVAT));
        invoice.append(String.format("VAT (15%%): %.2f\n", vat));
        invoice.append(String.format("Delivery Fee: %.2f\n", deliveryFee));
        invoice.append(String.format("Total Cost After VAT: %.2f\n", totalCostAfterVAT));

        JOptionPane.showMessageDialog(this, invoice.toString());

        processPayment(totalCostAfterVAT, orderId, deliveryAddress, deliveryInstructions);
        cart.clear();
    }

    private Media selectRandomFreeItem(List<Media> dvdList) {
        if (dvdList.isEmpty()) {
            return null;
        }        int randomIndex = (int) (Math.random() * dvdList.size());
        return dvdList.get(randomIndex);
    }


    private void processPayment(double amount, String orderId, String deliveryAddress, String deliveryInstructions) {
        JTextField cardOwnerField = new JTextField();
        JTextField cardNumberField = new JTextField();

        JPanel paymentPanel = new JPanel(new GridLayout(0, 2));
        paymentPanel.add(new JLabel("Card Owner:"));
        paymentPanel.add(cardOwnerField);
        paymentPanel.add(new JLabel("Card Number:"));
        paymentPanel.add(cardNumberField);

        int result = JOptionPane.showConfirmDialog(this, paymentPanel, "Payment Information", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String cardOwner = cardOwnerField.getText().trim();
            String cardNumber = cardNumberField.getText().trim();

            if (cardOwner.isEmpty() || cardNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Card owner and card number cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String insertOrderSQL = "INSERT INTO orders (orderID, cardOwner, cardNumber, totalCost, status, address, instructions) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = new MySQLConnection().getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertOrderSQL)) {
                preparedStatement.setString(1, orderId);
                preparedStatement.setString(2, cardOwner);
                preparedStatement.setString(3, cardNumber);
                preparedStatement.setDouble(4, amount);
                preparedStatement.setString(5, "pending");
                preparedStatement.setString(6, deliveryAddress);
                preparedStatement.setString(7, deliveryInstructions);

                preparedStatement.executeUpdate();

                String paymentSummary = String.format("Payment of %.2f has been processed.\nOrder ID: %s", amount, orderId);
                JOptionPane.showMessageDialog(this, paymentSummary);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error saving order to database: " + e.getMessage());
            }
        }
    }

    private void logout() {
		RoleSelectionFrame roleselectionframe = new RoleSelectionFrame();
		roleselectionframe.setVisible(true);
        dispose();
    }
}

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
import model.DVD;

public class CustomerFrame extends BaseFrame {
    private Map<DVD, Integer> cart;
    private DVD dvd;

    public CustomerFrame(DVD dvd) { 
        super(dvd); 
        this.dvd=dvd;
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
                addToCart();
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

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String dvdId = (String) tableModel.getValueAt(selectedRow, 0);
            for (DVD dvd : cart.keySet()) {
                if (dvd.getId().equals(dvdId)) {
                    cart.put(dvd, cart.get(dvd) + 1);
                    JOptionPane.showMessageDialog(this, dvd.getTitle() + " quantity increased to " + cart.get(dvd));
                    return; 
                }
            }
            for (DVD dvd : dvd.getDVDs()) {
            	if (dvd.getId().equals(dvdId)) {
                    cart.put(dvd, 1);
                    JOptionPane.showMessageDialog(this, dvd.getTitle() + " has been added to your cart.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "DVD not found in the store.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid DVD to add to cart.");
        }
    }
    private void viewCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(cart.size() + 1, 4));
        panel.add(new JLabel("DVD Title"));
        panel.add(new JLabel("Price"));
        panel.add(new JLabel("Quantity"));
        panel.add(new JLabel("Total Price"));

        List<DVD> dvdList = new ArrayList<>(cart.keySet());

        for (DVD dvd : dvdList) {
            int quantity = cart.get(dvd);

            JTextField titleField = new JTextField(dvd.getTitle());
            titleField.setEditable(false);
            JTextField priceField = new JTextField(String.format("%.2f", dvd.getCost()));
            priceField.setEditable(false);
            JTextField quantityField = new JTextField(String.valueOf(quantity));
            double totalPrice = dvd.getCost() * quantity;
            JTextField totalPriceField = new JTextField(String.format("%.2f", totalPrice));
            totalPriceField.setEditable(false);

            panel.add(titleField);
            panel.add(priceField);
            panel.add(quantityField);
            panel.add(totalPriceField);
        }

        int result = JOptionPane.showConfirmDialog(this, panel, "View Cart", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            for (int index = 0; index < dvdList.size(); index++) {
                JTextField quantityField = (JTextField) panel.getComponent(4 + (index * 4) + 2);
                String quantityText = quantityField.getText().trim();

                try {
                    int newQuantity = Integer.parseInt(quantityText);
                    if (newQuantity <= 0) {
                        cart.remove(dvdList.get(index));
                        JOptionPane.showMessageDialog(this, dvdList.get(index).getTitle() + " removed from cart.");
                    } else {
                        cart.put(dvdList.get(index), newQuantity);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity for " + dvdList.get(index).getTitle());
                }
            }
        }
    }
    private DVD selectRandomFreeItem(List<DVD> dvdList) {
        if (dvdList.isEmpty()) {
            return null;
        }        int randomIndex = (int) (Math.random() * dvdList.size());
        return dvdList.get(randomIndex);
    }
    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        String deliveryAddress = JOptionPane.showInputDialog(this, "Enter your delivery address:");
        String deliveryInstructions = JOptionPane.showInputDialog(this, "Enter delivery instructions (if any):");

        double totalCostBeforeVAT = 0.0;
        double totalMass = 0.0;
        int totalQuantity = 0;

        List<DVD> dvdList = new ArrayList<>(cart.keySet());
        for (Map.Entry<DVD, Integer> entry : cart.entrySet()) {
            DVD dvd = entry.getKey();
            int quantity = entry.getValue();
            totalCostBeforeVAT += dvd.getCost() * quantity;
            totalMass += 5 * quantity;
            totalQuantity += quantity;
        }
        if (totalQuantity > 20) {
            JOptionPane.showMessageDialog(this, "You cannot purchase more than 20 DVDs at once.");
            return;
        }
        double vat = totalCostBeforeVAT * 0.15;
        double deliveryFee = 5 * totalMass;
        double totalCostAfterVAT = totalCostBeforeVAT + vat + deliveryFee;

        DVD freeItem = (totalQuantity > 5) ? selectRandomFreeItem(dvdList) : null;

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
        invoice.append("DVDs Purchased:\n");

        for (Map.Entry<DVD, Integer> entry : cart.entrySet()) {
            DVD dvd = entry.getKey();
            int quantity = entry.getValue();
            if (dvd.equals(freeItem)) {
                invoice.append(String.format("%s - Price: 0.00 (Free Item)\n", dvd.getTitle()));
                if (quantity > 1) {
                    invoice.append(String.format("%s - Price: %.2f - Quantity: %d\n", 
                            dvd.getTitle(), dvd.getCost(), quantity - 1));
                }
            } else {
                invoice.append(String.format("%s - Price: %.2f - Quantity: %d\n", 
                        dvd.getTitle(), dvd.getCost(), quantity));
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
		RoleSelectionFrame roleselectionframe = new RoleSelectionFrame(dvd);
		roleselectionframe.setVisible(true);
        dispose();
    }
}
